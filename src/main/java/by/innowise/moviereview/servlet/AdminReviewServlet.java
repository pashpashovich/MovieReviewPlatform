package by.innowise.moviereview.servlet;

import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/reviews")
public class AdminReviewServlet extends HttpServlet {
    private final ReviewService reviewService = ReviewService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Review> reviews = reviewService.findAllPendingReviews();
        req.setAttribute("reviews", reviews);
        req.getRequestDispatcher("/WEB-INF/views/admin/reviews.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long reviewId = Long.parseLong(req.getParameter("reviewId"));
        String status = req.getParameter("status");

        try {
            reviewService.updateReviewStatus(reviewId, status);
            resp.sendRedirect(req.getContextPath() + "/admin/reviews");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Не удалось обновить статус рецензии.");
        }
    }
}

