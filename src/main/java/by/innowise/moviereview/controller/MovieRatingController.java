package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.RateCreateDto;
import by.innowise.moviereview.dto.RatingDto;
import by.innowise.moviereview.dto.RatingUpdateRequest;
import by.innowise.moviereview.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/movies/rate")
@RequiredArgsConstructor
public class MovieRatingController {
    private final RatingService ratingService;

    @PostMapping()
    public ResponseEntity<RatingDto> rateMovie(@RequestBody RateCreateDto rateDto) {
        RatingDto ratingDto = ratingService.saveRating(rateDto);
        return ResponseEntity.ok(ratingDto);
    }

    @PatchMapping("/{rateId}")
    public ResponseEntity<RatingDto> updateRateMovie(@PathVariable("rateId") Long rateId, @RequestBody RatingUpdateRequest ratingUpdateRequest) {
        RatingDto ratingDto = ratingService.updateRating(rateId, ratingUpdateRequest);
        return ResponseEntity.ok(ratingDto);
    }
}


