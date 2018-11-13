package rss.combinator.project.representation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rss.combinator.project.dto.UserDTO;
import rss.combinator.project.model.Tag;
import rss.combinator.project.model.User;
import rss.combinator.project.services.UserService;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserRepresentation {

    private final UserService userService;

    @Autowired
    public UserRepresentation(UserService userService) {
        this.userService = userService;
    }

    public UserDTO createUser(UserDTO dto) {
        return toDto(userService.create(fromDto(dto)));
    }

    public UserDTO getUserById(Long id) {
        return toDto(userService.getById(id));
    }

    public Page<UserDTO> getAllActiveUsers(Pageable pageable) {
        return userService.getAllActive(pageable).map(this::toDto);
    }

    public void deactivateUser(Long id) {
        userService.deactivate(id);
    }

    public UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .tags(user.getTags() == null || user.getTags().isEmpty() ?
                        new HashSet<>() : user.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .build();
    }

    public User fromDto(UserDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

}
