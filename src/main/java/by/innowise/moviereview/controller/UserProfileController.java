package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.ReviewService;
import by.innowise.moviereview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user/profile")
public class UserProfileController {

    private final UserService userService;
    private final ReviewService reviewService;

    public UserProfileController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public String viewProfile(Model model, @PathVariable("id") Long id) {
        UserDto userDetails = userService.findById(id);
        if (userDetails == null) {
            model.addAttribute("error", "Пользователь не найден");
            return "error";
        }

        model.addAttribute("user", userDetails);
        model.addAttribute("recentReviews", reviewService.findRecentReviewsByUserId(id));
        return "user/profile";
    }
}
