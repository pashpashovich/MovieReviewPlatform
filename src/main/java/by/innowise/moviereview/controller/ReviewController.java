package by.innowise.moviereview.controller;

import by.innowise.moviereview.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user/movies")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review")
    public String addReview(
            @RequestParam("userId") Long userId,
            @RequestParam("movieId") Long movieId,
            @RequestParam("content") String content,
            @RequestParam("rating") int rating,
            HttpSession session, Model model) {
        try {
            reviewService.addReview(userId, movieId, content, rating);

            session.setAttribute("successMessage", "Ваша рецензия добавлена в список обработки у администратора, ожидайте.");
            return "redirect:/user/movies/movie/" + movieId + "?userId=" + userId;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Ошибка сохранения рецензии.");
            return "error";
        }
    }
}
