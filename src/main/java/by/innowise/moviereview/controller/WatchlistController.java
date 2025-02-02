package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/user/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping("/{userId}")
    public String getWatchlist(Model model, @PathVariable("userId") Long userId) {
        try {
            List<WatchlistDto> watchlist = watchlistService.getWatchlistByUserId(userId);
            model.addAttribute("watchlist", watchlist);
            model.addAttribute("userId", userId);
            return "user/watchlist";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Не удалось загрузить список.");
            return "error";
        }
    }

    @PostMapping("/add")
    public String addToWatchlist(@RequestParam("movieId") Long movieId, @RequestParam("userId") Long userId, @RequestHeader("Referer") String referer, Model model) throws UnsupportedEncodingException {
        try {
            if (watchlistService.isMovieInWatchlist(userId, movieId)) {
                return "redirect:" + referer + "?error=already_in_watchlist";
            }
            watchlistService.addToWatchlist(userId, movieId);
            return "redirect:" + referer;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", URLEncoder.encode(e.getMessage(), "UTF-8"));
            return "redirect:" + referer + "?error=" + URLEncoder.encode(e.getMessage(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка добавления в Watchlist.");
            return "error";
        }
    }

    @DeleteMapping()
    public String removeFromWatchlist(@RequestParam("movieId") Long movieId, @RequestParam("userId") Long userId) {
        try {
            watchlistService.removeFromWatchlist(userId, movieId);
            return "redirect:/user/watchlist/"+userId;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
