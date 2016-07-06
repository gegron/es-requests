package fr.xebia.xebicon.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    public void should_convert_object_to_json() throws JsonProcessingException {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        Post post = new Post(asList("category"), "content", "creator", "description", "14/07/1987", "title");

        // When
        String json = objectMapper.writeValueAsString(post);

        // Then
        assertThat(json).isNotEmpty();
        System.out.println("Result : " + json);
    }

}