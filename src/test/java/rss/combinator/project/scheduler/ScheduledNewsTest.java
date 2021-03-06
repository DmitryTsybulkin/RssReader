package rss.combinator.project.scheduler;

import org.junit.Test;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.runner.RunWith;
import rss.combinator.project.entities.Tag;
import org.mockito.junit.MockitoJUnitRunner;
import rss.combinator.project.services.RssParser;
import rss.combinator.project.services.TagService;
import rss.combinator.project.services.LinkService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class ScheduledNewsTest {

    @Mock
    private RssParser rssParser;
    @Mock
    private TagService tagService;
    @Mock
    private LinkService linkService;
    private ScheduledNews scheduledNews;

    private Tag sport = Tag.builder().id(1L).name("sport").build();
    private Tag reddit = Tag.builder().id(2L).name("reddit").build();
    private Map<String, List<String>> map = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        this.scheduledNews = new ScheduledNews(rssParser, tagService, linkService);
        String linkSport = "http://rss.cnn.com/rss/edition_sport.rss";
        String linkReddit = "https://www.reddit.com/r/news+wtf.rss";
        map.put(sport.getName(), Collections.singletonList(linkSport));

        when(tagService.getAll()).thenReturn(Arrays.asList(sport, reddit));
        when(linkService.getAllByTag(sport))
                .thenReturn(Collections.singletonList(linkSport));
        when(linkService.getAllByTag(reddit))
                .thenReturn(Collections.singletonList(linkReddit));
    }

    @Test
    public void requestFreshNews() throws Exception {
         scheduledNews.requestFreshNews();
         verify(tagService, times(1)).getAll();
         verify(linkService, times(2)).getAllByTag(any(Tag.class));
         verify(rssParser, times(1)).parseRss(anyMap());
    }

    @Test
    public void deleteOldNews() throws Exception {
        scheduledNews.deleteOldNews();
        verify(tagService, times(1)).getAll();
        verify(rssParser, times(2)).deleteFile(anyString());
    }
}