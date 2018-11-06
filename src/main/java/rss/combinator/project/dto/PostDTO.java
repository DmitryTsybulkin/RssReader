package rss.combinator.project.dto;

import lombok.*;
import rss.combinator.project.services.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

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
    public int compareTo(PostDTO that) {
        int dateCompare = LocalDateTime.parse(this.date, Utils.outDateFormat)
                .compareTo(LocalDateTime.parse(that.date, Utils.outDateFormat));
        if (dateCompare != 0) {
            return dateCompare;
        }

        int titleCompare = this.title.compareTo(that.title);
        if (titleCompare != 0) {
            return titleCompare;
        }

        return this.link.compareTo(that.link);
    }
}


