package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    @Mapping(target = "movies", ignore = true)
    List<PersonDto> toDto(List<Person> people);
}
