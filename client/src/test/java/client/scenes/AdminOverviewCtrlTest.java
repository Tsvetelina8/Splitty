package client.scenes;

import client.utils.AppConfig;
import client.utils.JsonController;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import commons.Event;
import jakarta.ws.rs.core.Response;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Locale.Builder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class AdminOverviewCtrlTest {

    @Mock
    private ServerUtils server;
    private AdminOverviewCtrl sut;
    private TableView<Event> eventList;
    private TableColumn<Event, String> eventTitle;
    private TableColumn<Event, LocalDateTime> eventCreationDate;
    private TableColumn<Event, LocalDateTime> eventLastActivity;
    @Mock
    private JsonController jsonController;
    private Event selectedEvent;
    @Mock
    private MainCtrl mc;
    @Mock
    private UIAlertService alertService;
    private ResourceBundle resourceBundle;

    private Menu languageIndicator;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        doNothing().when(mc).showStartScreen();
        doNothing().when(mc).showAddLanguage();
        doNothing().when(mc).showAbout();
        doNothing().when(jsonController).exportEvent(any(Event.class));
        when(mc.getLocales()).thenReturn(Collections.singletonList(Locale.ENGLISH));


        eventList = new TableView<>();
        eventTitle = new TableColumn<>();
        eventCreationDate = new TableColumn<>();
        eventLastActivity = new TableColumn<>();
        selectedEvent = new Event("testEvent");
        selectedEvent.id = 10;
        languageIndicator = new Menu();

        AppConfig appConfig = new AppConfig();
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", Locale.of(appConfig.getSelectedLocale()));

        when(mc.getConfig()).thenReturn(appConfig);

        Object[] params = {server, mc, eventList, eventTitle,
                eventCreationDate, eventLastActivity, jsonController, selectedEvent,
                alertService, true, resourceBundle, appConfig, languageIndicator};
        sut = new AdminOverviewCtrl(params);
        spy(sut);
    }

    @Test
    void initializeTest() {
        sut.initialize(null, null);
        assertEquals(eventTitle.getId(), "title");
        assertEquals(eventCreationDate.getId(), "creationDate");
        assertEquals(eventLastActivity.getId(), "lastActivity");
    }

    @Test
    void initializeListenerTest() {
        sut.initialize(null, null);
        Event newSelect = new Event();
        eventList.getSelectionModel().select(newSelect);
        assertEquals(sut.getSelectedEvent(), newSelect);
    }

    @Test
    void refreshEventsTest() {
        when(server.getAllEvents()).thenReturn(Collections.singletonList(new Event()));
        sut.refreshEvents();
        List<Event> eventListItems1 = Collections.singletonList(new Event());
        List<Event> eventListItems2 = sut.getEventList().getItems();
        assertEquals(eventListItems1, eventListItems2);
    }

    @Test
    void deleteEventTest() {
        sut.getEventList().getItems().add(selectedEvent);
        assertTrue(sut.getEventList().getItems().contains(selectedEvent));
        when(server.deleteEventById(anyLong())).thenReturn(204);
        when(alertService.showAlert(any(), anyString(), anyString(), anyString())).
                thenReturn(Optional.of(ButtonType.OK));
        sut.getEventList().getSelectionModel().select(selectedEvent);
        sut.deleteEvent();
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.DELETE, false, false, false, false));
        assertFalse(sut.getEventList().getItems().contains(selectedEvent));
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.D, false, false, false, false));
        assertFalse(sut.getEventList().getItems().contains(selectedEvent));
    }

    @Test
    void importEventTest(){
        Response response = mock(Response.class);
        when(server.importEvent(any(Event.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(200);
        when(response.readEntity(eq(Event.class))).thenReturn(new Event("TestEvent"));
        when(jsonController.importEvent()).thenReturn(new Event("TestEvent"));
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.I, false, false, false, false));
        assertTrue(sut.getEventList().getItems().stream().map(Event::getTitle).toList().contains("TestEvent"));;
    }

    @Test
    void importEventTestAlreadyExists() throws IOException {
        Response response = mock(Response.class);
        when(server.importEvent(any(Event.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(200);
        when(response.readEntity(eq(Event.class))).thenReturn(new Event("TestEvent"));
        when(jsonController.importEvent()).thenReturn(new Event("TestEvent"));
        sut.importEvent();

        when(response.getStatus()).thenReturn(400);
        sut.importEvent();
        verify(alertService, times(1)).showAlert(any(), anyString(), anyString(), anyString());
    }

    @Test
    void importEventTestNull() throws IOException {
        when(jsonController.importEvent()).thenReturn(null);
        sut.importEvent();
        verify(alertService, never()).showAlert(any(), anyString(), anyString(), anyString());
    }

    @Test
    void exportEventTest() throws IOException {
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.E, false, false, false, false));
        verify(jsonController, times(1)).exportEvent(any(Event.class));
    }

    @Test
    void showStartScreenTest() {
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.ESCAPE, false, false, false, false));
        verify(mc, times(1)).showStartScreen();
    }

    @Test
    void contributeLanguageTest() {
        sut.contributeLanguage();
        verify(mc, times(1)).showAddLanguage();
    }

    @Test
    void aboutPageTest() {
        sut.aboutPage();
        verify(mc, times(1)).showAbout();
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
    void refreshFullTest() {
        when(server.getAllEvents()).thenReturn(Collections.singletonList(selectedEvent));
        sut.refreshEventsFull();
        assertEquals(eventList.getItems(), Collections.singletonList(selectedEvent));
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

        doNothing().when(mc).switchLanguage(any(Locale.class));

        languageIndicator.getItems().get(0).fire();
        verify(mc, times(1)).switchLanguage(any(Locale.class));
    }
}