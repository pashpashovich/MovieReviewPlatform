package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String showRegisterPage() {
        return "common/register";
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody UserCreateDto userCreateDto) {
        UserDto userDto = userService.register(userCreateDto);
        return ResponseEntity.ok(userDto);
    }
}
