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
    public void create(String name) {
        if (tagRepository.findTagByName(name).isPresent()) {
            throw new EntryDuplicateException("Tag with name " + name + " already exists");
        }
        tagRepository.save(new Tag(name));
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
    public void delete(String name) {
        Tag tag = tagRepository.findTagByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Tag by name: " + name + " not found"));
        tagRepository.delete(tag);
    }

}
