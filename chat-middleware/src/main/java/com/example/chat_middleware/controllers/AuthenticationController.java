package com.example.chat_middleware.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat_middleware.dao.UserDao;
import com.example.chat_middleware.dao.entities.Users;
import com.example.chat_middleware.dto.LoginRequest;
import com.example.chat_middleware.dto.RegistrationRequest;
import com.example.chat_middleware.dto.RegistrationResponse;
import com.example.chat_middleware.security.ChatUserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDao userDao;

    private final AuthenticationManager authManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    

    public AuthenticationController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @PostMapping ("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username cannot be empty");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }
        if (userDao.existsByUserName(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        Users user = new Users();
        user.setUserName(request.getUsername());
        user.setPassword(request.getPassword());
        Users savedUser = userDao.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrationResponse(savedUser.getUserName()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req,
        HttpServletRequest request,
        HttpServletResponse response) {
        try {
            Authentication auth = authManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(req.getUserName(), req.getPassword()));
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response); //creates the session
            ChatUserPrincipal p = (ChatUserPrincipal) auth.getPrincipal();

            return ResponseEntity.ok(new UserSummary(p.getId(), p.getUsername()));
        } catch (AuthenticationException e) {
            // Same message for unknown user and wrong password
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
        }
    }
    @GetMapping("/me")
    public UserSummary me(@AuthenticationPrincipal ChatUserPrincipal p) {
        return new UserSummary(p.getId(), p.getUsername());
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request,
        HttpServletResponse response,
        Authentication auth) {
        new SecurityContextLogoutHandler().logout(request, response, auth); //invalidates the session
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/csrf")
        public ResponseEntity<Void> csrf(CsrfToken token) {
        token.getToken(); // touching it makes Spring write the XSRF-TOKEN cookie
        return ResponseEntity.noContent().build();
    }
    public record UserSummary(UUID id, String userName) {}
    
}
