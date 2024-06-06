package client.scenes;

import client.utils.ServerUtils;
import client.utils.UIAlertService;
import commons.Event;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class EditTitleCtrlTest {

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private UIAlertService uiAlertService;

    private EditTitleCtrl sut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mainCtrl.getSelectedLocale()).thenReturn(Locale.US);
        TextField tf = new TextField("title");
        sut = new EditTitleCtrl(server, mainCtrl, tf, uiAlertService);
    }

    @Test
    void setEvent() {
        sut.initialize(null, null);
        Event e = new Event("a");
        sut.setEvent(e);
        assertEquals(e, sut.getEvent());
    }

    @Test
    void setTitle() {
        sut.setEvent(new Event());
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.ENTER, false, false, false, false));
        verify(server, times(1)).updateTitle(anyString(), anyLong());
        assertEquals("title", sut.getEvent().getTitle());
    }

    @Test
    void eventDeleteAlert() {
        doThrow(IllegalArgumentException.class).when(server).updateTitle(anyString(), anyLong());
        sut.setTitle();
        verify(mainCtrl, times(1)).showStartScreen();
    }

    @Test
    void setId() {
        sut.setId(1);
        assertEquals(1, sut.getId());
    }

    @Test
    void showEditEventTest() {
        sut.setEvent(new Event());
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.ESCAPE, false, false, false, false));
        verify(mainCtrl, times(1)).showEditEvent(any());
    }
}