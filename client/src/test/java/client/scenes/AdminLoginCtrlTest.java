package client.scenes;

import client.utils.ServerUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class AdminLoginCtrlTest {

    private AdminLoginCtrl sut;
    @Mock
    private ServerUtils server;
    @Mock
    private MainCtrl mc;
    @Mock
    private Alert alert;
    @Mock
    private PasswordField passwordField;

    private Menu languageIndicator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mc.getLocales()).thenReturn(null);
        doNothing().when(mc).switchLanguage(any(Locale.class));
        doNothing().when(mc).showAdminOverview();
        when(alert.showAndWait()).thenReturn(null);
        when(passwordField.getText()).thenReturn("password");

        languageIndicator = new Menu();

        sut = new AdminLoginCtrl(server, mc, alert, passwordField, languageIndicator);
    }

    @Test
    void initializeTest() {
        sut.initialize(null, null);
        verifyNoMoreInteractions(server);
    }

    @Test
    void loginAdminTest() {
        when(server.verifyPassword(anyString())).thenReturn("Ok");
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.ENTER, false, false, false, false));
        verify(mc, times(1)).showAdminOverview();
    }

    @Test
    void loginAdminIncorrectPasswordTest() {
        when(server.verifyPassword(anyString())).thenReturn("Bad Request");
        sut.loginAdmin();
        verify(mc, never()).showAdminOverview();
        verify(alert, times(1)).showAndWait();
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
    void switchLanguageTest() {
        Builder builder = new Locale.Builder().setLanguage("en").setRegion("US");

        Locale locale = builder.build();
        List<Locale> locales = new ArrayList<>();
        locales.add(locale);

        doReturn(locales).when(mc).getLocales();
        sut.fillLanguageSwitch();
        assertEquals(1, languageIndicator.getItems().size());

        passwordField.setText("Aboba");

        doNothing().when(mc).switchLanguage(any(Locale.class));

        languageIndicator.getItems().get(0).fire();
        verify(mc, times(1)).switchLanguage(any(Locale.class));
        assertEquals("password", passwordField.getText());
    }
}