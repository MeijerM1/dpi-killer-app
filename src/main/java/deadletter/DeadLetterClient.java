package deadletter;

import messaging.gateway.DeadLetterGateway;
import org.apache.activemq.store.MessageStore;
import org.apache.activemq.store.memory.MemoryMessageStore;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Meijer
 * Created on 09/04/2019
 */
public class DeadLetterClient {
    private static DeadLetterGateway gateway = new DeadLetterGateway("ActiveMQ.DLQ");
    private static List<Message> store = new ArrayList<>();
    public static void main(String[] args) {
        gateway.addListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                store.add(message);
                for (Message m :
                        store) {
                    System.out.println(m);
                }
                gateway.sendMessage(message);
            }
        });
    }
}
