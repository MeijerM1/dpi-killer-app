package messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * @author Max Meijer
 * Created on 06/02/2019
 */
@SuppressWarnings("Duplicates")
abstract class Messenger {

    private Connection connection;
    Session session;

    private Destination sendDestination; // reference to a queue/topic destination
    private Destination receiveDestination;

    MessageProducer producer; // for sending messages
    MessageConsumer consumer; // for receiving messages

    private String queueName;
    private boolean isReceiver;
    private boolean useSenderTopic;
    private boolean useReceiverTopic;

    public Messenger(boolean isReceiver, String queue, boolean useSenderTopic, boolean useReceiverTopic) {
        this.queueName = queue;
        this.isReceiver = isReceiver;
        this.useSenderTopic = useSenderTopic;
        this.useReceiverTopic = useReceiverTopic;

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

            if(isReceiver) {
                if(useReceiverTopic) {
                    Topic topic = session.createTopic(queueName);
                    consumer = session.createConsumer(topic);
                } else {
                    // connect to the receiver destination
                    receiveDestination = (Destination) jndiContext.lookup(queueName);
                    consumer = session.createConsumer(receiveDestination);
                }

                connection.start(); // this is needed to start receiving messages
            } else {
                if(useSenderTopic) {
                    Topic topic = session.createTopic(queueName);
                    producer = session.createProducer(topic);
                } else {
                    // connect to the receiver destination
                    sendDestination = (Destination) jndiContext.lookup(queueName);
                    producer = session.createProducer(sendDestination);
                    producer.setTimeToLive(5000);
                }
            }


        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    private void setupTopic(String topicName) {
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

            Topic topic = session.createTopic(topicName);
            consumer = session.createConsumer(topic);
        } catch(Exception e) {

        }
    }
}
