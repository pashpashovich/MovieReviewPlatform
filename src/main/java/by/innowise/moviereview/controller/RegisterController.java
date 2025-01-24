package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegisterPage() {
        return "common/register";
    }

    @PostMapping
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model) {
        UserCreateDto userDto = UserCreateDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        try {
            userService.register(userDto);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "common/register";
        }
    }
}
