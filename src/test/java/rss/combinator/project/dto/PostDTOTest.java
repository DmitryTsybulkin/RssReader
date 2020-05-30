package rss.combinator.project.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PostDTOTest {

    private PostDTO testDTO = PostDTO.builder().title("title").date("00:00:00 06-11-2018").link("link").build();

    @Test
    public void compareTo() throws Exception {
        assertEquals(testDTO.compareTo(PostDTO.builder()
                .title("title")
                .date("00:00:00 06-11-2018")
                .link("link")
                .build()), 0);

        assertNotEquals(testDTO.compareTo(PostDTO.builder()
                .title("anotherTitle")
                .date("00:00:00 06-11-2018")
                .link("link")
                .build()), 0);

        assertNotEquals(testDTO.compareTo(PostDTO.builder()
                .title("title")
                .date("12:59:59 07-11-2018")
                .link("link")
                .build()), 0);

        assertNotEquals(testDTO.compareTo(PostDTO.builder()
                .title("title")
                .date("00:00:00 06-11-2018")
                .link("anotherLink")
                .build()), 0);
    }

}