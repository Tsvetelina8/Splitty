package client.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.net.URISyntaxException;

@ExtendWith(ApplicationExtension.class)
class AppWebSocketServiceTest {

    @Mock
    private CustomWebSocketClient customWebSocketClient;

    private AppWebSocketService appWebSocketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appWebSocketService = new AppWebSocketService(customWebSocketClient);
    }

    @Test
    void connectTest() {
        appWebSocketService.connect();
        Mockito.verify(customWebSocketClient, Mockito.times(1)).connect();
    }

    @Test
    void sendTest() {
        Mockito.when(customWebSocketClient.isOpen()).thenReturn(true);
        appWebSocketService.send("Hello");
        Mockito.verify(customWebSocketClient, Mockito.times(1)).send("Hello");
    }

    @Test
    void sendTestNotOpen() {
        Mockito.when(customWebSocketClient.isOpen()).thenReturn(false);
        appWebSocketService.send("Hello");
        Mockito.verify(customWebSocketClient, Mockito.never()).send("Hello");
    }

    @Test
    void subscribeTest() {
        String subscribeFrame = "SUBSCRIBE\nid:sub-0\ndestination:hoi\n\n\u0000";
        Mockito.when(customWebSocketClient.isOpen()).thenReturn(true);
        appWebSocketService.subscribe("hoi");
        Mockito.verify(customWebSocketClient, Mockito.times(1)).send(subscribeFrame);
    }

    @Test
    void closeTest() {
        appWebSocketService.close();
        Mockito.verify(customWebSocketClient, Mockito.times(1)).close();
    }
}