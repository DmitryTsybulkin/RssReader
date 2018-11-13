package rss.combinator.project.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.exceptions.ResourceNotFoundException;
import rss.combinator.project.model.User;
import rss.combinator.project.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private User user = User.builder().username("username").password("password").active(true).role("USER").build();

    @Test
    public void create() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);
        final User result = userService.create(this.user);
        assertNotNull(result);
        assertEquals(result.getUsername(), user.getUsername());
        assertEquals(result.getPassword(), user.getPassword());
    }

    @Test(expected = EntryDuplicateException.class)
    public void createFailedEntryDuplicateException() throws Exception {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        userService.create(user);
    }

    @Test
    public void getById() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        final User result = userService.getById(1L);
        assertNotNull(result);
        assertEquals(result.getUsername(), user.getUsername());
        assertEquals(result.getPassword(), user.getPassword());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByIdResourceNotFoundException() throws Exception {
        userService.getById(1000L);
    }

    @Test
    public void getAllActive() throws Exception {
        when(userRepository.findAllByActiveIsTrue(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(user)));
        final Page<User> allActive = userService.getAllActive(Pageable.unpaged());
        assertNotNull(allActive);
        assertTrue(allActive.stream()
                .anyMatch(item -> item.getUsername().equals(user.getUsername()) &&
                        item.getPassword().equals(user.getPassword())));
    }

    @Test
    public void deactivate() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userService.deactivate(1L);
        assertFalse(user.getActive());
    }

    @Test
    public void loadUserByUsername() throws Exception {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        assertNotNull(userDetails);
        assertEquals(userDetails.getUsername(), user.getUsername());
        assertEquals(userDetails.getPassword(), user.getPassword());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameFailedNotFound() throws Exception {
        userService.loadUserByUsername(anyString());
    }

    @TestConfiguration
    public static class TestConf {
        @MockBean
        private UserRepository userRepository;
    }
}