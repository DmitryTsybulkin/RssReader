package rss.combinator.project.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.model.User;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    private User user;

    @Before
    public void setUp() throws Exception {
        user = User.builder()
                .username("username")
                .password("password")
                .active(true)
                .role("USER")
                .build();
//        when()
    }

    @Test
    public void create() throws Exception {
    }

    @Test
    public void getById() throws Exception {
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
}