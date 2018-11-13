package rss.combinator.project.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.model.User;

import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = userRepository.save(User.builder()
                .username("username")
                .password("password")
                .active(true)
                .role("USER")
                .build());
    }

    @Test
    public void findByUsername() throws Exception {
        final Optional<User> targetUser = userRepository.findByUsername("username");
        assertTrue(targetUser.isPresent());
        assertNotNull(targetUser.get().getId());
        assertEquals(targetUser.get().getPassword(), user.getPassword());
        assertTrue(targetUser.get().getActive());
        assertEquals(targetUser.get().getRole(), user.getRole());
    }

    @Test
    public void findAllByActiveIsTrue() throws Exception {
        final Page<User> allByActiveIsTrue = userRepository.findAllByActiveIsTrue(Pageable.unpaged());
        assertNotNull(allByActiveIsTrue);
        assertEquals(allByActiveIsTrue.getTotalElements(), 1);
        assertEquals(allByActiveIsTrue.getTotalPages(), 1);
        assertTrue(allByActiveIsTrue.stream()
                .anyMatch(targetUser -> targetUser.getUsername().equals(user.getUsername()) &&
                        targetUser.getPassword().equals(user.getPassword()) &&
                        targetUser.getRole().equals(user.getRole()) &&
                        targetUser.getActive()));
    }
}