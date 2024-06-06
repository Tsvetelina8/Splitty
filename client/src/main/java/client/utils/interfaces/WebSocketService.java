package client.utils.interfaces;

public interface WebSocketService {
    /**
     * Method to connect to web socket
     */
    void connect();

    /**
     * Method to send a message to web socket
     * 
     * @param message message to send
     */
    void send(String message);

    /**
     * Method to subscribe to a specific topic
     * using a STOMP frame
     * 
     * @param topic   topic to subscribe to
     */
    void subscribe(String topic);

    /**
     * Method to close the websocket connection after
     * unsubscribing from all the topics
     */
    void close();
}
