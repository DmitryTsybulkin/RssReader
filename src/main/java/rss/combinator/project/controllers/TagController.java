package rss.combinator.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rss.combinator.project.representation.TagRepresentation;

import java.util.Set;

@RestController("/tags")
public class TagController {

    private final TagRepresentation tagRepresentation;

    @Autowired
    public TagController(TagRepresentation tagRepresentation) {
        this.tagRepresentation = tagRepresentation;
    }

    @GetMapping
    public Set<String> getTags() {
        return tagRepresentation.getAllTags();
    }

    @PostMapping
    public Set<String> createTag(@RequestParam(value = "name") String name,
                                 @RequestParam(value = "links") Set<String> links) {
        return tagRepresentation.createTag(name, links);
    }

    @DeleteMapping("/{name}")
    public void deleteTag(@PathVariable("name") String name) {
        tagRepresentation.deleteTag(name);
    }

}
