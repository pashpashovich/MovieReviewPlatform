package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/user/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<WatchlistDto>> getWatchlist(@PathVariable("userId") Long userId) {
        List<WatchlistDto> watchlist = watchlistService.getWatchlistByUserId(userId);
        return ResponseEntity.ok(watchlist);
    }

//    @PostMapping("/add")
//    public String addToWatchlist(@RequestParam("movieId") Long movieId, @RequestParam("userId") Long userId, @RequestHeader("Referer") String referer, Model model) throws UnsupportedEncodingException {
//        if (watchlistService.isMovieInWatchlist(userId, movieId)) {
//            return "redirect:" + referer + "?error=already_in_watchlist";
//        }
//        watchlistService.addToWatchlist(userId, movieId);
//        return "redirect:" + referer;
//    }

    @DeleteMapping()
    public ResponseEntity<Void> removeFromWatchlist(@RequestParam("movieId") Long movieId, @RequestParam("userId") Long userId) {
        watchlistService.removeFromWatchlist(userId, movieId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
