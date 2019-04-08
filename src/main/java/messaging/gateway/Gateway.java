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

    public Gateway(String receiverQueue, String senderQueue, boolean useSenderTopic, boolean useReceiverTopic) {
        receiver = new MessageReceiverGateway(receiverQueue, useSenderTopic, useReceiverTopic);
        sender = new MessageSenderGateway(senderQueue, useSenderTopic, useReceiverTopic);
    }

    public void addListener(MessageListener listener) {
        receiver.addReceiver(listener);
    }
}
