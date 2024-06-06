package server.services;

import org.springframework.stereotype.Service;

@Service
public class EntityService {
    /**
     * Checks if a string is null or empty
     * @param s The string to check
     * @return If the string is null or empty
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
