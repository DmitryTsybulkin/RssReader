package rss.combinator.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.exceptions.ResourceNotFoundException;
import rss.combinator.project.model.Tag;
import rss.combinator.project.repository.TagRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Tag create(Tag tag) {
        if (tagRepository.findTagByName(tag.getName()).isPresent()) {
            throw new EntryDuplicateException("Tag with name " + tag.getName() + " already exists");
        }
        return tagRepository.save(new Tag(tag.getName()));
    }

    @Transactional(readOnly = true)
    public Tag getById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag by id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Set<Tag> getAll() {
        return new HashSet<>(tagRepository.findAll());
    }

    @Transactional
    public void delete(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag by id: " + id + " not found"));
        tagRepository.delete(tag);
    }

}
