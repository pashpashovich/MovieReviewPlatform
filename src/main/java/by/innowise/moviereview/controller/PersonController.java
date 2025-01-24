package by.innowise.moviereview.controller;


import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.service.PersonService;
import by.innowise.moviereview.util.enums.MovieRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String getPeople(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "search", required = false) String searchQuery,
                            @RequestParam(value = "role", required = false) String roleFilter,
                            Model model) {
        List<Person> people = personService.getAllPeople(page, size, searchQuery, roleFilter);
        long totalRecords = personService.countPeople(searchQuery, roleFilter);
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        model.addAttribute("people", people);
        model.addAttribute("roles", MovieRole.values());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("roleFilter", roleFilter);
        model.addAttribute("totalPages", totalPages);

        return "admin/people";
    }

    @PostMapping
    public String addPerson(@RequestParam("fullName") String fullName,
                            @RequestParam("role") String role) {
        Person person = new Person();
        person.setFullName(fullName);
        person.setRole(MovieRole.valueOf(role));
        personService.addPerson(person);
        return "redirect:/admin/people";
    }

    @PutMapping
    public String updatePerson(@RequestParam("id") Long id,
                               @RequestParam("fullName") String fullName,
                               @RequestParam("role") String role) {
        Person person = personService.getPersonById(id);
        if (person != null) {
            person.setFullName(fullName);
            person.setRole(MovieRole.valueOf(role));
            personService.update(person);
        }
        return "redirect:/admin/people";
    }

    @DeleteMapping
    public String deletePerson(@RequestParam("id") Long id) {
        personService.deletePersonById(id);
        return "redirect:/admin/people";
    }
}

