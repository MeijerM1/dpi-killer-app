package messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jndi.ReadOnlyContext;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * @author Max Meijer
 * Created on 09/04/2019
 */
@SuppressWarnings("Duplicates")
public class DeadLetterReceiver {
    private Connection connection;
    Session session;

    public Destination receiveDestination;

    public MessageConsumer consumer; // for receiving messages

    private String queueName;
    private boolean isReceiver;

    public DeadLetterReceiver(String queue) {
        this.queueName = queue;
        setup();
    }

    private void setup() {
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");

            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            props.put(("queue." + queueName), queueName);

            Context jndiContext = new InitialContext(props);
            ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            //connectionFactory.setTrustAllPackages(true);
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // connect to the receiver destination
            receiveDestination = (Destination) jndiContext.lookup(queueName);
            consumer = session.createConsumer(receiveDestination);

            connection.start(); // this is needed to start receiving messages
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

}
