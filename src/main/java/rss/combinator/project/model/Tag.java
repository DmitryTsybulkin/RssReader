package rss.combinator.project.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Link> links;
    @ManyToMany(mappedBy = "tags")
    private List<User> users = new ArrayList<>();

    public Tag(String name) {
        this.name = name;
    }
}
