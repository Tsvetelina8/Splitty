package client.utils;

import commons.Event;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailUtilsTest {
    @Spy
    EmailUtils emailUtils;
    @Mock
    Session session;
    @Mock
    AppConfig config;
    @BeforeEach
    void setUp() throws MessagingException {
        MockitoAnnotations.openMocks(this);
        when(config.getUserEmail()).thenReturn("email@gmail.com");
        when(config.getUserPassword()).thenReturn("abc123");
        doNothing().when(emailUtils).send(Mockito.any());
    }

    @Test
    void getAddressesFromList() {
        List<String> recipients = List.of(new String[]{"abc@gmail.com", "def@gmail.com"});
        var addresses = emailUtils.getAddressesFromList(recipients);
        assertEquals(addresses.length, 2);
    }

    @Test
    void inviteEmailIsSent() throws MessagingException {
        List<String> recipients = List.of(new String[]{"abc@gmail.com", "def@gmail.com"});
        emailUtils.sendInvites(new Event("event"), recipients);
        verify(emailUtils).send(Mockito.any());
    }

    @Test
    void testEmailIsSent() throws MessagingException {
        emailUtils.sendTest();
        verify(emailUtils).send(Mockito.any());
    }
}