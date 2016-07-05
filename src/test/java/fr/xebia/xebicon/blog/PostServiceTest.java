package fr.xebia.xebicon.blog;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static fr.xebia.xebicon.jest.MyJestClient.getJestClient;

public class PostServiceTest {

    private PostService postService;

    @Before
    public void setUp() throws Exception {
        postService = new PostService(getJestClient());
    }

    @Test
    public void should_find_java_posts() {
        // Given

        // When
        List<Post> posts = postService.searchByTitle("Java");

        // Then
        System.out.println("Count found: " + posts.size());
    }


}