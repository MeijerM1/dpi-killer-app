package messaging.gateway;

import domain.Order;

import javax.jms.Message;

/**
 * @author Max Meijer
 * Created on 08/04/2019
 */
public class BarHubGateway extends Gateway{

    public BarHubGateway(String receiverQueue, String senderQueue) {
        super(receiverQueue, senderQueue);
    }

    public void sendOrder(Order order ) {
        Message message = sender.createMessage(order);
        sender.sendMessage(message);
    }

    public void completeOrder(int orderId) {
        Message message = sender.createTextMessage(String.valueOf(orderId));
        sender.sendMessage(message);
    }
}
