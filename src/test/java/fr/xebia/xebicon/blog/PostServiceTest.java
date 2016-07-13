package fr.xebia.xebicon.blog;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static fr.xebia.xebicon.jest.MyJestClient.getJestClient;
import static org.assertj.core.api.Assertions.assertThat;

public class PostServiceTest {

    private PostService postService;

    @Before
    public void setUp() throws Exception {
        postService = new PostService(getJestClient());
    }

    @Test
    public void should_find_post_related_to_kodo_kojo() {
        //Given
        String term = "kodo kojo";

        //when
        List<Post> posts = postService.search(term);

        //Then
        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getTitle()).isEqualTo("Kodo Kojo : un projet open source pour gérer vos usines logicielles");
        assertThat(posts.get(1).getTitle()).isEqualTo("Kodo Kojo ouvre ses sources !");
    }

    @Test
    public void should_find_post_related_to_full_text_search() {
        //Given
        String term = "recherche full text";

        //when
        List<Post> posts = postService.search(term);

        //Then
        assertThat(posts).hasSize(10);
        assertThat(posts.get(0).getTitle()).isEqualTo("Xebia accueille le Mongo User Group le 2 avril");
        assertThat(posts.get(1).getTitle()).isEqualTo("Logstash, ElasticSearch, Kibana – S01E00 – Analyse de vos données en temps réel cloud-ready");
    }

    @Test
    public void should_find_posts_by_creator() {
        // Given
        String expectedCreator = "Jean-Louis Rigau";

        // When
        List<Post> posts = postService.searchByCreator(expectedCreator);

        // Then
        assertThat(posts.size()).isPositive();
        assertThat(posts).allMatch(post -> post.getCreator().equals(expectedCreator));
    }

}