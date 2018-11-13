package rss.combinator.project.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReqDTO {

    private Long id;
    private String username;
    private String password;
    private Set<String> tags;

}
