package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.ErrorResponseImpl;
import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.exception.EmailNotAvailableException;
import by.innowise.moviereview.exception.UserNotFoundException;
import by.innowise.moviereview.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("api/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseImpl> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailNotAvailableException.class)
    public ResponseEntity<ErrorResponseImpl> handleEmailNotAvailableException(EmailNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        UserDto userDto = userService.register(userCreateDto);
        return ResponseEntity.ok(userDto);
    }
}
