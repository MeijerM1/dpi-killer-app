package messaging.gateway;

import com.sun.org.apache.bcel.internal.generic.StoreInstruction;
import domain.StoreOrder;

import javax.jms.Message;

/**
 * @author Max Meijer
 * Created on 10/04/2019
 */
public class StoreGateway extends Gateway {
    public StoreGateway(String receiverQueue, String senderQueue, boolean useSenderTopic, boolean useReceiverTopic) {
        super(receiverQueue, senderQueue, useSenderTopic, useReceiverTopic);
    }

    public void orderIngredients(StoreOrder order) {
        Message message = sender.createMessage(order);
        sender.sendMessage(message);
    }
}
