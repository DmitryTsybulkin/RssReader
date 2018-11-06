package rss.combinator.project.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getPosts() throws Exception {
        String contentType = "text/event-stream;charset=UTF-8";

        mockMvc.perform(get("/posts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(content().contentType(contentType));

        mockMvc.perform(get("/posts")
                .param("tag", "business"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(content().contentType(contentType));

        mockMvc.perform(get("/posts")
                .param("tag", "business")
                .param("from", "00:00:00 04-11-2018"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(content().contentType(contentType));
    }
}