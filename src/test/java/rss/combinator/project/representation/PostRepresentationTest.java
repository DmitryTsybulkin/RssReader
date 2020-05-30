package rss.combinator.project.representation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.services.RssParser;
import rss.combinator.project.services.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

@Transactional
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
        final String reddit = "https://www.reddit.com/r/news+wtf.rss";
        final String sport = "http://rss.cnn.com/rss/edition_sport.rss";
        final String football = "http://rss.cnn.com/rss/edition_football.rss";

        path1 = Paths.get(Utils.getAbsolute() + "reddit.json");
        path2 = Paths.get(Utils.getAbsolute() + "sport.json");

        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
        assertTrue(Files.notExists(path1));
        assertTrue(Files.notExists(path2));

        Map<String, List<String>> map = new HashMap<>();
        map.put("reddit", Collections.singletonList(reddit));
        map.put("sport", Arrays.asList(sport, football));

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
        LocalDateTime ago = LocalDateTime.now().minusHours(5);
        List<PostDTO> travel = postRepresentation
                .getByTagAndFromDate(Collections.singletonList("reddit"), ago);

        assertNotNull(travel);
        assertFalse(travel.isEmpty());
        assertTrue(travel.stream().allMatch(dto ->
                dto.getLink().contains("www.reddit.com") &&
                        (LocalDateTime.parse(dto.getDate(), Utils.outDateFormat).isAfter(ago) ||
                                LocalDateTime.parse(dto.getDate(), Utils.outDateFormat).isEqual(ago))));
    }

    @Test
    public void getAll() throws Exception {
        List<PostDTO> all = postRepresentation.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertTrue(all.stream().anyMatch(item ->
                item.getLink().contains("www.reddit.com") ||
                        item.getLink().contains("edition_sport") ||
                        item.getLink().contains("edition_football")));
    }

    @Test
    public void getAllFromDate() throws Exception {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        List<PostDTO> all = postRepresentation.getAllFromDate(from);
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertTrue(all.stream().anyMatch(item ->
                item.getLink().contains("www.reddit.com") ||
                        item.getLink().contains("edition_sport") ||
                        item.getLink().contains("edition_football")));
        assertTrue(all.stream().allMatch(item ->
                LocalDateTime.parse(item.getDate(), Utils.outDateFormat).isAfter(from) ||
                        LocalDateTime.parse(item.getDate(), Utils.outDateFormat).isEqual(from)));
    }

    @Test
    public void getByTags() throws Exception {
        List<PostDTO> all = postRepresentation.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());

        List<PostDTO> reddit = postRepresentation.getByTags(Collections.singletonList("reddit"));
        assertNotNull(reddit);
        assertFalse(reddit.isEmpty());
        assertTrue(all.size() > reddit.size());
        assertTrue(reddit.stream().allMatch(dto -> dto.getLink().contains("www.reddit.com")));
    }

    @Test
    public void getFromDate() throws Exception {
        List<PostDTO> all = postRepresentation.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<PostDTO> fromDate = postRepresentation.getFromDate(all, yesterday);
        assertTrue(fromDate.stream().allMatch(dto ->
                LocalDateTime.parse(dto.getDate(), Utils.outDateFormat).isAfter(yesterday) ||
                        LocalDateTime.parse(dto.getDate(), Utils.outDateFormat).isEqual(yesterday)));
    }
}