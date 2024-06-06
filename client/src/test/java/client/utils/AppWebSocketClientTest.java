package client.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import client.scenes.MainCtrl;
import javafx.application.Platform;

import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class AppWebSocketClientTest {

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private ServerHandshake serverHandshake;

    private AppWebSocketClient client;

    @Mock
    private AppWebSocketService service;

    private MockedStatic<Platform> platform;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        client = new AppWebSocketClient(service, mainCtrl);
        doNothing().when(service).send(anyString());

        platform = Mockito.mockStatic(Platform.class);
    }

    @AfterEach
    void tearDown() {
        platform.close();
    }

    @Test
    void testOnMessageReceivedRefreshesEditEvent() {
        String message = "MESSAGE\ndestination:/topic/person\ncontent-type:text/plain\n\n{}\u0000";
        
        platform.when(() -> Platform.runLater(any(Runnable.class)))
            .thenAnswer(invocation -> {
                Runnable runnable = invocation.getArgument(0);
                runnable.run();
                return null;
            });

        client.onMessageReceived(message);

        verify(mainCtrl, times(1)).refreshEditEvent();
    }

    @Test
    void testOnMessageReceivedRefreshesStartScreen() {
        String message = "MESSAGE\ndestination:/topic/events\ncontent-type:text/plain\n\n{}\u0000";

        platform.when(() -> Platform.runLater(any(Runnable.class)))
                .thenAnswer(invocation -> {
                    Runnable runnable = invocation.getArgument(0);
                    runnable.run();
                    return null;
                });

        client.onMessageReceived(message);

        verify(mainCtrl, times(1)).refreshStartScreen();
    }

    @Test
    void testOnMessageReceivedRefreshesAddExpense() {
        String message = "MESSAGE\ndestination:/topic/event\ncontent-type:text/plain\n\n{}\u0000";

        platform.when(() -> Platform.runLater(any(Runnable.class)))
                .thenAnswer(invocation -> {
                    Runnable runnable = invocation.getArgument(0);
                    runnable.run();
                    return null;
                });

        client.onMessageReceived(message);

        verify(mainCtrl, times(1)).refreshAddExpense();
    }

    @Test
    void testInitializeEditEvent() {
        client.initialize();
        client.onConnected();

        verify(service, times(1)).subscribe("/topic/person");
    }

    @Test
    void testInitializeStartScreen() {
        client.initialize();
        client.onConnected();

        verify(service, times(1)).subscribe("/topic/events");
    }

    @Test
    void testInitializeAddExpense() {
        client.initialize();
        client.onConnected();

        verify(service, times(1)).subscribe("/topic/event");
    }

    @Test
    void testClose() {
        client.initialize();
        client.onConnected();
        client.close();

        verify(service, times(1)).close();
    }
}
