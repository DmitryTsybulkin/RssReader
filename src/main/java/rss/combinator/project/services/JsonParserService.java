package rss.combinator.project.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rss.combinator.project.dto.PostDTO;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class JsonParserService {

    private final ObjectMapper mapper = new ObjectMapper();

    public String toJson(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("Error convert object to json format: " + e.getLocalizedMessage());
        }
        return null;
    }

    public List<PostDTO> fromJson(final File file) {
        try {
            return mapper.readValue(file, new TypeReference<List<PostDTO>>(){});
        } catch (IOException e) {
            log.error("Error parsing json file: " + e.getLocalizedMessage());
        }
        return null;
    }

}
