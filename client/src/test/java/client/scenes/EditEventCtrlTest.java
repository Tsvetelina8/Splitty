package client.scenes;

import client.utils.AppConfig;
import client.utils.EmailUtils;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import commons.Event;
import commons.Expense;
import commons.Person;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Locale.Builder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class EditEventCtrlTest {
    @Mock
    private MainCtrl mc;
    private Event event;
    private ListView<Person> participants;
    @Mock
    private ServerUtils server;
    private EditEventCtrl sut;
    private Person person;
    private Label inviteCodeLabel;
    private Label eventName;
    private ListView<Expense> eventExpensesList;
    private ListView<Expense> eventExpensesListIncluding;
    private ListView<Expense> eventExpensesListFrom;
    private ResourceBundle resourceBundle;
    private Menu languageSwitch;
    private Menu languageIndicator;
    private Button addNewParticipant;
    private Button eventAddExpense;
    private Button eventSettleDebts;
    private Button eventSendInvites;
    private Button editTitle;
    private Button backButton;
    private Tab eventExpensesFromX;
    private Tab eventExpensesIncludingX;
    private Label totalExpenses;
    private Label paidLabel;
    private Label owesLabel;
    @Mock
    private UIAlertService alertService;
    @Mock
    private EmailUtils emailUtils;
    @Mock
    private Tooltip copyTip;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        doNothing().when(mc).showStartScreen();
        when(copyTip.getText()).thenReturn("Jeff");

        event = new Event("Test Event");
        participants = new ListView<>();
        person = new Person("Test", "Person");
        event.addParticipant(person);
        AppConfig appConfig = new AppConfig();
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", Locale.of(appConfig.getSelectedLocale()));

        inviteCodeLabel = new Label();
        eventName = new Label();
        eventExpensesList = new ListView<>();
        eventExpensesListIncluding = new ListView<>();
        eventExpensesListFrom = new ListView<>();
        languageSwitch = new Menu("Language");
        addNewParticipant = new Button("Add New Participant");
        eventAddExpense = new Button("Add Event Expenses");
        eventSettleDebts = new Button("Settle Debts");
        eventSendInvites = new Button("Send Invites");
        editTitle = new Button("Edit Event");
        backButton = new Button("Back");
        eventExpensesFromX = new Tab("Event Expenses");
        eventExpensesIncludingX = new Tab("Event Expenses Including");
        totalExpenses = new Label();
        paidLabel = new Label();
        owesLabel = new Label();
        languageIndicator = new Menu();

        when(server.findEventById(anyLong())).thenReturn(event);
        when(mc.getLocales()).thenReturn(Collections.singletonList(Locale.ENGLISH));
        when(mc.getConfig()).thenReturn(appConfig);
        when(emailUtils.isEmailSetUp()).thenReturn(false);

        List<Object> params = new ArrayList<>();
        params.add(server);
        params.add(mc);
        params.add(event);
        params.add(resourceBundle);
        params.add(participants);
        params.add(inviteCodeLabel);
        params.add(eventName);
        params.add(eventExpensesList);
        params.add(eventExpensesListIncluding);
        params.add(eventExpensesListFrom);
        params.add(languageSwitch);
        params.add(addNewParticipant);
        params.add(eventAddExpense);
        params.add(eventSettleDebts);
        params.add(eventSendInvites);
        params.add(editTitle);
        params.add(backButton);
        params.add(eventExpensesFromX);
        params.add(eventExpensesIncludingX);
        params.add(copyTip);
        params.add(totalExpenses);
        params.add(paidLabel);
        params.add(owesLabel);
        params.add(alertService);
        params.add(appConfig);
        params.add(languageIndicator);
        params.add(emailUtils);

        sut = new EditEventCtrl(params);
    }

    @Test
    public void deleteParticipantTest() {
        when(server.findEventById(anyLong())).thenReturn(new Event("Test Event"));

        participants.getSelectionModel().select(person);
        sut.deleteParticipants(person);

        ObservableList<Person> items1 = participants.getItems();
        ObservableList<Person> items2 = sut.getParticipants().getItems();

        assertFalse(items1.contains(person));

        assertEquals(items1.size(), items2.size());
        assertTrue(items1.containsAll(items2));
        assertTrue(items2.containsAll(items1));
    }

    @Test
    void deleteParticipantTestInvalid() {
        event.addExpense(new Expense("a", "a", LocalDateTime.MAX, person, 1));
        sut.setEvent(event);
        sut.deleteParticipants(person);
        verify(alertService, times(1)).showAlert(any(), anyString(), anyString(), anyString());
    }

    @Test
    public void setEventTest() {
        Event newEvent = new Event("New Event");
        when(server.findEventById(any(Long.class))).thenReturn(newEvent);

        sut.setEvent(newEvent);
        assertEquals("New Event", sut.getEvent().getTitle());
        assertEquals("New Event", sut.getEventName().getText());
    }

    @Test
    public void addParticipantTest() {
        sut.addNewParticipant();
        verify(mc, times(1)).showAddPerson(any(),  eq(null));
    }

    @Test
    public void settleDebtsTest() {
        sut.settleDebts();
        verify(mc, times(1)).showSettleDebts(any());
    }

    @Test
    public void showStartScreenTest() {
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.ESCAPE, false, false, false, false));
        verify(mc, times(1)).showStartScreen();
    }

    @Test
    public void showAddExpense() {
        sut.addExpense();
        verify(mc, times(1)).showAddExpense(any(), any());
    }

    @Test
    void initializeTest() {
        sut.initialize();
        sut.addExpenseParticipant(person);
        participants.getSelectionModel().select(person);
        assertEquals(person, sut.getSelectedPerson());
    }

    @Test
    void initializeTestLongName() {
        sut.initialize();
        Person p = new Person("1234567890", "1234567890123");
        sut.addExpenseParticipant(p);
        participants.getSelectionModel().select(p);
        assertEquals(p, sut.getSelectedPerson());
        assertEquals(owesLabel.getText(), String.format(resourceBundle.getString("edit-event-owes"),
                "1234567890 123456789...", 0.0));
    }



    @Test
    void sendInvitesTest() {
        sut.sendInvites();
        verify(mc, times(1)).showSendInvitation(any());
    }

    @Test
    void deleteExpense() {
        sut.deleteExpense(new Expense());
        verify(server, times(1)).deleteExpense(any(), any());
    }

    @Test
    void addExpenseTest() {
        sut.addExpense();
        verify(mc, times(1)).showAddExpense(any(), eq(null));
    }

    @Test
    void editParticipantTest() {
        sut.editParticipant(new Person());
        verify(mc, times(1)).showAddPerson(any(), eq(new Person()));
    }

    @Test
    void contributeLanguageTest() {
        sut.contributeLanguage();
        verify(mc, times(1)).showAddLanguage();
    }

    @Test
    void toStringTest() {
        assertEquals(sut.toString(),
                "Event: <" + event.toString() + ">" +
                        "Event Participants: " +  event.getParticipants().toString() + "\n");
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

        List<Person> currentItems = participants.getItems();

        doNothing().when(mc).switchLanguage(any(Locale.class));

        languageIndicator.getItems().get(0).fire();
        verify(mc, times(1)).switchLanguage(any(Locale.class));
        assertEquals(currentItems, participants.getItems());
    }

    @Test
    void eventDeleteAlertTest(){
        Stage mockStage = mock(Stage.class);
        when(server.findEventById(anyLong())).thenThrow(new IllegalArgumentException());
        when(mc.getPrimaryStage()).thenReturn(mockStage);
        when(mockStage.getTitle()).thenReturn("Edit Event");
        sut.refresh();
        verify(alertService, times(1)).showAlert(any(), anyString(), anyString(), anyString());
        verify(mc, times(1)).showStartScreen();
    }
}
