package rss.combinator.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.representation.PostRepresentation;

import java.time.LocalDateTime;
import java.util.Set;

@RestController("/posts")
public class PostController {

    private final PostRepresentation postRepresentation;

    @Autowired
    public PostController(PostRepresentation postRepresentation) {
        this.postRepresentation = postRepresentation;
    }

    @GetMapping
    public Flux<PostDTO> getPosts(@RequestParam(name = "from", required = false) String date,
                                  @RequestParam(name = "tag", required = false) Set<String> tags) {
        if (date != null && tags != null && !tags.isEmpty()) {
            return Flux.fromIterable(postRepresentation.getByTagAndFromDate(tags, LocalDateTime.parse(date)));
        } else if (date == null && tags != null && !tags.isEmpty()) {
            return Flux.fromIterable(postRepresentation.getByTags(tags));
        } else {
            return Flux.fromIterable(postRepresentation.getAll());
        }
    }

}
