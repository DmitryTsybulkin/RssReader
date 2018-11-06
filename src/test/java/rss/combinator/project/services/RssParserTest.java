package rss.combinator.project.services;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.dto.PostDTO;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RssParserTest {

    @Autowired
    private RssParser rssParser;

    private final String company = "http://feeds.reuters.com/reuters/companyNews";
    private final String business = "http://feeds.reuters.com/reuters/businessNews";
    private final String science = "http://feeds.reuters.com/reuters/scienceNews";
    private final String space = "http://rss.cnn.com/rss/edition_space.rss";

    private Path path1 = Paths.get(Utils.getAbsolute() + "business.json");
    private Path path2 = Paths.get(Utils.getAbsolute() + "science.json");

    @Before
    public void setUp() throws Exception {
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
        assertTrue(Files.notExists(path1));
        assertTrue(Files.notExists(path2));
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
    }

    @Test
    public void parseRss() throws Exception {
        Map<String, List<String>> map = new HashMap<>();
        map.put("business", Arrays.asList(company, business));
        map.put("science", Arrays.asList(science, space));
        rssParser.parseRss(map);
        assertTrue(Files.exists(path1));
        assertTrue(Files.exists(path2));
        assertTrue(Files.isReadable(path1));
        assertTrue(Files.isReadable(path2));
    }

    @Test
    public void saveToFile() throws Exception {
        rssParser.saveToFile("name", Collections.singletonList("{\"name\":\"best-name\"}"));
        Path path = Paths.get(Utils.getAbsolute() + "name.json");
        assertTrue(Files.exists(path));
        assertTrue(Files.isReadable(path));
        assertTrue(Files.readAllLines(path).get(0).contains("{\"name\":\"best-name\"}"));
        Files.delete(path);
    }

    @Test
    public void deleteFile() throws Exception {
        rssParser.saveToFile("name", Collections.singletonList("{\"name\":\"best-name\"}"));
        Path path = Paths.get(Utils.getAbsolute() + "name.json");
        assertTrue(Files.exists(path));
        assertTrue(rssParser.deleteFile("name"));
        assertTrue(Files.notExists(path));
    }

    @Test
    public void parseLink() throws Exception {
        final Callable<List<String>> callable = rssParser.parseLink(science);
        assertNotNull(callable);
        assertTrue(callable.call().get(0).contains("["));
        assertTrue(callable.call().get(callable.call().size() - 1).contains("]"));
        assertTrue(callable.call().get(1).contains("title"));
        assertTrue(callable.call().get(1).contains("date"));
        assertTrue(callable.call().get(1).contains("link"));
    }

    @Test
    public void toPostDto() throws Exception {
        URL url = new URL(space);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(url));
        SyndEntry syndEntry = feed.getEntries().get(0);
        String pubDate = syndEntry.getPublishedDate().toString();
        String title = syndEntry.getTitle();
        String link = syndEntry.getLink();
        PostDTO dto = rssParser.toPostDto(syndEntry);
        assertNotNull(dto);
        assertNotNull(dto.getTitle());
        assertEquals(title, dto.getTitle());
        assertNotNull(dto.getDate());
        assertEquals(pubDate, dto.getDate());
        assertNotNull(dto.getLink());
        assertEquals(link, dto.getLink());
    }
}