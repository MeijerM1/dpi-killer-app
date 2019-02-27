import consumer.MyConsumer;
import producer.MyProducer;

/**
 * @author Max Meijer
 * Created on 27/02/2019
 */
public class Main {

    public static void main(String[] args) {
        MyProducer producer = new MyProducer();
        producer.send();
    }
}
