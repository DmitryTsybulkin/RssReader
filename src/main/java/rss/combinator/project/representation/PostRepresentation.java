package rss.combinator.project.representation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.services.JsonFormatterService;
import rss.combinator.project.services.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostRepresentation {

    private final JsonFormatterService jsonFormatterService;
    private String absolutePath = Utils.getAbsolute();

    @Autowired
    public PostRepresentation(JsonFormatterService jsonFormatterService) {
        this.jsonFormatterService = jsonFormatterService;
    }

    public List<PostDTO> getByTagAndFromDate(List<String> tags, LocalDateTime from) {
        List<PostDTO> result = getByTags(tags);
        return getFromDate(result, from);
    }

    public List<PostDTO> getAll() {
        List<PostDTO> result = new ArrayList<>();
        try {
            Files.list(Paths.get(absolutePath))
                    .collect(Collectors.toList())
                    .forEach(path ->
                            result.addAll(jsonFormatterService.fromJson(path.toFile())));
        } catch (IOException e) {
            e.getLocalizedMessage();
        }
        result.forEach(dto -> dto.setDate(Utils.formatDate(dto.getDate())));
        Collections.sort(result);
        return result;
    }

    public List<PostDTO> getByTags(List<String> tags) {
        List<PostDTO> result = new ArrayList<>();
        tags.forEach(name -> {
            try {
                Files.list(Paths.get(absolutePath))
                        .collect(Collectors.toList())
                        .forEach(path -> {
                            if (path.endsWith(name + ".json")) {
                                result.addAll(jsonFormatterService.fromJson(path.toFile()));
                            }
                        });
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        });
        result.forEach(dto -> dto.setDate(Utils.formatDate(dto.getDate())));
        Collections.sort(result);
        return result;
    }

    public List<PostDTO> getFromDate(List<PostDTO> dtos, LocalDateTime from) {
        List<PostDTO> result = new ArrayList<>();
        if (from != null) {
            dtos.forEach(postDTO -> {
                LocalDateTime date = LocalDateTime.parse(postDTO.getDate(), Utils.outDateFormat);
                if (date.isEqual(from) || date.isAfter(from)) {
                    result.add(postDTO);
                }
            });
        }
        return result;
    }

}
