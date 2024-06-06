package client.scenes;

import client.utils.AppConfig;
import client.utils.PersonSelectionModel;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import commons.Event;
import commons.Expense;
import commons.Person;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class AddExpenseCtrlTest {

    private AddExpenseCtrl sut;

    @Mock
    private ServerUtils server;
    @Mock
    private MainCtrl mainCtrl;

    private Event event;
    private ChoiceBox<Person> payer;
    private DatePicker expenseDate;
    private TextField expenseName;
    @Mock
    private Spinner<Double> expenseValue;
    private ChoiceBox<String> expenseCurrency;
    private RadioButton equallyRadio;
    private RadioButton selectPeopleRadio;
    private ListView<PersonSelectionModel> selectPeopleListView;
    private TextField expenseType;
    private Menu languageSwitch;
    private Menu languageIndicator;
    private Button expenseAdd;
    private Button expenseAbort;
    private Label title;
    @Mock
    private UIAlertService alertService;
    private ResourceBundle resourceBundle;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(mainCtrl).showStartScreen();

        this.payer = new ChoiceBox<>();
        this.payer.setValue(new Person("1", "1"));
        this.expenseDate = new DatePicker();
        this.expenseDate.setValue(LocalDate.now());
        this.expenseName = new TextField();
        this.expenseName.setText("expense1");
        this.expenseCurrency = new ChoiceBox<>();
        this.equallyRadio = new RadioButton();
        this.selectPeopleRadio = new RadioButton();
        this.expenseType = new TextField();
        this.languageSwitch = new Menu();
        this.expenseAdd = new Button();
        this.expenseAbort = new Button();
        this.selectPeopleListView = new ListView<>();
        this.title = new Label();
        this.languageIndicator = new Menu();
        AppConfig appConfig = new AppConfig();
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", Locale.of(appConfig.getSelectedLocale()));
        event = new Event("a");
        event.addParticipant(new Person());

        when(expenseValue.getValue()).thenReturn(1.0);
        when(expenseValue.getValueFactory()).thenReturn(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0));

        Object[] params = {server, mainCtrl, payer, expenseDate, expenseName,
                expenseValue, expenseCurrency, equallyRadio, selectPeopleRadio,
                expenseType, languageSwitch, expenseAdd, expenseAbort, selectPeopleListView,
        title, alertService, resourceBundle, appConfig, languageIndicator};

        sut = new AddExpenseCtrl(params);

        when(server.findEventById(Mockito.anyLong())).thenReturn(new Event("title1"));
        when(mainCtrl.getLocales()).thenReturn(Collections.singletonList(Locale.ENGLISH));
        when(mainCtrl.getConfig()).thenReturn(appConfig);
    }

    @Test
    void setEventTest() {
        sut.setEvent(new Event("abc"), null);
        assertEquals("abc", sut.getEvent().getTitle());
    }

    @Test
    void refreshTest() {
        sut.setEvent(new Event("abc"), null);
        sut.refresh();
        assertEquals("title1", sut.getEvent().getTitle());
    }

    @Test
    void setEventWithParticipantsTest() {
        Event event = new Event("title2");
        event.addParticipant(new Person("John", "Cruelty"));
        event.addParticipant(new Person("2", "2" ));
        event.addParticipant(new Person("3", "3" ));

        sut.setEvent(event, null);

        assertEquals("title2", sut.getEvent().getTitle());
        assertEquals("John", sut.getEvent().getParticipants().get(0).firstName);
        assertEquals("Cruelty", sut.getEvent().getParticipants().get(0).lastName);
    }

    @Test
    void addTest() {
        sut.add();
        verify(server, times(1)).addExpense(any(), any());
        verify(mainCtrl, times(1)).showEditEvent(any());
    }

    @Test
    void initializeTest() {
        sut.initialize();
        assertEquals(expenseCurrency.getItems().get(0), "EUR");
    }

    @Test
    void addTestPayerNull() {
        payer.setValue(null);
        sut.add();
        verify(alertService, times(1)).showAlert(any(), anyString(), anyString(), anyString());
    }

    @Test
    void addTestExpenseNameBlank() {
        expenseName.setText("");
        sut.add();
        verify(alertService, times(1)).showAlert(any(), anyString(), anyString(), anyString());
    }

    @Test
    void addTestExpenseValueNull() {
        when(expenseValue.getValue()).thenReturn(null);
        when(expenseValue.getValueFactory()).thenReturn(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0));
        sut.add();
        verify(alertService, times(1)).showAlert(any(), anyString(), anyString(), anyString());
    }

    @Test
    void addTestExpenseValue0() {
        when(expenseValue.getValue()).thenReturn(0.0);
        when(expenseValue.getValueFactory()).thenReturn(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0));
        sut.add();
        verify(alertService, times(1)).showAlert(any(), anyString(), anyString(), anyString());
    }

    @Test
    void addTestIsPut() {
        selectPeopleRadio.setSelected(true);
        sut.setEvent(event, new Expense("a", "a", LocalDateTime.MAX, new Person(), 1));
        sut.add();
        verify(server, times(1)).editExpense(eq(event), any());
    }

    @Test
    void addTestIsNotPut() {
        equallyRadio.setSelected(true);
        sut.setEvent(event, null);
        expenseName.setText("a");
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.ENTER, false, false, false, false));
        verify(server, times(1)).addExpense(any(), any());
    }

    @Test
    void abortTest() {
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.ESCAPE, false, false, false, false));
        verify(mainCtrl, times(1)).showEditEvent(any());
    }

    @Test
    void contributeLanguageTest() {
        sut.contributeLanguage();
        verify(mainCtrl, times(1)).showAddLanguage();
    }

    @Test
    void aboutPageTest() {
        sut.aboutPage();
        verify(mainCtrl, times(1)).showAbout();
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

        expenseType.setText("Aboba");

        doNothing().when(mainCtrl).switchLanguage(any(Locale.class));

        languageIndicator.getItems().get(0).fire();
        verify(mainCtrl, times(1)).switchLanguage(any(Locale.class));
        assertEquals("Aboba", expenseType.getText());
    }
}