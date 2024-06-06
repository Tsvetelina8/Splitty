package client.scenes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import client.utils.LanguageItem;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@ExtendWith(ApplicationExtension.class)
public class AddLanguageCtrlTest {

    private ServerUtils server;

    @Mock
    MainCtrl mainCtrl;

    @Mock
    UIAlertService uiAlertService;

    private TextField locale;
    private TableView<LanguageItem> languageTable;
    private Label languageTitle;
    private TableColumn<LanguageItem, String> languageLabel;
    private TableColumn<LanguageItem, String> languageDefault;
    private TableColumn<LanguageItem, String> languageTranslation;

    private AddLanguageCtrl sut;
    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {

        mocks = MockitoAnnotations.openMocks(this);
        Mockito.doNothing().when(mainCtrl).showStartScreen();

        server = new ServerUtils();
        
        locale = new TextField();
        languageTable = new TableView<>();
        languageTitle = new Label();
        languageLabel = new TableColumn<>();
        languageDefault = new TableColumn<>();
        languageTranslation = new TableColumn<>();

        Object[] params = {server, mainCtrl, locale, languageTable, languageTitle, languageLabel, languageDefault, languageTranslation, uiAlertService};

        sut = new AddLanguageCtrl(params);

    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void initializeTest() {
        sut.initialize();

        assertFalse(languageTable.getItems().isEmpty());
        assertEquals("", languageTable.getItems().get(0).getTranslation());
    }

    @Test
    public void fillTableTest() {
        sut.initialize();

        String localizationPath;
        try {
            localizationPath = getClass()
                    .getResource("/client/localization/Labels_en_US.properties")
                    .toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        Properties props = new Properties();
        try (InputStream input = new FileInputStream(localizationPath)) {
            props.load(input);

            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                LanguageItem item = new LanguageItem(key, value);

                assertTrue(languageTable.getItems().contains(item));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void invalidLocaleTest() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            sut.initialize();

            sut.submitButton();

            future.complete(null);
        });
        
        try {
            future.get();

            verify(uiAlertService).showAlert(eq(Alert.AlertType.ERROR), "Such locale does not exist!", anyString(), anyString());
        } catch (Exception e) {

        }
    }

    @Test
    public void incompleteTranslationsTest() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            sut.initialize();
            locale.setText("en_GB");

            sut.submitButton();

            future.complete(null);
        });

        try {
            future.get();

            verify(uiAlertService).showAlert(eq(Alert.AlertType.ERROR), "Incomplete Translations", anyString(), anyString());
        } catch (Exception e) {

        }
    }

    @Test
    public void submitButtonTest() {
        sut.initialize();

        locale.setText("en_GB");

        for (LanguageItem item : languageTable.getItems()) {
            item.setTranslation("aboba");
        }

        sut.submitButton();

        verify(mainCtrl, times(1)).showStartScreen();
    }
}
