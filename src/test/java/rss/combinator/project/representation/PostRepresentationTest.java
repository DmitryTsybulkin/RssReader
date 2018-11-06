package rss.combinator.project.representation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.services.RssParser;
import rss.combinator.project.services.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PostRepresentationTest {

    @Autowired
    private PostRepresentation postRepresentation;
    @Autowired
    private RssParser rssParser;

    private Path path1;
    private Path path2;

    @Before
    public void setUp() throws Exception {
        final String business = "http://feeds.reuters.com/reuters/businessNews";
        final String science = "http://feeds.reuters.com/reuters/scienceNews";
        final String space = "http://rss.cnn.com/rss/edition_space.rss";

        path1 = Paths.get(Utils.getAbsolute() + "business.json");
        path2 = Paths.get(Utils.getAbsolute() + "science.json");

        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
        assertTrue(Files.notExists(path1));
        assertTrue(Files.notExists(path2));

        Map<String, List<String>> map = new HashMap<>();
        map.put("business", Collections.singletonList(business));
        map.put("science", Arrays.asList(science, space));

        rssParser.parseRss(map);
        assertTrue(Files.exists(path1));
        assertTrue(Files.exists(path2));
        assertTrue(Files.isReadable(path1));
        assertTrue(Files.isReadable(path2));
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
    }

    @Test
    public void getByTagAndFromDate() throws Exception {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<PostDTO> business = postRepresentation.getByTagAndFromDate(Collections.singletonList("business"), yesterday);

        assertNotNull(business);
        assertFalse(business.isEmpty());
        business.forEach(postDTO -> {
            assertTrue(postDTO.getLink().contains("businessNews"));
            assertTrue(LocalDateTime.parse(postDTO.getDate(), Utils.outDateFormat).isAfter(yesterday));
        });
    }

    @Test
    public void getAll() throws Exception {
        List<PostDTO> all = postRepresentation.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    public void getByTags() throws Exception {
        List<PostDTO> all = postRepresentation.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());

        List<PostDTO> business = postRepresentation.getByTags(Collections.singletonList("business"));
        assertNotNull(business);
        assertFalse(business.isEmpty());

        assertTrue(all.size() > business.size());

        business.forEach(postDTO -> {
            assertTrue(postDTO.getLink().contains("businessNews"));
        });
    }

    @Test
    public void getFromDate() throws Exception {
        List<PostDTO> all = postRepresentation.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        List<PostDTO> fromDate = postRepresentation.getFromDate(all, yesterday);

        fromDate.forEach(postDTO -> {
            assertTrue(LocalDateTime.parse(postDTO.getDate(), Utils.outDateFormat).isAfter(yesterday));
        });
    }
}