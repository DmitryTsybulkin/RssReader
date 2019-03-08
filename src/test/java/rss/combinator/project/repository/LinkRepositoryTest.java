package rss.combinator.project.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.entities.Link;
import rss.combinator.project.entities.Tag;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@SpringBootTest
@RunWith(SpringRunner.class)
public class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private TagRepository tagRepository;

    private Tag tag;
    private Link link;

    @Before
    public void setUp() throws Exception {
        tag = tagRepository.save(Tag.builder().name("life").build());
        link = linkRepository.save(Link.builder().url("test-url").tag(tag).build());
    }

    @After
    public void tearDown() throws Exception {
        linkRepository.delete(link);
        tagRepository.delete(tag);
    }

    @Test
    public void save() throws Exception {
        Link newLink = linkRepository.save(Link.builder().tag(tag).url("some_url").build());
        assertNotNull(newLink);
        assertNotNull(newLink.getId());
        assertTrue(linkRepository.existsById(newLink.getId()));
        assertTrue(linkRepository.findAll().stream().anyMatch(item -> item.getUrl().equals(newLink.getUrl())));
        linkRepository.delete(newLink);
    }

    @Test
    public void findByUrlAndTag() throws Exception {
        final Optional<Link> byUrlAndTag = linkRepository.findByUrlAndTag(link.getUrl(), tag);
        assertTrue(byUrlAndTag.isPresent());
        assertNotNull(byUrlAndTag.get().getId());
        assertTrue(linkRepository.existsById(byUrlAndTag.get().getId()));
    }

    @Test
    public void findAllByTag() throws Exception {
        final List<Link> allByTag = linkRepository.findAllByTag(tag);
        assertNotNull(allByTag);
        assertTrue(allByTag.stream().anyMatch(item -> item.getUrl().equals(link.getUrl())));
    }

    @Test
    public void deleteAllByTag() throws Exception {
        assertTrue(linkRepository.existsById(link.getId()));
        linkRepository.deleteAllByTag(tag);
        assertFalse(linkRepository.existsById(link.getId()));
        assertFalse(linkRepository.findAll().stream().anyMatch(item -> item.getUrl().equals(link.getUrl())));
    }
}