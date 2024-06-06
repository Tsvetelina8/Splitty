package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Loan;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Builder;

@ExtendWith(ApplicationExtension.class)
public class SettleDebtsCtrlTest {
    @Mock
    private Client client;
    @Mock
    private WebTarget target;
    @Mock
    private Invocation.Builder builder;
    ServerUtils server;

    @Mock
    MainCtrl mainCtrl;

    private Menu languageSwitch;
    private Menu languageIndicator;
    private Button back;
    private Label eventName;
    private Event event;
    private ListView<Loan> debtsListView;

    private SettleDebtsCtrl sut;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        Mockito.doNothing().when(mainCtrl).showStartScreen();

        when(client.target(Mockito.anyString())).thenReturn(target);
        when(target.path(Mockito.anyString())).thenReturn(target);
        when(target.request(Mockito.anyString())).thenReturn(builder);
        when(target.request()).thenReturn(builder);
        when(builder.accept(Mockito.anyString())).thenReturn(builder);

        client = Mockito.mock(Client.class);
        server = Mockito.mock(ServerUtils.class);

        server.injectMockClient(client);

        event = new Event("Test Event");
        languageSwitch = new Menu();
        languageIndicator = new Menu();
        back = new Button();
        eventName = new Label();
        debtsListView = new ListView<>();

        Object[] params = {server, mainCtrl, event, languageSwitch, back, eventName, languageIndicator, debtsListView};

        sut = new SettleDebtsCtrl(params);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void setEventTest() {
        Event newEvent = new Event("New Event");
        Mockito.when(server.findEventById(Mockito.any(Long.class))).thenReturn(newEvent);

        sut.setEvent(newEvent);
        assertEquals("New Event", sut.getEvent().getTitle());
        assertEquals("New Event", sut.getEventName().getText());
    }

    @Test
    public void TestKeyPressedTestESC() {

        KeyEvent escapeEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        sut.keyPressed(escapeEvent);

        verify(mainCtrl).showEditEvent(sut.getEvent());
    }


    @Test
    public void TestBack() {

        sut.back();
        verify(mainCtrl).showEditEvent(sut.getEvent());
    }

    @Test
    void switchLanguageTest() {
        Builder builder = new Locale.Builder().setLanguage("en").setRegion("US");

        Locale locale = builder.build();
        List<Locale> locales = new ArrayList<>();
        locales.add(locale);

        doReturn(locales).when(mainCtrl).getLocales();
        sut.fillLanguageSwitch();
        assertEquals(1, languageIndicator.getItems().size());

        List<Loan> current = debtsListView.getItems();

        doNothing().when(mainCtrl).switchLanguage(any(Locale.class));

        languageIndicator.getItems().get(0).fire();
        verify(mainCtrl, times(1)).switchLanguage(any(Locale.class));
        assertEquals(current, debtsListView.getItems());
    }
}
