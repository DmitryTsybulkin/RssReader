package rss.combinator.project.services;

import lombok.extern.slf4j.Slf4j;
import com.rometools.rome.io.XmlReader;
import com.rometools.rome.io.SyndFeedInput;
import rss.combinator.project.dto.PostDTO;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RssParser {

    @Value("${download.path.prefix}")
    private String pathPrefix;
    @Value("${download.path.suffix}")
    private String pathSuffix;
    private final JsonFormatterService jsonFormatterService;

    @Autowired
    public RssParser(JsonFormatterService jsonFormatterService) {
        this.jsonFormatterService = jsonFormatterService;
    }

    public void parseRss(final String link, final String tag) {
        try {
            URL feedUrl = new URL(link);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            List<String> toJson = new ArrayList<>();
            toJson.add("[");
            toJson.addAll(feed.getEntries().stream()
                    .map(this::toMessageDto)
                    .map(dto -> jsonFormatterService.toJson(dto) + ",")
                    .collect(Collectors.toList()));

            int lastElement = toJson.size() - 1;
            toJson.set(lastElement, toJson.get(lastElement).substring(0, toJson.get(lastElement).lastIndexOf(",")));
            toJson.add("]");

            save(toJson, tag);

        } catch (Exception e) {
            log.error("Error parsing rss/atom tape by link: " + link);
            e.getLocalizedMessage();
        }
    }

    public PostDTO toMessageDto(SyndEntry entry) {
        return PostDTO.builder()
                .title(entry.getTitle())
                .date(entry.getPublishedDate() == null ? "unknown date" : entry.getPublishedDate().toString())
                .link(entry.getLink())
                .build();
    }

    public synchronized void save(final List<String> toJson, final String filename) throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(pathPrefix + filename + pathSuffix))) {
            for (String entry : toJson)
                pw.println(entry);
        }
    }

}
