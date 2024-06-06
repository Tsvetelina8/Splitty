package client.scenes;

import client.utils.ServerUtils;
import client.utils.UIAlertService;
import commons.Event;
import commons.Person;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import javafx.scene.control.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.ResourceBundle;

@ExtendWith(ApplicationExtension.class)
public class AddPersonCtrlTest {
    @Mock
    ServerUtils server;

    @Mock
    MainCtrl mainCtrl;

    private TextField firstName;
    private TextField lastName;
    private Menu languageSwitch;
    private Menu languageIndicator;
    private Button participantAdd;
    private Button participantCancel;
    private Label title;
    @Mock
    private UIAlertService uiAlertService;
    private ResourceBundle resourceBundle;

    private AddPersonCtrl sut;
    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        Mockito.doNothing().when(mainCtrl).showStartScreen();

        firstName = new TextField();
        lastName = new TextField();
        languageSwitch = new Menu();
        participantAdd = new Button();
        participantCancel = new Button();
        title = new Label();
        languageIndicator = new Menu();
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", Locale.US);

        Object[] params = {server, mainCtrl, firstName, lastName, languageSwitch, participantAdd, participantAdd,
            title, languageIndicator, uiAlertService, resourceBundle};

        sut = new AddPersonCtrl(params);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void initializeTest() {
        when(mainCtrl.getSelectedLocale()).thenReturn(Locale.US);
        sut.initialize();

        assertEquals("", firstName.getText());
        assertEquals("", lastName.getText());
    }

    @Test
    public void testAddValidInput() {
        Event event = new Event("Test");
        sut.setEvent(event, null);
        firstName.setText("John");
        lastName.setText("Doe");

        sut.add();

        verify(server).addPerson(any(Event.class), any(Person.class));
        verify(mainCtrl).showEditEvent(event);
    }

    @Test
    public void TestKeyPressedENTER() {
        KeyEvent enterEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false);
        Event event = new Event("Test");
        sut.setEvent(event, null);
        firstName.setText("John");
        lastName.setText("Doe");

        sut.keyPressed(enterEvent);

        verify(server).addPerson(any(Event.class), any(Person.class));
    }
    @Test
    public void TestKeyPressedTestESC() {
        KeyEvent escapeEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        sut.keyPressed(escapeEvent);

        verify(mainCtrl).showEditEvent(sut.getEvent());
    }


    @Test
    public void TestCancel() {
        Event event = new Event("Test");
        sut.setEvent(event, null);
        sut.cancel();
        verify(mainCtrl).showEditEvent(event);
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
        
        lastName.setText("Aboba");

        doNothing().when(mainCtrl).switchLanguage(any(Locale.class));

        languageIndicator.getItems().get(0).fire();
        verify(mainCtrl, times(1)).switchLanguage(any(Locale.class));
        assertEquals("Aboba", lastName.getText());
    }

    @Test
    void showCreateError() {
        sut.showCreateError();
        verify(uiAlertService).showAlert(any(), anyString(), anyString(), anyString());
    }

    @Test
    void personDeleteAlert() {
        sut.personDeleteAlert();
        verify(uiAlertService).showAlert(any(), anyString(), anyString(), anyString());
    }
}
