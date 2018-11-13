package rss.combinator.project.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column
    private String password;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_tag",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
    @Column
    private String role;
    @Column
    private Boolean active;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = true;
        this.role = "USER";
    }
}
