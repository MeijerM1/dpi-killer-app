import gateway.OrderGateway;

/**
 * @author Max Meijer
 * Created on 27/02/2019
 */
public class Main {

    public static void main(String[] args) {
        OrderGateway producer = new OrderGateway();
        producer.send();
    }
}
