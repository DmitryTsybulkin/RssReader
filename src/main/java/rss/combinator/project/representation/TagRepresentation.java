package rss.combinator.project.representation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rss.combinator.project.dto.TagDTO;
import rss.combinator.project.exceptions.FileStorageException;
import rss.combinator.project.model.Tag;
import rss.combinator.project.services.LinkService;
import rss.combinator.project.services.RssParser;
import rss.combinator.project.services.TagService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<String, List<String>> map = new HashMap<>();
        map.put(name, new ArrayList<>(links));
        rssParser.parseRss(map);
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
