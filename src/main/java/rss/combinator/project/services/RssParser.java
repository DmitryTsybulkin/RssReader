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
        ExecutorService executorService = Executors.newFixedThreadPool(cores - 1);

        List<String> json = new ArrayList<>();
        entries.forEach((tag, links) -> {

            links.forEach(link -> {
                Callable<List<String>> callable = parseLink(link);
                if (callable == null) {
                    throw new ParseRssException("Error parsing rss by link: " + link);
                }
                try {

                    if (links.size() > 1 && links.indexOf(link) != 0 && links.indexOf(link) == links.size() - 1) {
                        final String s = json.get(json.size() - 1);
                        json.set(json.size() - 1, s.replace("]", ","));
                    }

                    List<String> list = executorService.submit(callable).get();

                    if (links.size() > 1 && links.indexOf(link) == links.size() - 1) {
                        list.remove(0);
                    }

                    json.addAll(list);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
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
            e.getLocalizedMessage();
        }
    }

    public Boolean deleteFile(String name) {
        try {
            return Files.deleteIfExists(Paths.get(absolutePath + name + pathSuffix));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized Callable<List<String>> parseLink(String link) {
        try {
            URL url = new URL(link);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            List<String> json = new ArrayList<>();

            json.add("[");
            json.addAll(feed.getEntries().stream()
                    .map(this::toPostDto)
                    .map(dto -> jsonParserService.toJson(dto) + ",")
                    .collect(Collectors.toList()));

            int lastElement = json.size() - 1;
            json.set(lastElement, json.get(lastElement).substring(0, json.get(lastElement).lastIndexOf(",")));
            json.add("]");

            return () -> json;
        } catch (IOException | FeedException e) {
            e.getLocalizedMessage();
        }
        return null;
    }

    public synchronized PostDTO toPostDto(SyndEntry entry) {
        return PostDTO.builder()
                .title(entry.getTitle())
                .date(entry.getPublishedDate() == null ?
                        entry.getUpdatedDate().toString() : entry.getPublishedDate().toString())
                .link(entry.getLink())
                .build();
    }

}
