package rss.combinator.project.dto;

import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Comparable<PostDTO> {

    private String title;
    private String date;
    private String link;

    @Override
    public int compareTo(PostDTO o) {
        return 0;
    }
}


