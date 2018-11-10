package rss.combinator.project.exceptions;

public class ParseRssException extends RuntimeException {

    public ParseRssException() {
    }

    public ParseRssException(String message) {
        super(message);
    }

    public ParseRssException(String message, Throwable cause) {
        super(message, cause);
    }
}
