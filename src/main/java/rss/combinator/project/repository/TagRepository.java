package rss.combinator.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rss.combinator.project.model.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByName(String name);
}
