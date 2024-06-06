package client.scenes;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AboutCtrlTest {

    @Mock
    private MainCtrl mc;
    private AboutCtrl sut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(mc).showStartScreen();
        sut = new AboutCtrl(mc);
    }

    @Test
    void showStartScreenTest() {
        sut.showStartScreen();
        verify(mc, times(1)).showStartScreen();
    }

    @Test
    void keyPressedTest() {
        sut.keyPressed(new KeyEvent(null, null, null,
                KeyCode.ESCAPE, false, false, false, false));
        verify(mc, times(1)).showStartScreen();
    }
}