package client.utils;

import client.utils.interfaces.WebSocketCallback;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class CustomWebSocketClientTest {

    private CustomWebSocketClient client;
    @Mock
    private WebSocketCallback webSocketCallback;

    @BeforeEach
    void setUp() throws URISyntaxException {
        MockitoAnnotations.openMocks(this);
        client = new CustomWebSocketClient(new URI("hoi"), webSocketCallback);
    }

    @Test
    void onMessageTestMessage() {
        client.onMessage("MESSAGE");
        Mockito.verify(webSocketCallback, Mockito.times(1)).onMessageReceived(Mockito.anyString());
    }

    @Test
    void onMessageTestConnected() {
        client.onMessage("CONNECTED");
        Mockito.verify(webSocketCallback, Mockito.times(1)).onConnected();
    }

    @Test
    void onMessageTestNull() throws URISyntaxException {
        client = new CustomWebSocketClient(new URI("a"), null);
        client.onMessage("MESSAGE");
        client.onClose(1, "q", true);
        Mockito.verify(webSocketCallback, Mockito.never()).onConnected();
    }

    @Test
    void onCloseTest() {
        client.onClose(1, "q", true);
    }

    @Test
    void onError() {
        Exception exception = Mockito.mock(Exception.class);
        client.onError(exception);
        Mockito.verify(exception, Mockito.times(1)).printStackTrace();
    }
}