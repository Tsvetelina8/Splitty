package server.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    @Test
    void generatePassword() {
        PasswordService passwordService = new PasswordService();
        String password = passwordService.generatePassword();
        assertNotNull(password);
        assertEquals(6, password.length());
    }
}