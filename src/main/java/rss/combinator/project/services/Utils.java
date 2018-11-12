package rss.combinator.project.services;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {

    private final static String pathPrefix = "downloads/";

    public final static DateTimeFormatter inDateFormat = DateTimeFormatter
            .ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

    public final static DateTimeFormatter outDateFormat = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

    public static String getAbsolute() {
        return new File(pathPrefix).getAbsolutePath() + "/";
    }

    public static String formatDate(String date) {
        return outDateFormat.format(LocalDateTime.parse(date, inDateFormat));
    }

}
