package fr.xebia.xebicon.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import fr.xebia.xebicon.blog.Post;

import java.io.IOException;
import java.io.UncheckedIOException;

public class JsonParser {

    private final ObjectMapper objectMapper;

    public JsonParser() {
        objectMapper = new ObjectMapper();
    }

    public Post asPost(String jsonAsString) {
        try {
            return objectMapper.readValue(jsonAsString, Post.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String asJson(Post post) {
        try {
            return objectMapper.writeValueAsString(post);
        } catch (JsonProcessingException e) {
            throw Throwables.propagate(e);
        }
    }

    public JsonNode asJsonNode(String jsonAsText) {
        try {
            return objectMapper.readTree(jsonAsText);
        } catch (IOException e) {
            throw new IllegalStateException("bad json format :" + e.getMessage());
        }
    }
}
