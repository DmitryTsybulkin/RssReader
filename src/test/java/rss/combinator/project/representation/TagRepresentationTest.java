package rss.combinator.project.representation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.dto.TagDTO;
import rss.combinator.project.exceptions.FileStorageException;
import rss.combinator.project.entities.Tag;
import rss.combinator.project.repository.TagRepository;
import rss.combinator.project.services.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class TagRepresentationTest {

    @Autowired
    private TagRepresentation tagRepresentation;
    @Autowired
    private TagRepository tagRepository;

    private Path path;
    private List<String> links = new ArrayList<>();

    private final String tagName = "sport";

    @Before
    public void setUp() throws Exception {
        path = Paths.get(Utils.getAbsolute() + "sport.json");

        Files.deleteIfExists(path);
        assertTrue(Files.notExists(path));

        links.add("https://www.sports.ru/rss/rubric.xml?s=208");
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(path);
    }

    @Test
    public void createTag() throws Exception {
        tagRepresentation.createTag(tagName, links);
        assertTrue(Files.exists(path));
        assertTrue(tagRepository.findTagByName(tagName).isPresent());
        assertTrue(tagRepository.findAll().stream().anyMatch(item -> tagName.equals(item.getName())));
    }

    @Test
    public void getAllTags() throws Exception {
        final List<TagDTO> allTags = tagRepresentation.getAllTags();
        assertNotNull(allTags);
        assertFalse(allTags.isEmpty());
        assertTrue(allTags.stream().anyMatch(item -> item.getName().equals("business")));
        assertTrue(allTags.stream().anyMatch(item -> item.getName().equals("science")));
    }

    @Test
    public void updateTag() throws Exception {
        Tag tag = tagRepository.findTagByName("business").get();
        assertNotNull(tag);
        assertNotNull(tag.getId());
        assertNotNull(tag.getLinks());
        assertFalse(tag.getLinks().isEmpty());
        tagRepresentation.updateTag(tag.getId(), "economics");
        assertFalse(tagRepository.findTagByName("business").isPresent());
        assertTrue(tagRepository.existsById(tag.getId()));
        assertNotNull(tagRepository.findTagByName("economics"));
    }

    @Test
    public void deleteTag() throws Exception {
        tagRepresentation.createTag(tagName, links);
        Tag tag = tagRepository.findTagByName(tagName).get();
        assertNotNull(tag);
        assertTrue(Files.exists(path));
        tagRepresentation.deleteTag(tagName);
        assertFalse(tagRepository.findTagByName(tagName).isPresent());
        assertTrue(Files.notExists(path));
    }

    @Test(expected = FileStorageException.class)
    public void errorDeletingFile() throws Exception {
        tagRepresentation.createTag(tagName, links);
        Tag tag = tagRepository.findTagByName(tagName).get();
        assertNotNull(tag);
        assertTrue(Files.exists(path));
        Files.deleteIfExists(path);
        tagRepresentation.deleteTag(tagName);
    }

    @Test
    public void toDto() throws Exception {
        Tag tag = Tag.builder().id(1L).name("best_tag").build();
        final TagDTO tagDTO = tagRepresentation.toDto(tag);
        assertNotNull(tagDTO);
        assertNotNull(tagDTO.getId());
        assertNotNull(tagDTO.getName());
        assertEquals(tagDTO.getId(), tag.getId());
        assertEquals(tagDTO.getName(), tag.getName());
    }

    @Test
    public void fromDto() throws Exception {
        TagDTO dto = TagDTO.builder().id(1L).name("name").build();
        Tag tag = tagRepresentation.fromDto(dto);
        assertNotNull(tag);
        assertNotNull(tag.getId());
        assertNotNull(tag.getName());
        assertEquals(tag.getId(), dto.getId());
        assertEquals(tag.getName(), dto.getName());
    }
}