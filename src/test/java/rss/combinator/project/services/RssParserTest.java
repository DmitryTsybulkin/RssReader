package rss.combinator.project.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.services.RssParser;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RssParserTest {

    @Autowired
    private RssParser rssParser;

    @Before
    public void setUp() throws Exception {
        Files.createDirectory(Paths.get("downloads"));
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(Paths.get("downloads/news.json"));
        Files.deleteIfExists(Paths.get("downloads"));
    }

    @Test
    public void parseRss() throws Exception {
        rssParser.parseRss("http://rss.cnn.com/rss/edition_europe.rss", "news");
        assertTrue(Files.exists(Paths.get("downloads/news.json")));
    }

    @Test
    public void toMessageDto() throws Exception {
    }

    @Test
    public void save() throws Exception {
    }
}