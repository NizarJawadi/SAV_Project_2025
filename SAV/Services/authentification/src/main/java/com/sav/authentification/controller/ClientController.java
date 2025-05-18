package com.sav.authentification.controller;

import com.sav.authentification.feign.MailSenderClient;
import com.sav.authentification.model.Client;
import com.sav.authentification.model.Roles;
import com.sav.authentification.services.ClientServicesImpl;
import com.sav.authentification.services.VerificationCodeService;
import com.sav.authentification.utils.MailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientServicesImpl clientService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private MailSenderClient mailSenderClient;





    @PostMapping("/request-password-reset")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (!clientService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email non trouvé"));
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        verificationCodeService.saveCode(email, code);

        MailRequest mailRequest = new MailRequest();
        mailRequest.setTo(email);
        mailRequest.setSubject("Code de réinitialisation de mot de passe");
        mailRequest.setBody("Voici votre code de réinitialisation : " + code);

        mailSenderClient.sendMail(mailRequest);

        return ResponseEntity.ok(Collections.singletonMap("message", "Code envoyé"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");
        String code = request.get("code");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        // 1. Vérifier d'abord le code
        if (!verificationCodeService.verifyCode(email, code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Code invalide ou expiré"));
        }

        // 2. Vérifier que les mots de passe correspondent
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Les mots de passe ne correspondent pas"));
        }

        // 3. Vérifier que l'email existe
        if (!clientService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Aucun compte associé à cet email"));
        }

        try {
            // 4. Mettre à jour le mot de passe
            clientService.updatePassword(email, newPassword);

            // 5. Supprimer le code seulement après succès
            verificationCodeService.removeCode(email);

            return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la réinitialisation"));
        }
    }








    @PostMapping("/send-code")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = String.format("%06d", new Random().nextInt(999999));

        verificationCodeService.saveCode(email, code);

        MailRequest mailRequest = new MailRequest();
        mailRequest.setTo(email);
        mailRequest.setSubject("Code de vérification de votre compte");
        mailRequest.setBody("Voici votre code de vérification : " + code);

        mailSenderClient.sendMail(mailRequest);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Code de vérification envoyé");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        System.out.println(code + " " + email);

        boolean valid = verificationCodeService.verifyCode(email, code);
        Map<String, Object> response = new HashMap<>();

        if (valid) {
            //verificationCodeService.removeCode(email);
            response.put("valid", true);
            response.put("message", "Code valide");
        } else {
            response.put("valid", false);
            response.put("message", "Code invalide ou expiré");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("add")
    public ResponseEntity<Map<String, String>> registerClient(@RequestBody Client client) {
        Map<String, String> response = new HashMap<>();

        if (clientService.existsByEmail(client.getEmail())) {
            response.put("error", "Cet email est déjà utilisé");
            return ResponseEntity.badRequest().body(response);
        }

        client.setRole(Roles.CLIENT);
        client.setDateInscription(LocalDate.now());

        // Génération automatique du numéro SIP si non fourni
        if(client.getNumSIP() == null || client.getNumSIP().isEmpty()) {
            client.setNumSIP(clientService.generateUniqueSIPNumber());
        } else {
            // Vérification si le numéro SIP fourni est unique
            if(clientService.existsByNumSIP(client.getNumSIP())) {
                response.put("error", "Ce numéro SIP est déjà utilisé");
                return ResponseEntity.badRequest().body(response);
            }
        }


        clientService.addClient(client);
        response.put("message", "Client inscrit avec succès");
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @DeleteMapping("/remove/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}
