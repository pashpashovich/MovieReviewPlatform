package by.innowise.moviereview.controller;


import by.innowise.moviereview.service.AdminUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminUserService adminUserService;

    @InjectMocks
    private AdminUserController adminUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldGetAllUsers() throws Exception {
        // given
        when(adminUserService.getAllUsers()).thenReturn(Collections.emptyList());
        //when
        //then
        mockMvc.perform(get("/admin/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("users"));

        verify(adminUserService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldDeleteUser() throws Exception {
        // given
        //when
        //then
        mockMvc.perform(post("/admin/users")
                        .param("action", "delete")
                        .param("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
        verify(adminUserService, times(1)).deleteUser(1L);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldBlockUser() throws Exception {
        // given
        //when
        //then
        mockMvc.perform(post("/admin/users")
                        .param("action", "block")
                        .param("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(adminUserService, times(1)).blockUser(1L);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldUnblockUser() throws Exception {
        // given
        //when
        //then
        mockMvc.perform(post("/admin/users")
                        .param("action", "unblock")
                        .param("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(adminUserService, times(1)).unblockUser(1L);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldPromoteToAdmin() throws Exception {
        // given
        //when
        //then
        mockMvc.perform(post("/admin/users")
                        .param("action", "promote")
                        .param("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(adminUserService, times(1)).promoteToAdmin(1L);
    }
}