package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.dao.MovieDao;
import by.innowise.moviereview.dao.UserDao;
import by.innowise.moviereview.dao.WatchlistDao;
import by.innowise.moviereview.service.WatchlistService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@WebServlet(name = "WatchlistServlet", urlPatterns = {"/user/watchlist/add", "/user/watchlist", "/user/watchlist/remove"})
public class WatchlistServlet extends HttpServlet {
    private final WatchlistService watchlistService;

    public WatchlistServlet() {
        this.watchlistService = WatchlistService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Long userId = ((UserDto) req.getSession().getAttribute("user")).getId();
            List<WatchlistDto> watchlist = watchlistService.getWatchlistByUserId(userId);
            req.setAttribute("watchlist", watchlist);
            req.getRequestDispatcher("/WEB-INF/views/user/watchlist.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Не удалось загрузить список.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Long userId = ((UserDto) req.getSession().getAttribute("user")).getId();
            Long movieId = Long.parseLong(req.getParameter("movieId"));
            if (watchlistService.isMovieInWatchlist(userId, movieId)) {
                resp.sendRedirect(req.getHeader("Referer") + "?error=already_in_watchlist");
                return;
            }
            watchlistService.addToWatchlist(userId, movieId);
            resp.sendRedirect(req.getHeader("Referer"));
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getHeader("Referer") + "?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка добавления в Watchlist.");
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Long userId = ((UserDto) req.getSession().getAttribute("user")).getId();
            System.out.println(req.getParameter("movieId"));
            Long movieId = Long.parseLong(req.getParameter("movieId"));
            watchlistService.removeFromWatchlist(userId, movieId);
            resp.sendRedirect(req.getContextPath() + "/user/watchlist");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка удаления из Watchlist.");
        }
    }
}
