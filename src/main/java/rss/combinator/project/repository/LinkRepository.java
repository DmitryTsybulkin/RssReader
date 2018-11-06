package rss.combinator.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rss.combinator.project.model.Link;
import rss.combinator.project.model.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrlAndTag(String url, Tag tag);

    List<Link> findAllByTag(Tag tag);

    void deleteAllByTag(Tag tag);
}
