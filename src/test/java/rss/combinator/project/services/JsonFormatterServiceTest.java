package rss.combinator.project.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rss.combinator.project.dto.PostDTO;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JsonFormatterServiceTest {

    @Autowired
    private JsonFormatterService jsonFormatterService;

    private final String targetJson = "{\"title\":\"TITLE\"," +
                                        "\"date\":\"2018-11-01\"," +
                                        "\"link\":\"http://localhost:8080/best-link\"}";

    private final PostDTO dto = PostDTO.builder()
            .title("TITLE")
            .date("2018-11-01")
            .link("http://localhost:8080/best-link")
            .build();

    @Test
    public void toJson() throws Exception {
        String json = jsonFormatterService.toJson(dto);
        assertEquals(targetJson, json);
    }

    @Test
    public void fromJson() throws Exception {
        File file = new File(Utils.getAbsolute() + "test.json");
        PrintWriter pw = new PrintWriter(file);
        pw.println("[" + targetJson + "]");
        pw.close();
        PostDTO resultDTO = jsonFormatterService.fromJson(file).get(0);
        assertEquals(dto.getTitle(), resultDTO.getTitle());
        assertEquals(dto.getDate(), resultDTO.getDate());
        assertEquals(dto.getLink(), resultDTO.getLink());
        Files.deleteIfExists(file.toPath());
    }
}