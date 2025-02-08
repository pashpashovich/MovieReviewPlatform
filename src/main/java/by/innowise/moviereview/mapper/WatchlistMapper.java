package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.entity.Watchlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WatchlistMapper {
    @Mapping(target = "movieId", source = "movie.id")
    @Mapping(target = "movieTitle", source = "movie.title")
    @Mapping(target = "posterBase64", source = "movie.posterBase64")
    @Mapping(target = "addedAt", source = "addedAt")
    WatchlistDto toDto(Watchlist watchlist);

    List<WatchlistDto> toListDto(List<Watchlist> watchlist);}
