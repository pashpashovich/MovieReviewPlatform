package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonDto toDto(Person person);

    @Mapping(target = "movies", ignore = true)
    List<PersonDto> toListDto(List<Person> people);
}
