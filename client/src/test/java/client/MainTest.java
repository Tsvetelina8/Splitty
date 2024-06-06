package client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import com.google.inject.Injector;

import client.scenes.*;
import client.utils.AppConfig;
import client.utils.LanguageItem;
import client.utils.ServerUtils;
import client.utils.UIAlertService;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

@ExtendWith(ApplicationExtension.class)
public class MainTest {

    @Mock
    private MyFXML mockFXML;

    @Mock
    MainCtrl mainCtrl;

    @Mock
    UIAlertService uiAlertService;

    @Mock
    AppConfig mockConfig;

    @Mock
    private Injector mockInjector;

    @InjectMocks
    private Main sut;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() throws Exception {

        mocks = MockitoAnnotations.openMocks(this);
        Mockito.doReturn("en_US").when(mockConfig).getSelectedLocale();

        String[] toReturn = {"en_US"};
        Mockito.doReturn(toReturn).when(mockConfig).getAvailableLocales();

        sut = new Main(mockInjector, mockFXML);
        sut.setConfig(mockConfig);

        Mockito.when(mockFXML.load(Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
               .thenReturn(new Pair<>(null, null));
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void getAvailableLocalesTest() {
        List<Locale> expected = new ArrayList<>();
        Builder builder = new Locale.Builder().setLanguage("en").setRegion("US");

        expected.add(builder.build());

        assertEquals(expected, sut.getAvailableLocales());
    }

    @Test
    public void getSelectedLocaleTest() {
        Builder builder = new Locale.Builder().setLanguage("en").setRegion("US");

        Locale expectedLocale = builder.build();

        assertEquals(expectedLocale, sut.getSelectedLocate());
    }

    @Test
    public void configTest() {
        sut.setConfig(mockConfig);
        assertNotNull(sut.getConfig());
    }

    @Test
    public void getLocalizedParentsTest() {
        Builder builder = new Locale.Builder().setLanguage("en").setRegion("US");

        Locale locale = builder.build();

        Parent[] parents = sut.getLocalizedParents(locale);

        assertEquals(11, parents.length);
    }
    
}
