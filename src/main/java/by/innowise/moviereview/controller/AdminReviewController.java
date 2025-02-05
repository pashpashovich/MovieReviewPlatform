package by.innowise.moviereview.controller;

import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final ReviewService reviewService;

    public AdminReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String getPendingReviews(Model model) {
        List<Review> reviews = reviewService.findAllPendingReviews();
        model.addAttribute("reviews", reviews);
        return "admin/reviews";
    }

    @PostMapping
    public String updateReviewStatus(@RequestParam("reviewId") Long reviewId, @RequestParam("status") String status) {
        try {
            reviewService.updateReviewStatus(reviewId, status);
            return "redirect:/admin/reviews";
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обновить статус рецензии.", e);
        }
    }
}
