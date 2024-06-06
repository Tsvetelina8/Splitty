package client.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    private AppConfig appConfig;
    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
    }

    @Test
    void getSelectedLocaleTest() {
        assertEquals(appConfig.getSelectedLocale(), "en_GB");
    }

    @Test
    void getAvailableLocalesTest() {
        assertTrue(Arrays.asList(appConfig.getAvailableLocales()).contains("en_US"));
    }

    @Test
    void getServerUrlTest() {
        assertEquals(appConfig.getServerUrl(), "http://localhost:8080/");
    }

    @Test
    void getWebSocketUrlTest() {
        assertEquals(appConfig.getWebSocketUrl(), "ws://localhost:8080/ws");
    }

    @Test
    void getUserEmailTest() {
        assertEquals(appConfig.getUserEmail(), "person@gmail.com");
    }

    @Test
    void getUserPasswordTest() {
        assertEquals(appConfig.getUserPassword(), "appPassword123");
    }

    @Test
    void saveSelectedLocaleTest() {
        appConfig.saveSelectedLocale(Locale.UK);
        assertEquals(appConfig.getSelectedLocale(), "en_GB");
    }

    @Test
    void addAvailableLocaleTest() {
        appConfig.addAvailableLocale(Locale.ITALIAN.toString());
        assertTrue(Arrays.asList(appConfig.getAvailableLocales()).contains(Locale.ITALIAN.toString()));
    }
}