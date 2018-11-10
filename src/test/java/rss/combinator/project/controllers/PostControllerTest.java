package rss.combinator.project.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import rss.combinator.project.services.RssParser;
import rss.combinator.project.services.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RssParser rssParser;

    private Path path = Paths.get(Utils.getAbsolute() + "reddit.json");
    private final String tagName = "reddit";
    private final String reddit = "https://www.reddit.com/r/news+wtf.rss";

    @Before
    public void setUp() throws Exception {
        Map<String, List<String>> map = new HashMap<>();
        map.put(tagName, Collections.singletonList(reddit));
        rssParser.parseRss(map);
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(path);
    }

    @Test
    public void getPosts() throws Exception {
        String contentType = "text/event-stream;charset=UTF-8";
        String from = LocalDateTime.now().minusHours(5).format(Utils.outDateFormat);

        mockMvc.perform(get("/posts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(content().contentType(contentType));

        mockMvc.perform(get("/posts")
                .param("from", from))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(content().contentType(contentType));

        mockMvc.perform(get("/posts")
                .param("tag", tagName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(content().contentType(contentType));

        mockMvc.perform(get("/posts")
                .param("tag", tagName)
                .param("from", from))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(content().contentType(contentType));
    }
}