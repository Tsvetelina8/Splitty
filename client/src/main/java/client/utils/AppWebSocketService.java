package client.utils;

import client.utils.interfaces.WebSocketCallback;
import client.utils.interfaces.WebSocketService;
import org.java_websocket.client.WebSocketClient;

import java.net.URI;

public class AppWebSocketService implements WebSocketService {
    
    private final WebSocketClient webSocketClient;
    private int subCount;

    /**
     * Constructor for AppWebSocketService
     * 
     * @param serverUri  websocket endpoint URI
     * @param callback   who to call back when message is received or 
     *                   when connected
     */
    public AppWebSocketService(URI serverUri, WebSocketCallback callback) {
        this.webSocketClient = new CustomWebSocketClient(serverUri, callback);
        subCount = 0;
    }

    /**
     * Constructor used for testing
     * @param webSocketClient the websocketclient to inject
     */
    public AppWebSocketService(CustomWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
        subCount = 0;
    }

    /**
     * Method to connect to websocket
     */
    @Override
    public void connect() {
        webSocketClient.connect();
    }

    /**
     * Method to send message to websocket
     */
    @Override
    public void send(String message) {
        if (webSocketClient.isOpen()) {
            webSocketClient.send(message);
        }
    }

    /**
     * Method to subscribe to specific topic
     * 
     * @param destination  topic to subscribe to, e.g.: "/topic/person"
     */
    @Override
    public void subscribe(String destination) {
        System.out.println("SUBSCRIBING TO TOPICS");

        String subscribeFrame = "SUBSCRIBE\nid:sub-" + subCount + 
            "\ndestination:" + destination + "\n\n\u0000";
        
        send(subscribeFrame);

        subCount++;
    }

    /**
     * Method to unsubscribe from topics and close connection
     */
    @Override
    public void close() {
        for (;subCount >= 0; subCount--) {
            String unsubscribeFrame = "UNSUBSCRIBE\nid:sub-" + subCount + "\n\n\u0000";
            send(unsubscribeFrame);
        }

        String disconnectFrame = "DISCONNECT\n\n\u0000";
        send(disconnectFrame);

        webSocketClient.close();
    }
}