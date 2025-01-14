package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.UserDto;
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

    public UserProfileServlet() {
        this.userService = UserService.getInstance();
        this.reviewService = ReviewService.getInstance();
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