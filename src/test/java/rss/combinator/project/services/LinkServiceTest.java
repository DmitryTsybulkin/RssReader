package rss.combinator.project.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.entities.Tag;

import java.util.List;

import static org.junit.Assert.*;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class LinkServiceTest {

    @Autowired
    private LinkService linkService;
    @Autowired
    private TagService tagService;

    @Test
    public void create() throws Exception {
        final Tag tag = tagService.create("name");
        linkService.create("url", tag);
        final List<String> allByTag = linkService.getAllByTag(tag);
        assertNotNull(allByTag);
        assertEquals(1, allByTag.size());
        assertTrue(allByTag.get(0).contains("url"));
    }

    @Test(expected = EntryDuplicateException.class)
    public void createFailedEntryDuplicateException() throws Exception {
        final Tag tag = tagService.create("name");
        linkService.create("url", tag);
        linkService.create("url", tag);
    }

    @Test
    public void getAllByTag() throws Exception {
        final Tag tag = tagService.create("name");
        linkService.create("url", tag);
        linkService.create("link", tag);
        linkService.create("address", tag);
        final List<String> allByTag = linkService.getAllByTag(tag);
        assertEquals(3, allByTag.size());
        assertTrue(allByTag.contains("url"));
        assertTrue(allByTag.contains("link"));
        assertTrue(allByTag.contains("address"));
    }

    @Test
    public void deleteByTag() throws Exception {
        final Tag tag = tagService.create("name");
        linkService.create("url", tag);
        linkService.create("link", tag);
        linkService.create("address", tag);

        linkService.deleteByTag(tag);

        final List<String> allByTag = linkService.getAllByTag(tag);
        assertEquals(0, allByTag.size());
        assertTrue(allByTag.isEmpty());
    }
}