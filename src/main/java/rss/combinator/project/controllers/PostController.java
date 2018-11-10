package rss.combinator.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.representation.PostRepresentation;
import rss.combinator.project.services.Utils;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class PostController {

    private final PostRepresentation postRepresentation;

    @Autowired
    public PostController(PostRepresentation postRepresentation) {
        this.postRepresentation = postRepresentation;
    }

    @GetMapping(value = "/posts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PostDTO> getPosts(@RequestParam(name = "from", required = false) String date,
                                  @RequestParam(name = "tag", required = false) List<String> tags) {
        if (date != null && tags != null && !tags.isEmpty()) {
            return Flux.fromIterable(postRepresentation
                    .getByTagAndFromDate(tags, LocalDateTime.parse(date, Utils.outDateFormat)));
        } else if (date == null && tags != null && !tags.isEmpty()) {
            return Flux.fromIterable(postRepresentation.getByTags(tags));
        } else if (date != null) {
            return Flux.fromIterable(postRepresentation
                    .getAllFromDate(LocalDateTime.parse(date, Utils.outDateFormat)));
        } else {
            return Flux.fromIterable(postRepresentation.getAll());
        }
    }

}
