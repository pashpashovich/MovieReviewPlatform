package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    UserDto toDto(User user);


    @Mapping(target = "password", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "watchlist", ignore = true)
    User toEntity(UserDto userDto);

    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "watchlist", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isBlocked", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntityCreate(UserCreateDto userDto);
}

