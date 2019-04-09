package messaging.gateway;

import messaging.MessageReceiverGateway;

/**
 * @author Max Meijer
 * Created on 09/04/2019
 */
public class ErrorGateway {
    public MessageReceiverGateway receiver;

    public ErrorGateway(String receiverQueue) {
        receiver = new MessageReceiverGateway(receiverQueue, false, true);
    }
}
