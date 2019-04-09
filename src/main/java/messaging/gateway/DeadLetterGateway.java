package messaging.gateway;

import messaging.MessageSenderGateway;
import org.apache.activemq.store.MessageStore;
import org.apache.activemq.store.memory.MemoryMessageStore;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author Max Meijer
 * Created on 09/04/2019
 */
public class DeadLetterGateway {

    private DeadLetterReceiver receiver;
    private MessageSenderGateway sender;

    public DeadLetterGateway(String receiverQueue) {
        receiver = new DeadLetterReceiver(receiverQueue);
        sender = new MessageSenderGateway("DeadTable", true, false);
    }

    public void addListener(MessageListener listener) {
        try {
            receiver.consumer.setMessageListener(listener);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        sender.sendMessage(message);
    }
}
