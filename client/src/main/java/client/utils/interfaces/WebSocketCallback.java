package client.utils.interfaces;

public interface WebSocketCallback {
    /**
     * Method called when a message is received by 
     * web socket service
     * 
     * @param message  message received
     */
    void onMessageReceived(String message);

    /**
     * Method called whenever a confirmation STOMP
     * frame was received by web socket service
     * after successfully connecting
     */
    void onConnected();
}
