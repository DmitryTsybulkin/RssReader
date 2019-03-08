package rss.combinator.project.representation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rss.combinator.project.dto.TagDTO;
import rss.combinator.project.exceptions.FileStorageException;
import rss.combinator.project.entities.Tag;
import rss.combinator.project.services.LinkService;
import rss.combinator.project.services.RssParser;
import rss.combinator.project.services.TagService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TagRepresentation {

    private final TagService tagService;
    private final LinkService linkService;
    private final RssParser rssParser;

    @Autowired
    public TagRepresentation(TagService tagService, LinkService linkService, RssParser rssParser) {
        this.tagService = tagService;
        this.linkService = linkService;
        this.rssParser = rssParser;
    }

    public void createTag(String name, List<String> links) {
        rssParser.parseRss(Collections.singletonMap(name, links));
        Tag tag = tagService.create(name);
        links.forEach(url -> linkService.create(url, tag));
    }

    public List<TagDTO> getAllTags() {
        return tagService.getAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void updateTag(Long id, String name) {
        tagService.update(id, name);
    }

    public void deleteTag(String name) {
        linkService.deleteByTag(tagService.getByName(name));
        tagService.delete(name);
        Boolean deleted = rssParser.deleteFile(name);
        if (!deleted) {
            throw new FileStorageException("Error deleting file with name: " + name);
        }
    }

    public TagDTO toDto(Tag tag) {
        return TagDTO.builder().id(tag.getId()).name(tag.getName()).build();
    }

    public Tag fromDto(TagDTO dto) {
        return Tag.builder().id(dto.getId()).name(dto.getName()).build();
    }

}
