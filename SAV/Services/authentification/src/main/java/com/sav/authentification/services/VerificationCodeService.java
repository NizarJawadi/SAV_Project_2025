package com.sav.authentification.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeService {
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> codeExpiry = new ConcurrentHashMap<>();

    public void saveCode(String email, String code) {
        verificationCodes.put(email, code);
        codeExpiry.put(email, LocalDateTime.now().plusMinutes(15)); // Augmentez à 15 minutes
        System.out.println("Code saved for " + email + ": " + code); // Debug
    }

    public boolean verifyCode(String email, String code) {
        String savedCode = verificationCodes.get(email);
        LocalDateTime expiryTime = codeExpiry.get(email);

        System.out.println("Verifying code for " + email); // Debug
        System.out.println("Saved code: " + savedCode); // Debug
        System.out.println("Input code: " + code); // Debug
        System.out.println("Expiry time: " + expiryTime); // Debug

        if (savedCode == null || expiryTime == null) {
            System.out.println("No code found or expired"); // Debug
            return false;
        }

        if (LocalDateTime.now().isAfter(expiryTime)) {
            System.out.println("Code expired"); // Debug
            removeCode(email);
            return false;
        }

        boolean isValid = savedCode.equals(code);
        System.out.println("Code valid: " + isValid); // Debug
        return isValid;
    }

    public void removeCode(String email) {
        verificationCodes.remove(email);
        codeExpiry.remove(email);
        System.out.println("Code removed for " + email); // Debug
    }
}