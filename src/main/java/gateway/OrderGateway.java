package gateway;

import dal.OrderRepository;
import domain.Ingredient;
import domain.Order;
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

    private OrderRepository orderRepository = new OrderRepository();
    private List<Ingredient> stock = new ArrayList<>();
    private static TableOrderGateway gateway = new TableOrderGateway("OrderTable", "TableOrder");

    public static void main(String[] args) {
        gateway.addListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                ObjectMessage object = (ObjectMessage) message;
                try {
                    Order order = (Order) object.getObject();
                    System.out.println(order);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void send() {

    }
}
