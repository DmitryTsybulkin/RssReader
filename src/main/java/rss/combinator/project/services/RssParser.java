package rss.combinator.project.services;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rss.combinator.project.dto.PostDTO;
import rss.combinator.project.exceptions.ParseRssException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RssParser {

    @Value("${download.path.suffix}")
    private String pathSuffix;
    private String absolutePath = Utils.getAbsolute();
    private final JsonParserService jsonParserService;
    private final int cores = Runtime.getRuntime().availableProcessors();

    @Autowired
    public RssParser(JsonParserService jsonParserService) {
        this.jsonParserService = jsonParserService;
    }

    public void parseRss(Map<String, List<String>> entries) {
        ExecutorService executorService = Executors.newFixedThreadPool((int) (cores / (1 - 0.25)));

        List<String> json = new ArrayList<>();
        entries.forEach((tag, links) -> {

            links.forEach(link -> {
                Callable<String> callable = parseLink(link);
                if (callable == null) {
                    throw new ParseRssException("Error parsing rss by link: " + link);
                }
                try {

                    if (links.size() > 1 && links.indexOf(link) != 0 && links.indexOf(link) == links.size() - 1) {
                        final String s = json.get(json.size() - 1);
                        json.set(json.size() - 1, s.replace("]", ","));
                    }

                    String result = executorService.submit(callable).get();

                    if (links.size() > 1 && links.indexOf(link) == links.size() - 1) {
                        result = result.substring(1);
                    }

                    json.add(result);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error parsing rss feed: " + e.getLocalizedMessage());
                }
            });
            saveToFile(tag, json);
            json.clear();
        });
    }

    public synchronized void saveToFile(String name, List<String> json) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(absolutePath + name + pathSuffix))) {
            for (String entry : json) {
                pw.println(entry);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    public Boolean deleteFile(String name) {
        try {
            return Files.deleteIfExists(Paths.get(absolutePath + name + pathSuffix));
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return false;
    }

    public synchronized Callable<String> parseLink(String link) {
        try {
            URL url = new URL(link);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));
            List<PostDTO> json = feed.getEntries()
                    .stream()
                    .filter(entry -> entry.getTitle() != null &&
                            entry.getLink() != null &&
                            (entry.getPublishedDate() != null || entry.getUpdatedDate() != null))
                    .map(this::toPostDto)
                    .collect(Collectors.toList());

            return () -> jsonParserService.toJson(json);
        } catch (IOException | FeedException e) {
            log.error("Error parsing link: " + link + "\n" + e.getLocalizedMessage());
        }
        return null;
    }

    public synchronized PostDTO toPostDto(SyndEntry entry) {
        String date;
        if (entry.getPublishedDate() != null) {
            date = entry.getPublishedDate().toString();
        } else {
            date = entry.getUpdatedDate().toString();
        }
        return PostDTO.builder()
                .title(entry.getTitle())
                .date(date)
                .link(entry.getLink())
                .build();
    }

}
