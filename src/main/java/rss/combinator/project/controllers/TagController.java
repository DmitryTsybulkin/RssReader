package rss.combinator.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rss.combinator.project.dto.TagDTO;
import rss.combinator.project.representation.TagRepresentation;

import java.util.List;

@RestController
public class TagController {

    private final TagRepresentation tagRepresentation;

    @Autowired
    public TagController(TagRepresentation tagRepresentation) {
        this.tagRepresentation = tagRepresentation;
    }

    @GetMapping("/tags")
    public List<TagDTO> getTags() {
        return tagRepresentation.getAllTags();
    }

    @PostMapping("/tags/new")
    public void createTag(@RequestParam(value = "name") String name,
                          @RequestParam(value = "links") List<String> links) {
        tagRepresentation.createTag(name, links);
    }

    @PatchMapping("/tags/{id}")
    public void updateTag(@PathVariable("id") Long id,
                          @RequestParam(value = "name") String name) {
        tagRepresentation.updateTag(id, name);
    }

    @DeleteMapping("/tags/{name}")
    public void deleteTag(@PathVariable("name") String name) {
        tagRepresentation.deleteTag(name);
    }

}
