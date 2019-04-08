package messaging;

import javax.jms.JMSException;
import javax.jms.MessageListener;

/**
 * @author Max Meijer
 * Created on 13/02/2019
 */
public class MessageReceiverGateway extends Messenger {

    public MessageReceiverGateway(String queue, boolean useSenderTopic, boolean useReceiverTopic) {
        super(true, queue, useSenderTopic, useReceiverTopic);
    }

    public void addReceiver(MessageListener listener) {
        try {
            consumer.setMessageListener(listener);
        } catch (JMSException e) {
            System.out.println();
            e.printStackTrace();
        }
    }
}
