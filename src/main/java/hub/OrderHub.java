package hub;

import dal.OrderRepository;
import domain.*;
import messaging.gateway.BarHubGateway;
import messaging.gateway.HubKitchenGateway;
import messaging.gateway.TableOrderGateway;

import javax.jms.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Max Meijer
 * Created on 27/02/2019
 */
public class OrderHub {
    private final static Logger LOGGER = Logger.getLogger("OrderHub");
    private static OrderRepository orderRepository = new OrderRepository();
    private static TableOrderGateway tableGateway = new TableOrderGateway("HubTable", "TableHub");
    private static HubKitchenGateway kitchenGateway = new HubKitchenGateway("KitchenHub", "HubKitchen");
    private static BarHubGateway barGateway = new BarHubGateway("BarHub", "HubBar");
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
    }

    private static void handleKitchenComplete(int orderId) {
        LOGGER.log(Level.INFO, "Kitchen order completed: " + orderId);
        aggregator.completeKitchen(orderId);

        if(aggregator.orderComplete(orderId)) {
            LOGGER.log(Level.INFO, "Order complete");
            tableGateway.sendStatusUpdate("Order ready");
        }
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

        orderRepository.add(order);

        sendToProduction(order);
    }

    private static void sendToProduction(Order order) {
        OrderAggregation orderAggregation = new OrderAggregation();
        orderAggregation.setOrderId(order.getId());
        orderAggregation.setTableNumber(order.getTableNumber());

        Order kitchenOrder = order.copy();
        Order barOrder = order.copy();

        for (Item item : order.getItems()) {

            if(item instanceof Drink) {
                LOGGER.log(Level.INFO, "Item is a Drink " + item.toString());
                barOrder.addItem(item);
            }

            if(item instanceof Dish) {
                LOGGER.log(Level.INFO, "Item is a Dish " + item.toString());
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
