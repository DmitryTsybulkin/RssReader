package rss.combinator.project.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RssParserTest {

    @Autowired
    private RssParser rssParser;

    private final String testLink1 = "http://feeds.reuters.com/reuters/companyNews";
    private final String testLink2 = "http://feeds.reuters.com/reuters/businessNews";
    private final String testLink3 = "http://feeds.reuters.com/reuters/healthNews";
    private final String testLink4 = "http://feeds.reuters.com/reuters/scienceNews";

    private Path path1;
    private Path path2;

    @Before
    public void setUp() throws Exception {
        path1 = Paths.get(rssParser.getAbsolute() + "news.json");
        path2 = Paths.get(rssParser.getAbsolute() + "health.json");
        assertFalse(Files.exists(path1));
        assertFalse(Files.exists(path2));
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
    }

    @Test
    public void parseRss() throws Exception {
        Map<String, List<String>> map = new HashMap<>();
        map.put("news", Arrays.asList(testLink1, testLink2));
        map.put("health", Arrays.asList(testLink3, testLink4));
        rssParser.parseRss(map);
        assertTrue(Files.exists(path1));
        assertTrue(Files.exists(path2));
        assertTrue(Files.isReadable(path1));
        assertTrue(Files.isReadable(path2));
    }

    @Test
    public void saveToFile() {

    }

    @Test
    public void deleteFile() {

    }

    @Test
    public void get() {

    }

    @Test
    public void toMessageDto() throws Exception {
    }
}