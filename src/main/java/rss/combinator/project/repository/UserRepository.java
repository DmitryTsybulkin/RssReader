package rss.combinator.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rss.combinator.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
