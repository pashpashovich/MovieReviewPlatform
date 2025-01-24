package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.exception.BadCredentialsException;
import by.innowise.moviereview.exception.NoAccessException;
import by.innowise.moviereview.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showLoginPage() {
        return "common/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        try {
            UserDto userDto = userService.authenticate(email, password);

            session.setAttribute("user", userDto);

            switch (userDto.getRole()) {
                case ADMIN:
                    return "redirect:/admin/movies";
                case USER:
                    return "redirect:/user/profile";
                default:
                    return "redirect:/";
            }
        } catch (BadCredentialsException e) {
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            model.addAttribute("error", "Неверный логин или пароль. Повторите попытку");
            return "common/login";
        } catch (NoAccessException e) {
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            model.addAttribute("error", "Вас заблокировали. Если считаете, что произошла ошибка, свяжитесь с поддержкой");
            return "common/login";
        }
    }
}
