package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.service.PersonService;
import by.innowise.moviereview.util.enums.MovieRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class PersonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    void shouldGetPeopleWithFilters() throws Exception {
        // Given
        List<Person> people = Arrays.asList(
                new Person(),
                new Person()
        );
        Mockito.when(personService.getPeopleWithFiltersAndPagination(1, 10, "Person", "ACTOR"))
                .thenReturn(people);
        Mockito.when(personService.countPeopleWithFilters("Person", "ACTOR"))
                .thenReturn(2L);

        // When & Then
        mockMvc.perform(get("/admin/people")
                        .param("search", "Person")
                        .param("role", "ACTOR")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("people"))
                .andExpect(model().attribute("people", people))
                .andExpect(view().name("admin/people"));
    }

    @Test
    void shouldAddPerson() throws Exception {
        // Given
        Person person = new Person();
        person.setFullName("New Person");
        person.setRole(MovieRole.ACTOR);

        Mockito.doNothing().when(personService).addPerson(any(Person.class));

        // When & Then
        mockMvc.perform(post("/admin/people")
                        .param("fullName", "New Person")
                        .param("role", "ACTOR"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/people"));
    }

    @Test
    void shouldUpdatePerson() throws Exception {
        // Given
        Mockito.doNothing().when(personService).update(any(PersonDto.class));

        // When & Then
        mockMvc.perform(put("/admin/people")
                        .param("id", "1")
                        .param("fullName", "Updated Person")
                        .param("role", "DIRECTOR"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/people"));
    }

    @Test
    void shouldDeletePerson() throws Exception {
        // Given
        Mockito.doNothing().when(personService).deletePersonById(anyLong());

        // When & Then
        mockMvc.perform(delete("/admin/people")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/people"));
    }
}
