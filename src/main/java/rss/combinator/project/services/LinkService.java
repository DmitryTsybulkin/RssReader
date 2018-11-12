package rss.combinator.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.model.Link;
import rss.combinator.project.model.Tag;
import rss.combinator.project.repository.LinkRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LinkService {

    private final LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Transactional
    public void create(String url, Tag tag) {
        if (linkRepository.findByUrlAndTag(url, tag).isPresent()) {
            throw new EntryDuplicateException("link by url: " + url + " already exists");
        }
        linkRepository.save(new Link(url, tag));
    }

    @Transactional(readOnly = true)
    public List<String> getAllByTag(Tag tag) {
        return linkRepository.findAllByTag(tag).stream().map(Link::getUrl).collect(Collectors.toList());
    }

    @Transactional
    public void deleteByTag(Tag tag) {
        linkRepository.deleteAllByTag(tag);
    }
}
