package producer;

//import util.properties packages
import java.util.Properties;

//import simple producer packages
import com.sun.xml.internal.ws.api.message.Message;
import org.apache.kafka.clients.producer.Producer;

//import KafkaProducer packages
import org.apache.kafka.clients.producer.KafkaProducer;

//import ProducerRecord packages
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author Max Meijer
 * Created on 27/02/2019
 */
public class MyProducer {


    public void send() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:2181");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("group.id", "test-group");

        KafkaProducer kafkaProducer = new KafkaProducer(properties);
        try {
            for (int i = 0; i < 100; i++) {
                System.out.println(i);
                kafkaProducer.send(new ProducerRecord("devglan-test", Integer.toString(i), "test message - " + i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaProducer.close();
        }
    }
}
