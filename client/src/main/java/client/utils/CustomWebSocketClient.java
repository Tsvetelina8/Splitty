package client.utils;

import client.utils.interfaces.WebSocketCallback;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class CustomWebSocketClient extends WebSocketClient {

    private WebSocketCallback callback;

    /**
     * Constructor for CustomWebSocketClient
     *
     * @param serverUri  URI of the websocket
     * @param callback   class to call when there is message received
     *                   or when connected
     */
    public CustomWebSocketClient(URI serverUri, WebSocketCallback callback) {
        super(serverUri);
        this.callback = callback;
    }

    /**
     * Method called when the connection is open.
     * After opening the connection, the connect STOMP
     * frame is sent to websocket.
     *
     * @param handshakedata  handshake data
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Opened connection");

        String connectFrame = """
                    CONNECT
                    accept-version:1.2

                    \u0000
                    """;

        send(connectFrame);
    }

    /**
     * Method called when there is message received
     * When message starts with MESSAGE it is perceived as STOMP message
     * and callback is notified.
     * When starts with CONNECTED, it is perceived as successful STOMP
     * connection and callback is also notified.
     *
     * @param message message that was received
     */
    @Override
    public void onMessage(String message) {
        System.out.println("RECEIVED MESSAGE");
        System.out.println(message);

        if (callback != null && message.startsWith("MESSAGE"))
            callback.onMessageReceived(message);

        if (callback != null && message.startsWith("CONNECTED"))
            callback.onConnected();
    }

    /**
     * Method called when the connection is closed
     *
     * @param code    code of close
     * @param reason  reason behind closing
     * @param remote  whether it was closed by server
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        String message = "Connection closed by ";
        if (remote) {
            message += "server, Info: ";
        } else {
            message += "client, Info: ";
        }
        message += reason;
        System.out.println(message);
        System.out.println("Code: " + code);
    }

    /**
     * Method called when there was error
     *
     * @param ex     exception caught during websocket connection
     */
    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}