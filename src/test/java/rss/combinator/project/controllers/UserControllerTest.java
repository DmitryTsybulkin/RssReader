package rss.combinator.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import rss.combinator.project.dto.UserReqDTO;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.model.User;
import rss.combinator.project.services.UserService;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    private ObjectMapper mapper = new ObjectMapper();

    private User user = User.builder()
            .id(1L)
            .username("admin")
            .password("$2a$10$7ZG7huQW.WC.mzce4M3F0.X8LQyDbOFiNucOU5ZjHLw9aU9ZktC5m")
            .active(true)
            .role("ADMIN")
            .build();

    @Before
    public void setUp() throws Exception {
        when(userService.loadUserByUsername(anyString()))
                .thenReturn(new UserService
                        .UserPrincipal(user.getUsername(), user.getPassword(), user.getActive()));
    }

    @Test
    @WithMockUser(username = "admin", password = "user")
    public void createUser() throws Exception {
        String username = "newUser";
        User result = User.builder()
                .id(1L)
                .role("USER")
                .active(true)
                .username(username)
                .password("password")
                .build();
        when(userService.create(any(User.class))).thenReturn(result);
        UserReqDTO dto = UserReqDTO.builder().username(username).password("password").build();
        mockMvc.perform(post("/users/new")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", notNullValue()))
                .andExpect(jsonPath("$.username", equalTo(dto.getUsername())))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.role", notNullValue()))
                .andExpect(jsonPath("$.role", equalTo("USER")));
    }

    @Test
    @WithMockUser(username = "admin", password = "user")
    public void createUserFailedEntryDuplicateException() throws Exception {
        when(userService.create(any(User.class))).thenThrow(EntryDuplicateException.class);
        UserReqDTO dto = UserReqDTO.builder().username(user.getUsername()).password("password").build();
        mockMvc.perform(post("/users/new")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", password = "user")
    public void getUserById() throws Exception {
        when(userService.getById(anyLong())).thenReturn(user);
        mockMvc.perform(get("/users/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", notNullValue()))
                .andExpect(jsonPath("$.username", equalTo(user.getUsername())))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(user.getId().intValue())))
                .andExpect(jsonPath("$.role", notNullValue()))
                .andExpect(jsonPath("$.role", equalTo(user.getRole())));
    }

    @Test
    @WithMockUser(username = "admin", password = "user")
    public void getAllActiveUsers() throws Exception {
        when(userService.getAllActive(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(user)));
        mockMvc.perform(get("/users")
                .param("page", "0")
                .param("size", "5"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.size", equalTo(0)))
                .andExpect(jsonPath("$.number", equalTo(0)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)))
                .andExpect(jsonPath("$.numberOfElements", equalTo(1)))
                .andExpect(jsonPath("$.totalElements", equalTo(1)));
    }

    @Test
    @WithMockUser(username = "admin", password = "user")
    public void deactivateUser() throws Exception {
        User result = User.builder()
                .id(2L)
                .role("USER")
                .active(true)
                .username("username")
                .password("password")
                .build();
        mockMvc.perform(delete("/users/" + result.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        verify(userService, times(1)).deactivate(anyLong());
    }

    @TestConfiguration
    public static class TestConfig {
        @MockBean
        private UserService userService;
    }
}