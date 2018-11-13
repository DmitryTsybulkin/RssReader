package rss.combinator.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.dto.UserDTO;
import rss.combinator.project.model.User;
import rss.combinator.project.repository.UserRepository;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserRepository userRepository;

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
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    }

    @Ignore
    @Test
    @WithMockUser(username = "admin", password = "user")
    public void createUser() throws Exception {
        UserDTO dto = UserDTO.builder().username("newUser").password("password").build();
        mockMvc.perform(post("/users/new")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", notNullValue()))
                .andExpect(jsonPath("$.username", equalTo(dto.getUsername())))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.role", notNullValue()))
                .andExpect(jsonPath("$.role", equalTo("USER")));
    }

    @Test
    public void createUserFailedEntryDuplicateException() throws Exception {

    }

    @Test
    @Ignore
    @WithMockUser(username = "admin", password = "user")
    public void getUserById() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", notNullValue()))
                .andExpect(jsonPath("$.username", equalTo(user.getUsername())))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(user.getId())))
                .andExpect(jsonPath("$.role", notNullValue()))
                .andExpect(jsonPath("$.role", equalTo(user.getRole())));
    }

    @Test
    public void getAllActiveUsers() throws Exception {
    }

    @Test
    public void deactivateUser() throws Exception {
    }

    @TestConfiguration
    public static class TestConfig {
        @MockBean
        private UserRepository userRepository;
    }
}