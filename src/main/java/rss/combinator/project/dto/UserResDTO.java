package rss.combinator.project.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResDTO {

    private Long id;
    private String username;
    private Set<String> tags;
    private String role;

}
