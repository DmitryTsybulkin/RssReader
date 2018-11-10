package rss.combinator.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.exceptions.ResourceNotFoundException;
import rss.combinator.project.model.Tag;
import rss.combinator.project.repository.TagRepository;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Tag create(String name) {
        if (tagRepository.findTagByName(name).isPresent()) {
            throw new EntryDuplicateException("Tag with name " + name + " already exists");
        }
        return tagRepository.save(new Tag(name));
    }

    @Transactional(readOnly = true)
    public Tag getByName(String name) {
        return tagRepository.findTagByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Tag by name: " + name + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Transactional
    public void update(Long id, String name) {
        final Tag tag = tagRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tag by name: " + name + " not found"));
        tag.setName(name);
    }

    @Transactional
    public void delete(String name) {
        Tag tag = getByName(name);
        tagRepository.delete(tag);
    }

}
