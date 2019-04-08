package bar;

import domain.Order;
import messaging.gateway.BarHubGateway;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Max Meijer
 * Created on 27/03/2019
 */
@SuppressWarnings("Duplicates")
public class BarClient {
    private static final Logger LOGGER = Logger.getLogger("BarClient");
    private static BarHubGateway gateway = new BarHubGateway("HubBar", "BarHub");
    private static List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {

        gateway.addListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                ObjectMessage object = (ObjectMessage) message;
                try {
                    Order order = (Order) object.getObject();
                    handleOrder(order);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Enter an id to complete the order ");
        Scanner in = new Scanner(System.in);
        int id = in.nextInt();

        if(id > -1) {
            completeOrder(id);
        }
    }

    private static void completeOrder(int id) {
        LOGGER.log(Level.INFO,"Complete order: " + id);
        orders = orders.stream().filter(o -> o.getId() != id).collect(Collectors.toList());
        gateway.completeOrder(id);
    }

    private static void handleOrder(Order order) {
        LOGGER.log(Level.INFO, "Bar has received order: " + order.toString(), " Get to work!");
    }
}
