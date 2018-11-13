package rss.combinator.project.representation;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.dto.UserDTO;
import rss.combinator.project.model.User;
import rss.combinator.project.repository.UserRepository;
import rss.combinator.project.services.UserService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepresentationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepresentation userRepresentation;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("username", "password");
        user.setId(1L);
    }

    @Test
    public void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDTO dto = UserDTO.builder().username("username").password("password").build();
        UserDTO resultUser = userRepresentation.createUser(dto);
        assertNotNull(resultUser);
        assertEquals(resultUser.getUsername(), dto.getUsername());
        assertNotNull(resultUser.getId());
        assertNotNull(resultUser.getRole());
    }

    @Test
    public void getUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        final UserDTO userById = userRepresentation.getUserById(anyLong());
        assertNotNull(userById);
        assertEquals(userById.getUsername(), user.getUsername());
        assertEquals(userById.getId(), user.getId());
        assertEquals(userById.getRole(), "USER");
    }

    @Test
    public void getAllActiveUsers() {
        when(userRepository.findAllByActiveIsTrue(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(user)));
        Page<UserDTO> allActiveUsers = userRepresentation.getAllActiveUsers(Pageable.unpaged());
        assertNotNull(allActiveUsers);
        assertEquals(allActiveUsers.getTotalElements(), 1);
        assertEquals(allActiveUsers.getTotalPages(), 1);
        assertTrue(allActiveUsers.stream().anyMatch(userDTO ->
                userDTO.getUsername().equals(user.getUsername()) &&
                        userDTO.getId().equals(user.getId()) &&
                        userDTO.getRole().equals(user.getRole())));
    }

    @Test
    public void deactivateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userRepresentation.deactivateUser(anyLong());
        assertFalse(user.getActive());
    }

    @Test
    public void toDto() {
        UserDTO dto = userRepresentation.toDto(user);
        assertNotNull(dto);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getRole(), user.getRole());
        assertNull(dto.getPassword());
    }

    @Test
    public void fromDto() {
        UserDTO dto = UserDTO.builder().username("username").password("password").build();
        User result = userRepresentation.fromDto(dto);
        assertNotNull(result);
        assertEquals(result.getUsername(), dto.getUsername());
        assertEquals(result.getPassword(), result.getPassword());
    }

    @TestConfiguration
    public static class TestConf {
        @MockBean
        private UserRepository userRepository;
    }

}