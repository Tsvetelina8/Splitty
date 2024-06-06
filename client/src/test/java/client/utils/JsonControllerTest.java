package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonControllerTest {

    @Spy
    private ObjectMapper om;
    @Mock
    private FileChooser fc;
    @Mock
    private UIAlertService alertService;
    private JsonController sut;
    private ResourceBundle resourceBundle;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        ObservableList<FileChooser.ExtensionFilter> list = FXCollections.observableArrayList();
        when(fc.getExtensionFilters()).thenReturn(list);
        when(alertService.showAlert(any(), anyString(), anyString(),
                anyString())).thenReturn(null);

        AppConfig appConfig = new AppConfig();
        resourceBundle = ResourceBundle.getBundle(
                "client.localization.Labels", Locale.of(appConfig.getSelectedLocale()));

        sut = new JsonController(om, fc, alertService, resourceBundle);
    }

    @Test
    void exportEventTest() throws IOException, URISyntaxException {
        File saveFile = new File(Objects.requireNonNull(
                getClass().getClassLoader().getResource("ExportEvent.json")).toURI());
        when(fc.showSaveDialog(null)).thenReturn(saveFile);
        sut.exportEvent(new Event());
        verify(om, times(1)).writeValue(eq(saveFile), any(Event.class));
    }

    @Test
    void exportEventTestNull() throws IOException {
        when(fc.showSaveDialog(null)).thenReturn(null);
        sut.exportEvent(new Event());
        verify(om, never()).writeValue(any(File.class), any(Event.class));
    }

    @Test
    void importEventTest() throws IOException, URISyntaxException {
        File inputFile = new File(Objects.requireNonNull(
                getClass().getClassLoader().getResource("TestEvent.json")).toURI());
        Mockito.when(fc.showOpenDialog(null)).thenReturn(inputFile);
        sut.importEvent();
        verify(om, times(1)).readValue(eq(inputFile), eq(Event.class));
    }

    @Test
    void importEventTestInvalid() throws IOException, URISyntaxException {
        File inputFile = new File(Objects.requireNonNull(
                getClass().getClassLoader().getResource("TestEventInvalid.json")).toURI());
        Mockito.when(fc.showOpenDialog(null)).thenReturn(inputFile);
        assertNull(sut.importEvent());
        verify(om, times(1)).readValue(eq(inputFile), eq(Event.class));
        verify(alertService, times(1)).showAlert(any(), anyString(), anyString(),
                anyString());
    }

    @Test
    void importEventTestNull() throws IOException {
        Mockito.when(fc.showOpenDialog(null)).thenReturn(null);
        assertNull(sut.importEvent());
        verify(om, never()).readValue(any(File.class), eq(Event.class));
        verify(alertService, never()).showAlert(any(), anyString(), anyString(),
                anyString());
    }
}