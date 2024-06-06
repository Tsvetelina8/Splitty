package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import server.services.PasswordService;

import static org.junit.jupiter.api.Assertions.*;

class PasswordControllerTest {

    @Mock
    private PasswordService passwordService;
    private PasswordController pc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(passwordService.generatePassword()).thenReturn("password");
        pc = new PasswordController(passwordService);
    }

    @Test
    void passwordVerificationTest() {
        assertEquals(pc.passwordVerification("password"), ResponseEntity.ok("Ok"));
    }

    @Test
    void passwordVerificationTestFalse() {
        assertEquals(pc.passwordVerification("joe"), ResponseEntity.badRequest().build());
    }
}