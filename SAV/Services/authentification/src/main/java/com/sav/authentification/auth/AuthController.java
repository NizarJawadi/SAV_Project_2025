package com.sav.authentification.auth;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.User;
import com.sav.authentification.services.UserServicesImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserServicesImpl servicesImpl;

    private static final String SECRET_KEY = "superSecretKeyForJwtThatIsAtLeast32CharactersLong";

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getLogin(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getLogin());
        Roles role = servicesImpl.getRoleByLogin(authenticationRequest.getLogin());
        
        
        User us = servicesImpl.getUserByLogin(authenticationRequest.getLogin());
        
        final String jwt = jwtUtil.generateToken(us.getIdUser() ,userDetails.getUsername(), authenticationRequest.getLogin(), role , us.getUsername() , us.getNumSIP());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping("/nizar")
    public ResponseEntity<?> nizar() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "nizar");
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Vérifier si l'utilisateur existe déjà
        if (servicesImpl.getUserByLogin(request.getLogin()) != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        // Créer un nouvel utilisateur
        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .login(request.getLogin())
                .password(request.getPassword())  // Il sera encodé dans le service
                .role(request.getRole())
                .build();

        // Ajouter l'utilisateur en base
        servicesImpl.addUser(newUser);

        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAuthenticationToken(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();

            if (username != null && !jwtUtil.isTokenExpired(token)) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                String newToken = jwtUtil.generateRefreshToken(userDetails);
                Map<String, String> response = new HashMap<>();
                response.put("refreshToken", newToken);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Invalid or expired token");
            }
        } catch (SignatureException e) {
            return ResponseEntity.status(401).body("Invalid token signature");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }
}
