package rss.combinator.project.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rss.combinator.project.entities.Tag;
import rss.combinator.project.services.LinkService;
import rss.combinator.project.services.RssParser;
import rss.combinator.project.services.TagService;

import java.util.stream.Collectors;

@Slf4j
@Component
public class ScheduledNews {

    private final RssParser rssParser;
    private final TagService tagService;
    private final LinkService linkService;

    @Autowired
    public ScheduledNews(RssParser rssParser, TagService tagService, LinkService linkService) {
        this.rssParser = rssParser;
        this.tagService = tagService;
        this.linkService = linkService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void requestFreshNews() {
        rssParser.parseRss(tagService.getAll().stream()
                .collect(Collectors.toMap(Tag::getName, linkService::getAllByTag)));
        log.info("Loaded fresh news");
    }

    @Scheduled(cron = "0 0 1 */2 * *")
    public void deleteOldNews() {
        tagService.getAll().forEach(tag -> rssParser.deleteFile(tag.getName()));
    }

}
