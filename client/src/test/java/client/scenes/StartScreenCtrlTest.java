package client.scenes;

import client.utils.ServerUtils;
import client.utils.UIAlertService;
import commons.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Builder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class StartScreenCtrlTest {

    private StartScreenCtrl sut;

    private TextField newEvent;

    private TextField joinEvent;
    private Event selectedEvent;
    @Spy
    private ListView<Event> eventListTitles;
    @Mock
    private ServerUtils server;
    @Mock
    private MainCtrl mc;
    @Mock
    private UIAlertService alertService;

    private Menu languageIndicator;

    /**
     * Method for setup  before every test
     * @throws Exception exception
     */
    @BeforeEach
    public void setUp()  {
        MockitoAnnotations.openMocks(this);
        doNothing().when(mc).showStartScreen();
        doNothing().when(mc).showEditEvent(any(Event.class));

        newEvent = new TextField("New Event");
        joinEvent = new TextField("Join Event Code");
        selectedEvent = new Event("Event1");

        when(server.createEvent(anyString())).thenReturn(selectedEvent);

        Event response = new Event(joinEvent.getText());
        response.id = 2;
        when(server.findEventByCode(anyString())).thenReturn(response);

        List<Event> eventList = new ArrayList<>();
        eventList.add(selectedEvent);

        languageIndicator = new Menu();

        Object[] params = {server, mc, eventListTitles, newEvent,
                joinEvent, selectedEvent, alertService, languageIndicator};

        sut = new StartScreenCtrl(params);
        sut.setEvents(eventList);
        when(server.getAllEvents()).thenReturn(eventList);
    }

    @Test
    public void addEventTest() {
        sut.addEvent();
        List<Event> events = sut.getEventListTitles().getItems();
        assertNotNull(events);
        assertTrue(events.contains(selectedEvent));
    }

    @Test
    public void addDuplicateEvent() {
        assertTrue(sut.getEventListTitles().getItems().contains(selectedEvent));
        when(server.createEvent(anyString())).thenThrow(new IllegalArgumentException());
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.ENTER, false, false, false, false));
        sut.deleteEvent();
        List<Event> events = sut.getEventListTitles().getItems();
        assertFalse(events.contains(selectedEvent));
    }

    @Test
    public void joinEventTest() {
        assertEquals(1, sut.getEventListTitles().getItems().size());
        sut.joinEvent();
        List<Event> events = sut.getEventListTitles().getItems();
        assertEquals(2, events.size());
    }

    @Test
    public void joinEventTestInvalid() {
        assertEquals(1, sut.getEventListTitles().getItems().size());
        when(server.findEventByCode(anyString())).thenThrow(new IllegalArgumentException());
        sut.joinEvent();
        verify(alertService, times(1)).
                showAlert(any(Alert.AlertType.class), anyString(), anyString(), anyString());
        List<Event> events = sut.getEventListTitles().getItems();
        assertEquals(1, events.size());
    }

    @Test
    void initializeListenerTest() {
        sut.initialize(null, null);
        assertEquals(sut.getSelectedEvent(), selectedEvent);
    }

    @Test
    public void initializeTest() {
        sut.initialize(null, null);
        assertEquals("", sut.getNewEvent().getText());
        assertEquals("", sut.getJoinEvent().getText());
    }

    @Test
    public void deleteEventTest() {
        sut.setEvents(Collections.singletonList(selectedEvent));
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.CONTROL, false, false, false, false));
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.D, false, false, false, false));
        List<Event> events = sut.getEventListTitles().getItems();
        assertFalse(events.contains(selectedEvent));
    }

    @Test
    public void editEventTest() {
        sut.editEvent();
        verify(mc, times(1)).showEditEvent(any());
    }

    @Test
    void aboutPageTest() {
        sut.aboutPage();
        verify(mc, times(1)).showAbout();
    }

    @Test
    void loginAdminTest() {
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.CONTROL, false, false, false, false));
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.L, false, false, false, false));
        verify(mc, times(1)).showAdminLogin();
    }

    @Test
    void contributeLanguageTest() {
        sut.contributeLanguage();
        verify(mc, times(1)).showAddLanguage();
    }

    @Test
    void selectedItemTest() {
        Event e = new Event("eee");
        sut.getEventListTitles().getSelectionModel().select(e);
        assertEquals(sut.getSelectedEvent(), e);
    }

    @Test
    void ctrlPressTest() {
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.CONTROL, false, false, false, false));
        assertTrue(sut.getCtrlPressed());
    }

    @Test
    void ctrlPressTestFalse() {
        sut.keyReleased(new KeyEvent(null, null, null,
                KeyCode.CONTROL, false, false, false, false));
        assertFalse(sut.getCtrlPressed());
    }

    @Test
    void switchLanguageTest() {
        Builder builder = new Locale.Builder().setLanguage("en").setRegion("US");

        Locale locale = builder.build();
        List<Locale> locales = new ArrayList<>();
        locales.add(locale);

        doReturn(locales).when(mc).getLocales();
        sut.fillLanguageSwitch();
        assertEquals(1, languageIndicator.getItems().size());

        joinEvent.setText("Aboba");

        doNothing().when(mc).switchLanguage(any(Locale.class));

        languageIndicator.getItems().get(0).fire();
        verify(mc, times(1)).switchLanguage(any(Locale.class));
        assertEquals("Aboba", joinEvent.getText());
    }

}
