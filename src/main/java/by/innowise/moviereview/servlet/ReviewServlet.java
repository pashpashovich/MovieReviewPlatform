package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user/movies/review")
public class ReviewServlet extends HttpServlet {
    private final ReviewService reviewService;

    public ReviewServlet() {
        this.reviewService = new ReviewService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Long userId = ((UserDto) req.getSession().getAttribute("user")).getId();
            Long movieId = Long.parseLong(req.getParameter("movieId"));
            String content = req.getParameter("content");
            int rating = Integer.parseInt(req.getParameter("rating"));

            reviewService.addReview(userId, movieId, content, rating);

            resp.sendRedirect(req.getContextPath() + "/user/movies/" + movieId);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сохранения рецензии.");
        }
    }
}

