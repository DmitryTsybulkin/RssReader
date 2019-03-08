package rss.combinator.project.representation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.services.JsonParserService;
import rss.combinator.project.services.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostRepresentation {

    private final JsonParserService jsonParserService;
    private String absolutePath = Utils.getAbsolute();

    @Autowired
    public PostRepresentation(JsonParserService jsonParserService) {
        this.jsonParserService = jsonParserService;
    }

    public List<PostDTO> getByTagAndFromDate(List<String> tags, LocalDateTime from) {
        return getFromDate(getByTags(tags), from);
    }

    public List<PostDTO> getAllFromDate(LocalDateTime from) {
        return getFromDate(getAll(), from);
    }

    public List<PostDTO> getAll() {
        List<PostDTO> result = new ArrayList<>();
        try {
            Files.list(Paths.get(absolutePath))
                    .collect(Collectors.toList())
                    .forEach(path -> result.addAll(jsonParserService.fromJson(path.toFile())));
        } catch (IOException e) {
            log.error("Get all posts failed: " + e.getLocalizedMessage());
        }
        return sorted(result);
    }

    private List<PostDTO> sorted(List<PostDTO> result) {
        result.forEach(dto -> dto.setDate(Utils.formatDate(dto.getDate())));
        Collections.sort(result);
        Collections.reverse(result);
        return result;
    }

    public List<PostDTO> getByTags(List<String> tags) {
        List<PostDTO> result = new ArrayList<>();
        tags.forEach(name ->
                result.addAll(jsonParserService.fromJson(Paths.get(absolutePath + name + ".json").toFile())));
        return sorted(result);
    }

    public List<PostDTO> getFromDate(List<PostDTO> dtos, LocalDateTime from) {
        if (Objects.nonNull(from)) {
            return dtos.stream()
                    .filter(postDTO -> {
                        LocalDateTime date = LocalDateTime.parse(postDTO.getDate(), Utils.outDateFormat);
                        return date.isEqual(from) || date.isAfter(from);
                    }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
