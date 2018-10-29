package rss.combinator.project.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.dto.TagDTO;

import java.util.HashSet;
import java.util.Set;

@RestController
public class ReaderController {

    @GetMapping(value = "/all")
    public Flux<PostDTO> getAllPosts() {
        return null;
    }

    @GetMapping(value = "/tags")
    public Set<TagDTO> getTags() {
        return new HashSet<>();
    }

    @GetMapping(value = "/tape")
    public Flux<PostDTO> getPosts(@RequestParam(name = "tag", required = true) Set<String> tags) {
        return null;
    }

}
