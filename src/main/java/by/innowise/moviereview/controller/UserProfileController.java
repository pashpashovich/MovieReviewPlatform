package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.ReviewService;
import by.innowise.moviereview.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user/profile")
public class UserProfileController {

    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public UserProfileController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String viewProfile(HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("user");

        if (userDto == null) {
            return "redirect:/login";
        }

        Long userId = userDto.getId();
        UserDto userDetails = userService.findById(userId);

        if (userDetails == null) {
            model.addAttribute("error", "Пользователь не найден");
            return "error";
        }

        model.addAttribute("user", userDetails);
        model.addAttribute("recentReviews", reviewService.findRecentReviewsByUserId(userId));
        return "user/profile";
    }
}
