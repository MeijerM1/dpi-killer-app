package hub;

import dal.OrderRepository;
import domain.*;
import messaging.gateway.BarHubGateway;
import messaging.gateway.HubKitchenGateway;
import messaging.gateway.StoreGateway;
import messaging.gateway.TableOrderGateway;

import javax.jms.*;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Max Meijer
 * Created on 27/02/2019
 */
@SuppressWarnings("Duplicates")
public class OrderHub {
    private final static Logger LOGGER = Logger.getLogger("OrderHub");

    private static TableOrderGateway tableGateway = new TableOrderGateway("HubTable", "TableHub", true, false);
    private static HubKitchenGateway kitchenGateway = new HubKitchenGateway("KitchenHub", "HubKitchen");
    private static BarHubGateway barGateway = new BarHubGateway("BarHub", "HubBar");
    private static StoreGateway storeGateway = new StoreGateway("StoreHub", "HubStore", false, false);

    private static OrderRepository orderRepository = new OrderRepository();
    private static Stock stock = new Stock(100);
    private static OrderAggregator aggregator = new OrderAggregator();

    public static void main(String[] args) {
        tableGateway.addListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                ObjectMessage object = (ObjectMessage) message;
                try {
                    Order order = (Order) object.getObject();
                    LOGGER.log(Level.INFO, order.toString());
                    handleOrder(order);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        kitchenGateway.addListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    int orderId = Integer.parseInt(textMessage.getText());
                    handleKitchenComplete(orderId);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        barGateway.addListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    int orderId = Integer.parseInt(textMessage.getText());
                    handleBarComplete(orderId);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void handleKitchenComplete(int orderId) {
        LOGGER.log(Level.INFO, "Kitchen order completed: " + orderId);
        aggregator.completeKitchen(orderId);

        if(aggregator.orderComplete(orderId)) {
            completeOrder(orderId);
        }
    }

    private static void handleBarComplete(int orderId) {
        LOGGER.log(Level.INFO, "Bar order completed: " + orderId);
        aggregator.completeBar(orderId);

        if(aggregator.orderComplete(orderId)) {
            completeOrder(orderId);
        }

    }

    private static void completeOrder(int orderId) {
        LOGGER.log(Level.INFO, "Order " + orderId + " complete");
        orderRepository.get(orderId).setCompleteTime(new Date());
        tableGateway.sendStatusUpdate(aggregator.getTableNumberForOrder(orderId), "Order ready");
        LOGGER.log(Level.INFO, orderRepository.get(orderId).toString());
    }

    private static void handleOrder(Order order) {
        for (Item i: order.getItems()) {
            if(!stock.hasStock(i)) {
                LOGGER.log(Level.INFO, "NO INGREDIENTS");
                return;
            }
        }

        LOGGER.log(Level.INFO, "Enough ingredients");
        stock.useIngredientsForOrder(order);

        checkStock();

        orderRepository.add(order);
        sendToProduction(order);
    }

    private static void checkStock() {
        List<Ingredient> ingredients = stock.checkEmpty();

        if(ingredients.size() == 0) {
            LOGGER.log(Level.INFO, "No ingredients are empty");
            return;
        }

        LOGGER.log(Level.INFO, "The following ingredients are empty: " + ingredients);

        StoreOrder order = new StoreOrder();
        order.setIngredients(ingredients);

        storeGateway.orderIngredients(order);
    }

    private static void sendToProduction(Order order) {
        OrderAggregation orderAggregation = new OrderAggregation();
        orderAggregation.setOrderId(order.getId());
        orderAggregation.setTableNumber(order.getTableNumber());

        Order kitchenOrder = order.copy();
        Order barOrder = order.copy();

        for (Item item : order.getItems()) {

            if(item instanceof Drink) {
                // LOGGER.log(Level.INFO, "Item is a Drink " + item.toString());
                barOrder.addItem(item);
            }

            if(item instanceof Dish) {
                // LOGGER.log(Level.INFO, "Item is a Dish " + item.toString());
                kitchenOrder.addItem(item);
            }
        }

        if(kitchenOrder.getItems().size() > 0) {
            orderAggregation.setKitchenOrder(true);
            kitchenGateway.sendOrderToKitchen(kitchenOrder);
        }

        if(barOrder.getItems().size() > 0) {
            orderAggregation.setBarOrder(true);
            barGateway.sendOrder(barOrder);
        }

        aggregator.addOrder(orderAggregation);
    }
}
