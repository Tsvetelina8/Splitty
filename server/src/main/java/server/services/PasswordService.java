package server.services;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordService {

    /**
     * Generates a password for the admin login
     * @return The password that is generated
     */
    public String generatePassword() {
        Random random = new Random();
        final String possibleCharacters = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";

        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int choice = random.nextInt(0, possibleCharacters.length());
            passwordBuilder.append(possibleCharacters.charAt(choice));
        }

        return passwordBuilder.toString();
    }
}
