package rss.combinator.project.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.model.User;
import rss.combinator.project.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("username", "password");
        user.setId(1L);
    }

    @Test
    public void create() throws Exception {
        userService.create(any());
    }

    @Test(expected = EntryDuplicateException.class)
    public void createFailedEntryDuplicateException() throws Exception {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        userService.create(user);
    }

    @Test
    public void getById() throws Exception {
        
    }

    @Test
    public void getByIdResourceNotFoundException() throws Exception {

    }

    @Test
    public void getAllActive() throws Exception {
    }

    @Test
    public void deactivate() throws Exception {
    }

    @Test
    public void loadUserByUsername() throws Exception {
    }

    @Test
    public void loadUserByUsernameFailedNotFound() throws Exception {

    }

    @TestConfiguration
    public static class TestConf {
        @MockBean
        private UserRepository userRepository;
    }
}