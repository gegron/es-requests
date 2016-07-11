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