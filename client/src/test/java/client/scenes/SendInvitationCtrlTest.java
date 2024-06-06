package client.scenes;

import client.utils.EmailUtils;
import client.utils.ServerUtils;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import commons.Event;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Builder;

@ExtendWith(ApplicationExtension.class)
class SendInvitationCtrlTest {

    SendInvitationCtrl sut;
    Label inviteCodeLabel;
    Label eventNameLabel;
    Event event;
    @Mock
    EmailUtils emailUtils;
    @Mock
    TextArea emailInput;

    @Mock
    Menu languageSwitch;
    Menu languageIndicator;
    @Mock
    ServerUtils server;
    @Mock
    MainCtrl mainCtrl;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.doNothing().when(mainCtrl).showStartScreen();

        Object[] params = new Object[9];

        event = new Event("test");
        inviteCodeLabel = new Label("Words: {0}");
        eventNameLabel = new Label("abc");
        languageIndicator = new Menu();

        params[0] = mainCtrl;
        params[1] = server;
        params[2] = event;
        params[3] = inviteCodeLabel;
        params[4] = eventNameLabel;
        params[5] = languageSwitch;
        params[6] = emailInput;
        params[7] = emailUtils;
        params[8] = languageIndicator;

        sut = new SendInvitationCtrl(params);
    }

    @Test
    public void setEventChangesLabels() {
        Event e = new Event("changed");
        e.setInviteCode();
        sut.setEvent(e);

        assertEquals(eventNameLabel.getText(), e.getTitle());
        assertEquals(sut.event, e);
        assertTrue(inviteCodeLabel.getText().contains(e.getInviteCode()));
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

        doNothing().when(mainCtrl).switchLanguage(any(Locale.class));

        languageIndicator.getItems().get(0).fire();
        verify(mainCtrl, times(1)).switchLanguage(any(Locale.class));
    }
}