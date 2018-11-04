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

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
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

    public void parseRss(Map<String, List<String>> entries) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        long start = System.currentTimeMillis();
        List<String> json = new ArrayList<>();
        entries.forEach((tag, links) -> {

            links.forEach(link -> {
                Callable<List<String>> callable = parseLink(link);
                if (callable == null) {
                    throw new RuntimeException("Callable is null");
                }
                try {

                    // TODO: 04.11.2018 придумать, что сделать с этим и чуть ниже костылями
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

        System.out.println("Использовано времени: " + (System.currentTimeMillis() - start) + " миллисекунд");

    }

    private synchronized void saveToFile(String name, List<String> json) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(getAbsolute() + name + pathSuffix))) {
            for (String entry : json)
                pw.println(entry);
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
    }

    public String getAbsolute() {
        return new File(pathPrefix).getAbsolutePath();
    }

    public Boolean deleteFile(String name) {
        try {
            return Files.deleteIfExists(Paths.get(getAbsolute() + name + pathSuffix));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized Callable<List<String>> parseLink(String link) {
        try {
            URL url = new URL(link);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            List<String> json = new ArrayList<>();

            json.add("[");
            json.addAll(feed.getEntries().stream()
                    .map(this::toPostDto)
                    .map(dto -> jsonFormatterService.toJson(dto) + ",")
                    .collect(Collectors.toList()));

            int lastElement = json.size() - 1;
            json.set(lastElement, json.get(lastElement).substring(0, json.get(lastElement).lastIndexOf(",")));
            json.add("]");

            return () -> json;
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return null;
    }

    public synchronized PostDTO toPostDto(SyndEntry entry) {
        return PostDTO.builder()
                .title(entry.getTitle())
                .date(entry.getPublishedDate() == null ? "unknown date" : entry.getPublishedDate().toString())
                .link(entry.getLink())
                .build();
    }

}
