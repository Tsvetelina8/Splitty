package server.api;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.PasswordService;

@RestController
@RequestMapping("/api/password/")
public class PasswordController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PasswordController.class);

    private String password;
    private PasswordService passwordService;

    /**
     * Constructor, calls the generatePassword
     * @param passwordService The passwordservice to inject
     */
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
        password = passwordService.generatePassword();
        log.info("Admin password is: {}", password);
    }

    /**
     * Post mapping for /api/password to verify the password
     *
     * @param input the inputted password
     * @return response body with if the password was correct
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<String> passwordVerification(@RequestBody String input) {
        if (!input.equals(password)) {
            return ResponseEntity.badRequest().build();
        }
        log.info("Login to admin successful");
        return ResponseEntity.ok("Ok");
    }
}