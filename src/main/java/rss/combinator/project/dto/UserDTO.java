package rss.combinator.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String role;
    private Set<String> tags;

}
