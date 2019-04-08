package bar;

import domain.Order;
import messaging.gateway.BarHubGateway;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Max Meijer
 * Created on 27/03/2019
 */
public class BarClient {
    private static final Logger LOGGER = Logger.getLogger("BarClient");
    private static BarHubGateway gateway = new BarHubGateway("HubBar", "BarHub");

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
    }

    private static void handleOrder(Order order) {
        LOGGER.log(Level.INFO, "Bar has received order: " + order.toString(), " Get to work!");
    }
}
