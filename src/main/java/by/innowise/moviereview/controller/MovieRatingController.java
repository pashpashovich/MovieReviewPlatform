package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.RateCreateDto;
import by.innowise.moviereview.dto.RatingDto;
import by.innowise.moviereview.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/movies")
@RequiredArgsConstructor
public class MovieRatingController {
    private final RatingService ratingService;

    @PostMapping("/rate")
    public ResponseEntity<RatingDto> rateMovie(@RequestBody RateCreateDto rateDto) {
        RatingDto ratingDto = ratingService.saveOrUpdateRating(rateDto);
        return ResponseEntity.ok(ratingDto);
    }
}


