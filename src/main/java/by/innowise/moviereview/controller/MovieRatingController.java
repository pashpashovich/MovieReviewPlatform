package by.innowise.moviereview.controller;

import by.innowise.moviereview.service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/movies")
public class MovieRatingController {
    private final RatingService ratingService;

    public MovieRatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/rate")
    public String rateMovie(
            @RequestParam("userId") Long userId,
            @RequestParam("movieId") Long movieId,
            @RequestParam("rating") int rating,
            HttpServletRequest request) {
        try {
            ratingService.saveOrUpdateRating(userId, movieId, rating);

            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/user/movies/"+userId);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}


