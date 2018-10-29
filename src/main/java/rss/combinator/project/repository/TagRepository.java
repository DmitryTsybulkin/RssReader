package rss.combinator.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rss.combinator.project.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
