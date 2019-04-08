package messaging.gateway;

import domain.Order;
import messaging.MessageReceiverGateway;
import messaging.MessageSenderGateway;

import javax.jms.MessageListener;

/**
 * @author Max Meijer
 * Created on 06/04/2019
 */
public class Gateway {
    public MessageReceiverGateway receiver;
    public MessageSenderGateway sender;

    public Gateway(String receiverQueue, String senderQueue) {
        receiver = new MessageReceiverGateway(receiverQueue);
        sender = new MessageSenderGateway(senderQueue);
    }

    public void addListener(MessageListener listener) {
        receiver.addReceiver(listener);
    }
}
