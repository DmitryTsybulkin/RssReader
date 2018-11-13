package rss.combinator.project.representation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.dto.UserReqDTO;
import rss.combinator.project.dto.UserResDTO;
import rss.combinator.project.model.User;
import rss.combinator.project.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepresentationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepresentation userRepresentation;

    private User user = User.builder().id(1L).username("username").password("password").role("ADMIN").build();

    @Test
    public void createUser() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserReqDTO dto = UserReqDTO.builder().username("username").password("password").build();
        UserResDTO resultUser = userRepresentation.createUser(dto);
        assertNotNull(resultUser);
        assertEquals(resultUser.getUsername(), dto.getUsername());
        assertNotNull(resultUser.getId());
        assertNotNull(resultUser.getRole());
    }

    @Test
    public void getUserById() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        final UserResDTO userById = userRepresentation.getUserById(anyLong());
        assertNotNull(userById);
        assertEquals(userById.getUsername(), user.getUsername());
        assertEquals(userById.getId(), user.getId());
        assertEquals(userById.getRole(), "ADMIN");
    }

    @Test
    public void getAllActiveUsers() throws Exception {
        when(userRepository.findAllByActiveIsTrue(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(user)));
        Page<UserResDTO> allActiveUsers = userRepresentation.getAllActiveUsers(Pageable.unpaged());
        assertNotNull(allActiveUsers);
        assertEquals(allActiveUsers.getTotalElements(), 1);
        assertEquals(allActiveUsers.getTotalPages(), 1);
        assertTrue(allActiveUsers.stream().anyMatch(userDTO ->
                userDTO.getUsername().equals(user.getUsername()) &&
                        userDTO.getId().equals(user.getId()) &&
                        userDTO.getRole().equals(user.getRole())));
    }

    @Test
    public void deactivateUser() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userRepresentation.deactivateUser(anyLong());
        assertFalse(user.getActive());
    }

    @Test
    public void toDto() throws Exception {
        UserResDTO dto = userRepresentation.toDto(user);
        assertNotNull(dto);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getRole(), user.getRole());
    }

    @Test
    public void fromDto() throws Exception {
        UserReqDTO dto = UserReqDTO.builder().username("username").password("password").build();
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