package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.mapper.MovieMapperImpl;
import by.innowise.moviereview.mapper.UserMapperImpl;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.repository.RatingRepositoryImpl;
import by.innowise.moviereview.repository.UserRepositoryImpl;
import by.innowise.moviereview.service.RatingService;
import by.innowise.moviereview.service.ReviewService;
import by.innowise.moviereview.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user/profile")
public class UserProfileServlet extends HttpServlet {
    private final UserService userService;
    private final ReviewService reviewService;
    private final RatingService ratingService;

    public UserProfileServlet() {
        this.ratingService = new RatingService(new RatingRepositoryImpl(),new UserRepositoryImpl(),new MovieRepositoryImpl());
        this.userService = new UserService(new UserRepositoryImpl(), new UserMapperImpl());
        this.reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = ((UserDto) req.getSession().getAttribute("user")).getId();
        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
            return;
        }

        UserDto userDto = userService.findById(userId);
        if (userDto == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Пользователь не найден");
            return;
        }
        req.setAttribute("recentReviews", reviewService.findRecentReviewsByUserId(userId));

        req.setAttribute("user", userDto);
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }
}