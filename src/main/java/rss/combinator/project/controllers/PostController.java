package rss.combinator.project.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(description = "Get existing posts")
public class PostController {

    private final PostRepresentation postRepresentation;

    @Autowired
    public PostController(PostRepresentation postRepresentation) {
        this.postRepresentation = postRepresentation;
    }

    @GetMapping(value = "/posts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation(value = "Get all posts or by tags or by date ", notes = "No params: get all posts;\n" +
            "Param: from - return posts by tags;\n" +
            "Param: tag - return all posts by date;\n" +
            "Params: all - return posts by tags and by date;\n", httpMethod = "GET", produces = "text/event-stream")
    public Flux<PostDTO> getPosts(@RequestParam(name = "from", required = false)
                                      @ApiParam(value = "Date from which need news, format: \"HH:mm:ss dd-MM-yyyy\".")
                                              String date,
                                  @RequestParam(name = "tags", required = false)
                                      @ApiParam(value = "Array of strings, where strings are names of tags")
                                              List<String> tags) {
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
