package rss.combinator.project.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.services.Utils;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTags() throws Exception {
        mockMvc.perform(get("/tags"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[0].id", notNullValue()))
                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].name", notNullValue()))
                .andExpect(jsonPath("$.[0].name", equalTo("business")))
                .andExpect(jsonPath("$.[1].id", notNullValue()))
                .andExpect(jsonPath("$.[1].id", equalTo(2)))
                .andExpect(jsonPath("$.[1].name", notNullValue()))
                .andExpect(jsonPath("$.[1].name", equalTo("science")));
    }

    @Test
    public void createTag() throws Exception {
        mockMvc.perform(post("/tags/new")
                .param("name", "sport")
                .param("links", "https://www.sports.ru/rss/rubric.xml?s=208"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/tags"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[2].id", notNullValue()))
                .andExpect(jsonPath("$.[2].id", equalTo(3)))
                .andExpect(jsonPath("$.[2].name", notNullValue()))
                .andExpect(jsonPath("$.[2].name", equalTo("sport")));
        Files.deleteIfExists(Paths.get(Utils.getAbsolute() + "sport.json"));
    }

    @Test
    public void updateTag() throws Exception {
        mockMvc.perform(patch("/tags/1")
                .param("name", "sport"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/tags"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[0].id", notNullValue()))
                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].name", notNullValue()))
                .andExpect(jsonPath("$.[0].name", equalTo("sport")));
    }

    @Test
    public void deleteTag() throws Exception {
        mockMvc.perform(delete("/tags/business"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        final String result = mockMvc.perform(get("/tags"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        assertFalse(result.contains("business"));
    }
}