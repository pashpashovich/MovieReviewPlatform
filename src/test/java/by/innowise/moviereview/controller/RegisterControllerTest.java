package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class RegisterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
    }

    @Test
    void shouldShowRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("common/register"));
    }

    @Test
    void shouldRegisterUserSuccess() throws Exception {
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "password123";

        Mockito.doNothing().when(userService).register(any(UserCreateDto.class));

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void shouldRegisterUserFailure() throws Exception {
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "password123";

        doThrow(new RuntimeException("Registration failed")).when(userService).register(any(UserCreateDto.class));

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("common/register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Registration failed"));
    }
}
