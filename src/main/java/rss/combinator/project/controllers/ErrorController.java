package rss.combinator.project.controllers;

import com.rometools.rome.io.FeedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rss.combinator.project.dto.ErrorDTO;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.exceptions.FileStorageException;
import rss.combinator.project.exceptions.ParseRssException;
import rss.combinator.project.exceptions.ResourceNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO resourceNotFoundExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(EntryDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO entryDuplicateExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO fileStorageExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO IOExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(ParseRssException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO parseRssExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(InterruptedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO interruptedExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(ExecutionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO executionExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO fileNotFoundExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(FeedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO feedExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
        public ErrorDTO anyExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }

}
