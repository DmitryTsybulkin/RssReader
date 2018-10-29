package rss.combinator.project.exceptions;

public class EntryDuplicateException extends RuntimeException {

    public EntryDuplicateException() {
    }

    public EntryDuplicateException(String message) {
        super(message);
    }

    public EntryDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
