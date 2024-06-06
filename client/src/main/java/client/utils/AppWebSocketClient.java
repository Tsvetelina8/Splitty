package client.utils;

import client.scenes.MainCtrl;
import client.utils.interfaces.WebSocketCallback;
import client.utils.interfaces.WebSocketService;

public class AppWebSocketClient implements WebSocketCallback {
    private WebSocketService webSocketService;
    private final MainCtrl mainCtrl;

    /**
     * Constructor for AppWebSocketClient
     * 
     * @param webSocketService  web socket service (needed to allow mocking)
     * @param mainCtrl          main control (to allow UI actions)
     */
    public AppWebSocketClient(WebSocketService webSocketService, MainCtrl mainCtrl) {
        this.webSocketService = webSocketService;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Setter for web socket service. This allows callbacks from service
     * 
     * @param webSocketService  web socket service
     */
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    /**
     * Method to initialize the web socket client
     * First connects to web socket
     */
    public void initialize() {
        webSocketService.connect();
    }

    /**
     * Method to close the web socket connection
     */
    public void close() {
        webSocketService.close();
    }

    /**
     * Method called whenever a message is received by
     * web socket service
     * 
     * @param message message received
     */
    public void onMessageReceived(String message) {
        if (message.startsWith("MESSAGE")) {
            String destination = message.substring(message.indexOf("destination:")
                + 12, message.indexOf("content-type:") - 1);
            // Extract the message body. We should consider more readable parsing
            String body = message.substring(message.indexOf("\n\n") + 2, message.length() - 1);
            
            // System.out.println("Received: " + body);

            switch (destination){
                case "/topic/person":
                    javafx.application.Platform.runLater(() -> {
                        mainCtrl.refreshEditEvent();
                        mainCtrl.refreshAdminOverview();
                    });
                    break;
                case "/topic/events":
                    javafx.application.Platform.runLater(() -> {
                        mainCtrl.refreshStartScreen();
                        mainCtrl.refreshAdminOverview();
                        mainCtrl.refreshEditEvent();
                    });
                    break;
                case "/topic/event":
                    javafx.application.Platform.runLater(() -> {
                        mainCtrl.refreshAddExpense();
                        mainCtrl.refreshEditEvent();
                    });
                    break;
                default:
            }
        }
    }

    /**
     * Method called whenever web socket service
     * receives a confirmation frame after connecting
     * to STOMP.
     */
    public void onConnected() {
        webSocketService.subscribe("/topic/person");
        webSocketService.subscribe("/topic/events");
        webSocketService.subscribe("/topic/event");
    }
}