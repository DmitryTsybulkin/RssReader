package rss.combinator.project.representation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rss.combinator.project.exceptions.FileStorageException;
import rss.combinator.project.model.Tag;
import rss.combinator.project.services.RssParser;
import rss.combinator.project.services.TagService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagRepresentation {

    private final TagService tagService;
    private final RssParser rssParser;

    @Autowired
    public TagRepresentation(TagService tagService, RssParser rssParser) {
        this.tagService = tagService;
        this.rssParser = rssParser;
    }

    public void createTag(String name, Set<String> links) {
        tagService.create(name);
        createTape(name, links);
    }

    public void createTape(String name, Set<String> links) {
        if (links != null && !links.isEmpty()) {
            Map<String, List<String>> map = new HashMap<>();
            map.put(name, new ArrayList<>(links));
            rssParser.parseRss(map);
        }
    }

    public Set<String> getAllTags() {
        return tagService.getAll()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
    }

    public void deleteTape(String name) {
        tagService.delete(name);
        Boolean deleted = rssParser.deleteFile(name);
        if (!deleted) {
            throw new FileStorageException("Error deleting file with name: " + name);
        }
    }

}
