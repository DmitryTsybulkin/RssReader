package rss.combinator.project.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import rss.combinator.project.dto.PostDTO;

import java.util.*;

@RestController
public class ReaderController {

    @GetMapping(value = "/tags")
    public Set<String> getTags() {
        return new HashSet<>();
    }

    @GetMapping(value = "/tape")
    public Flux<PostDTO> getPosts(@RequestParam(name = "from", required = false) String date,
                                  @RequestParam(name = "tag", required = false) Set<String> tags) {
        return Flux.fromIterable(new ArrayList<>());
    }

}
