package fr.xebia.xebicon.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.xebia.xebicon.blog.Post;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonParserTest {

    @Test
    public void should_convert_object_to_json() throws JsonProcessingException {
        // Given
        Post post = new Post(singletonList("category"), "content", "creator", "description", "14/07/1987", "title");

        // When
        String json = new JsonParser().asJson(post);

        // Then
        assertThat(json).isEqualTo("{" +
                "\"category\":[\"category\"]," +
                "\"content\":\"content\"," +
                "\"creator\":\"creator\"," +
                "\"description\":\"description\"," +
                "\"pubDate\":\"14/07/1987\"," +
                "\"title\":\"title\"" +
                "}");
    }

}