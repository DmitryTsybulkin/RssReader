package rss.combinator.project.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import rss.combinator.project.model.Link;
import rss.combinator.project.model.Tag;
import rss.combinator.project.repository.LinkRepository;
import rss.combinator.project.repository.TagRepository;
import rss.combinator.project.services.RssParser;
import rss.combinator.project.services.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Component
public class ApplicationInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final TagRepository tagRepository;
    private final LinkRepository linkRepository;
    private final RssParser rssParser;

    @Autowired
    public ApplicationInitializer(TagRepository tagRepository, LinkRepository linkRepository, RssParser rssParser) {
        this.tagRepository = tagRepository;
        this.linkRepository = linkRepository;
        this.rssParser = rssParser;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Initializing application...");

        String business = "http://feeds.reuters.com/reuters/businessNews";
        String science = "http://feeds.reuters.com/reuters/scienceNews";
        String space = "http://rss.cnn.com/rss/edition_space.rss";

        String businessTagName = "business";
        String scienceTagName = "science";

        try {
            Tag usualTag = tagRepository.save(Tag.builder().name(businessTagName).build());
            Tag scienceTag = tagRepository.save(Tag.builder().name(scienceTagName).build());

            Link businessLink = linkRepository.save(Link.builder().url(business).tag(usualTag).build());
            Link scienceLink = linkRepository.save(Link.builder().url(science).tag(scienceTag).build());
            Link spaceLink = linkRepository.save(Link.builder().url(space).tag(scienceTag).build());

            usualTag.setLinks(Collections.singletonList(businessLink));
            scienceTag.setLinks(Arrays.asList(scienceLink, spaceLink));
            log.info("Database was successfully initialized");
            if (Files.notExists(Paths.get(Utils.getAbsolute()))) {
                Files.createDirectory(Paths.get(Utils.getAbsolute()));
            }
            Map<String, List<String>> map = new HashMap<>();
            map.put(businessTagName, Collections.singletonList(business));
            map.put(scienceTagName, Arrays.asList(space, science));
            rssParser.parseRss(map);
            log.info("Files were successfully saved");
        } catch (RuntimeException | IOException e) {
            log.error("Application initializing failed: " + e.getLocalizedMessage());
        }
    }
}
