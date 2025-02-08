package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.AuthenticationRequest;
import by.innowise.moviereview.dto.AuthenticationResponse;
import by.innowise.moviereview.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = service.authenticate(request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.clearContext();
            log.info("User logged out: {}", authentication.getName());
        }
        return ResponseEntity.ok().build();
    }

}