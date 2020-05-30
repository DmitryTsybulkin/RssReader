package rss.combinator.project.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.entities.Tag;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@SpringBootTest
@RunWith(SpringRunner.class)
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    private Tag tag;

    @Before
    public void setUp() throws Exception {
        tag = tagRepository.save(Tag.builder().name("sport").build());
    }

    @After
    public void tearDown() throws Exception {
        tagRepository.delete(tag);
    }

    @Test
    public void save() throws Exception {
        Tag newTag = tagRepository.save(Tag.builder().name("life").build());
        assertNotNull(newTag);
        assertNotNull(newTag.getId());
        assertTrue(tagRepository.existsById(newTag.getId()));
        assertTrue(tagRepository.findAll().stream().anyMatch(item -> item.getName().equals("life")));
        tagRepository.delete(newTag);
    }

    @Test
    public void findTagByName() throws Exception {
        final Optional<Tag> sport = tagRepository.findTagByName("sport");
        assertTrue(sport.isPresent());
        assertNotNull(sport.get().getId());
        assertEquals(tag.getId(), sport.get().getId());
    }

    @Test
    public void findAll() throws Exception {
        final List<Tag> all = tagRepository.findAll();
        assertNotNull(all);
        assertTrue(all.stream().anyMatch(item -> item.getName().equals("sport")));
    }

    @Test
    public void findById() throws Exception {
        final Optional<Tag> sport = tagRepository.findById(tag.getId());
        assertTrue(sport.isPresent());
        assertNotNull(sport.get().getName());
        assertEquals(sport.get().getName(), "sport");
    }

    @Test
    public void delete() throws Exception {
        assertTrue(tagRepository.findTagByName("sport").isPresent());
        tagRepository.delete(tag);
        assertFalse(tagRepository.findTagByName("sport").isPresent());
        assertFalse(tagRepository.existsById(tag.getId()));
    }
}