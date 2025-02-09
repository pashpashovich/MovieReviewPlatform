package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.AuthenticationRequest;
import by.innowise.moviereview.dto.AuthenticationResponse;
import by.innowise.moviereview.dto.ErrorResponseImpl;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService service;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseImpl> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseImpl> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

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