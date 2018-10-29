package rss.combinator.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rss.combinator.project.model.Tape;

public interface TapeRepository extends JpaRepository<Tape, Long> {
}
