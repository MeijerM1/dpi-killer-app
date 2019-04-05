package gateway;

import dal.OrderRepository;
import domain.Ingredient;
import domain.Item;
import domain.Order;
import domain.Stock;
import messaging.gateway.TableOrderGateway;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Meijer
 * Created on 27/02/2019
 */
public class OrderGateway {

    private static OrderRepository orderRepository = new OrderRepository();
    private static TableOrderGateway gateway = new TableOrderGateway("OrderTable", "TableOrder");
    private static Stock stock = new Stock();

    public static void main(String[] args) {
        gateway.addListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                ObjectMessage object = (ObjectMessage) message;
                try {
                    Order order = (Order) object.getObject();
                    handleOrder(order);
                    System.out.println(order);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void handleOrder(Order order) {
        for (Item i: order.getItems()) {
            if(!stock.hasStock(i)) {
                // TODO handle no stock for order
                System.out.println("WE DONT HAVE THE INGREDIENTS");
                return;
            }
        }
        // remove ingredients from stock
        stock.useIngredientsForOrder(order);

        orderRepository.add(order);

        // TODO send order to kitchen or bar
    }
}
