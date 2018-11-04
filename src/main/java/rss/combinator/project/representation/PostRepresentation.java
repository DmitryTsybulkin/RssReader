package rss.combinator.project.representation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.services.JsonFormatterService;
import rss.combinator.project.services.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostRepresentation {

    private final JsonFormatterService jsonFormatterService;
    private String absolutePath = Utils.getAbsolute();

    @Autowired
    public PostRepresentation(JsonFormatterService jsonFormatterService) {
        this.jsonFormatterService = jsonFormatterService;
    }

    public List<PostDTO> getAll() {
        List<PostDTO> dtos = new ArrayList<>();
        try {
            Files.list(Paths.get(absolutePath))
                    .collect(Collectors.toList())
                    .forEach(path ->
                            dtos.addAll(jsonFormatterService.fromJson(path.toFile())));
        } catch (IOException e) {
            e.getLocalizedMessage();
        }
        return dtos;
    }

    public List<PostDTO> getByTags(Set<String> tags) {
        return new ArrayList<>();
    }

    public List<PostDTO> getByTagAndFromDate(Set<String> tags, LocalDateTime from) {
        return new ArrayList<>();
    }

    public List<PostDTO> sortByChronology(List<PostDTO> dtos, LocalDateTime from) {
        Collections.sort(dtos);
        List<PostDTO> result = new ArrayList<>();
        if (from != null) {
            dtos.forEach(postDTO -> {
                LocalDateTime date = LocalDateTime.parse(postDTO.getDate(), Utils.dateFormat);
                if (date.isEqual(from) || date.isAfter(from)) {
                    result.add(postDTO);
                }
            });
        }
        return result;
    }

}
