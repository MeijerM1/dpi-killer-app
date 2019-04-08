package messaging.gateway;

import domain.Order;
import messaging.MessageReceiverGateway;
import messaging.MessageSenderGateway;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author Max Meijer
 * Created on 03/04/2019
 */
public class TableOrderGateway {
    private MessageReceiverGateway receiver;
    private MessageSenderGateway sender;

    public TableOrderGateway(String senderQueue, String receiverQueue) {
        receiver = new MessageReceiverGateway(receiverQueue);
        sender = new MessageSenderGateway(senderQueue);
    }

    public void addListener(MessageListener listener) {
        receiver.addReceiver(listener);
    }

    public void sendOrder(Order order) {
        Message message = sender.createMessage(order);
        sender.sendMessage(message);
    }

    public void sendStatusUpdate(String text) {
        Message message = sender.createTextMessage(text);
        sender.sendMessage(message);
    }
}
