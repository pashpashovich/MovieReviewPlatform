package by.innowise.moviereview.controller;


import by.innowise.moviereview.dto.PersonCreateDto;
import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/admin/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

//    @GetMapping
//    public String getPeople(@RequestParam(value = "page", defaultValue = "1") int page,
//                            @RequestParam(value = "size", defaultValue = "10") int size,
//                            @RequestParam(value = "search", required = false) String searchQuery,
//                            @RequestParam(value = "role", required = false) String roleFilter) {
//        List<Person> people;
//        long totalRecords;
//        if ((searchQuery == null || searchQuery.isEmpty()) && (roleFilter == null || roleFilter.isEmpty())) {
//            people = personService.getAllPeopleWithPagination(page, size);
//            totalRecords = personService.countPeople();
//        } else {
//            people = personService.getPeopleWithFiltersAndPagination(page, size, searchQuery, roleFilter);
//            totalRecords = personService.countPeopleWithFilters(searchQuery, roleFilter);
//        }
//        int totalPages = (int) Math.ceil((double) totalRecords / size);
//
//        return "admin/people";
//    }

    @PostMapping
    public ResponseEntity<PersonDto> addPerson(@RequestBody PersonCreateDto personCreateDto) {
        PersonDto personDto = personService.addPerson(personCreateDto);
        return ResponseEntity.ok(personDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> updatePerson(@PathVariable Long id, @RequestBody PersonCreateDto personCreateDto) {
        PersonDto updatedPerson = personService.update(id, personCreateDto);
        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") Long id) {
        personService.deletePersonById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

