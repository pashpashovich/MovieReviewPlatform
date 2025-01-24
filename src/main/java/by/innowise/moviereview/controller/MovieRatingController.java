package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String rateMovie(@RequestParam("movieId") Long movieId,
                            @RequestParam("rating") int rating,
                            HttpSession session,
                            HttpServletRequest request) {
        try {
            UserDto user = (UserDto) session.getAttribute("user");
            if (user == null) {
                throw new IllegalStateException("User not logged in.");
            }

            Long userId = user.getId();
            ratingService.saveOrUpdateRating(userId, movieId, rating);

            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/user/movies");
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}


