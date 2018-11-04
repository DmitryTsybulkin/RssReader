package rss.combinator.project.representation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.services.JsonFormatterService;

import java.time.LocalDate;
import java.util.*;

@Service
public class PostRepresentation {

    @Value("${download.path.prefix}")
    private String pathPrefix;

    private final JsonFormatterService jsonFormatterService;

    @Autowired
    public PostRepresentation(JsonFormatterService jsonFormatterService) {
        this.jsonFormatterService = jsonFormatterService;
    }

    public List<PostDTO> getByTags(Set<String> tags) {
        return new ArrayList<>();
    }

    public List<PostDTO> getByTagAndFromDate(Set<String> tags, LocalDate from) {
        return new ArrayList<>();
    }

    public List<PostDTO> sortByChronology(List<PostDTO> dtos, LocalDate from) {
        // read
        if (from != null) {
            // get from Date
        }
        return new ArrayList<>();
    }

}
