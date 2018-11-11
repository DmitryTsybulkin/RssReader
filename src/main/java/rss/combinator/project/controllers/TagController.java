package rss.combinator.project.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rss.combinator.project.dto.TagDTO;
import rss.combinator.project.representation.TagRepresentation;

import java.util.List;

@RestController
@Api(description = "Manage tags of news")
public class TagController {

    private final TagRepresentation tagRepresentation;

    @Autowired
    public TagController(TagRepresentation tagRepresentation) {
        this.tagRepresentation = tagRepresentation;
    }

    @GetMapping("/tags")
    @ApiOperation(value = "Get all tags", httpMethod = "GET", produces = "application/json")
    public List<TagDTO> getTags() {
        return tagRepresentation.getAllTags();
    }

    @PostMapping("/tags/new")
    @ApiOperation(value = "Create new tag", httpMethod = "POST")
    public void createTag(@RequestParam(value = "name") String name,
                          @RequestParam(value = "links") List<String> links) {
        tagRepresentation.createTag(name, links);
    }

    @PatchMapping("/tags/{id}")
    @ApiOperation(value = "Update name of existing tag by id", httpMethod = "PATCH")
    public void updateTag(@PathVariable("id") Long id,
                          @RequestParam(value = "name") String name) {
        tagRepresentation.updateTag(id, name);
    }

    @DeleteMapping("/tags/{name}")
    @ApiOperation(value = "Delete existing tag and its links", httpMethod = "DELETE")
    public void deleteTag(@PathVariable("name") String name) {
        tagRepresentation.deleteTag(name);
    }

}
