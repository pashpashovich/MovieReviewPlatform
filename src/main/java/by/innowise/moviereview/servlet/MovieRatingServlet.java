package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.RatingService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/user/movies/rate")
public class MovieRatingServlet extends HttpServlet {
    private final RatingService ratingService = RatingService.getInstance();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Long userId = ((UserDto) session.getAttribute("user")).getId();
        Long movieId = Long.parseLong(req.getParameter("movieId"));
        int rating = Integer.parseInt(req.getParameter("rating"));

        try {
            ratingService.saveOrUpdateRating(userId, movieId, rating);
            resp.sendRedirect(req.getHeader("Referer"));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сохранения или обновления рейтинга.");
        }
    }
}

