package rss.combinator.project.representation;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.dto.UserDTO;
import rss.combinator.project.model.User;
import rss.combinator.project.services.UserService;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserRepresentationTest {

    @Mock
    private UserService userService;
    @Autowired
    private UserRepresentation userRepresentation;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .active(true)
                .role("USER")
                .build();
        when(userService.getById(anyLong())).thenReturn(user);
    }

    @Test
    public void createUser() {
    }

    @Ignore
    @Test
    public void getUserById() {
        final UserDTO userById = userRepresentation.getUserById(1L);
        assertNotNull(userById);
    }

    @Test
    public void getAllActiveUsers() {
    }

    @Test
    public void deactivateUser() {
    }

    @Test
    public void toDto() {
    }

    @Test
    public void fromDto() {
    }
}