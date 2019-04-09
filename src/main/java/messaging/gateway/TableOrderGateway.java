package messaging.gateway;

import domain.Order;
import messaging.MessageReceiverGateway;
import messaging.MessageSenderGateway;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author Max Meijer
 * Created on 03/04/2019
 */
public class TableOrderGateway extends Gateway {
    public TableOrderGateway(String receiverQueue, String senderQueue, boolean useSenderTopic, boolean useReceiverTopic) {
        super(receiverQueue, senderQueue, useSenderTopic, useReceiverTopic);
    }

    public void addListener(MessageListener listener) {
        receiver.addReceiver(listener);
    }

    public void sendOrder(Order order) {
        Message message = sender.createMessage(order);
        try {
            message.setIntProperty("tableNumber", order.getTableNumber());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        sender.sendMessage(message);
    }

    public void sendStatusUpdate(int tableNumber, String text) {
        Message message = sender.createTextMessage(text);
        try {
            message.setIntProperty("tableNumber", tableNumber);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        sender.sendMessage(message);
    }
}
