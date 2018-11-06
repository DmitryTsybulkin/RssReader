package rss.combinator.project.services;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void getAbsolute() throws Exception {
        Path path = Paths.get(Utils.getAbsolute());
        assertTrue(path.isAbsolute());
        assertTrue(path.endsWith("downloads/"));
    }

    @Test
    public void formatDate() throws Exception {
        String inputDate = "Fri Nov 02 21:29:58 MSK 2018";
        assertEquals("21:29:58 02-11-2018", Utils.formatDate(inputDate));
    }
}