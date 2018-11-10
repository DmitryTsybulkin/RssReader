package rss.combinator.project.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.exceptions.ResourceNotFoundException;
import rss.combinator.project.model.Tag;
import rss.combinator.project.repository.TagRepository;

import java.util.List;

import static org.junit.Assert.*;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class TagServiceTest {

    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepository;

    @Before
    public void setUp() throws Exception {
        tagRepository.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        tagRepository.deleteAll();
    }

    @Test
    public void createAndGetByName() throws Exception {
        tagService.create("newTag");
        final Tag tag = tagService.getByName("newTag");
        assertNotNull(tag);
        assertNotNull(tag.getId());
        assertNotNull(tag.getName());
        assertEquals("newTag", tag.getName());
    }

    @Test(expected = EntryDuplicateException.class)
    public void createFailedEntryDuplicateException() throws Exception {
        tagService.create("newTag");
        tagService.create("newTag");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getByNameFailedResourceNotFoundException() throws Exception {
        tagService.getByName("newTag");
    }

    @Test
    public void getAll() throws Exception {
        final String news = "news";
        final String sport = "sport";
        final String travel = "travel";
        tagService.create(news);
        tagService.create(sport);
        tagService.create(travel);

        final List<Tag> all = tagService.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertEquals(3, all.size());
        assertEquals(all.get(0).getName(), news);
        assertEquals(all.get(1).getName(), sport);
        assertEquals(all.get(2).getName(), travel);
    }

    @Test
    public void update() throws Exception {
        final String travel = "travel";
        final String newName = "sport";
        tagService.create(travel);
        final Tag tag = tagService.getByName(travel);
        assertNotNull(tag);
        tagService.update(tag.getId(), newName);
        final Tag result = tagService.getByName(newName);
        assertNotNull(result);
    }

    @Test
    public void delete() throws Exception {
        final String name = "tag";
        tagService.create(name);
        assertNotNull(tagService.getByName(name));
        tagService.delete(name);
        assertFalse(tagRepository.findTagByName(name).isPresent());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteFailedResourceNotFoundException() throws Exception {
        tagService.delete("bad-name");
    }
}