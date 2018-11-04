package rss.combinator.project.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class Utils {

    @Value("${download.path.prefix}")
    private static String pathPrefix;

    public final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String getAbsolute() {
        return new File(Objects.requireNonNull(pathPrefix)).getAbsolutePath() + "/";
    }

}
