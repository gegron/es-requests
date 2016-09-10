package fr.xebia.xebicon;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.xebia.xebicon.blog.PostService;
import fr.xebia.xebicon.jest.MyJestClient;
import io.searchbox.client.JestClient;

import static spark.Spark.get;

public class Launcher {

    private final PostService postService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Launcher() {
        JestClient jestClient = MyJestClient.getJestClient();
        this.postService = new PostService(jestClient);
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.initData();
        launcher.initRoute();
    }

    public void initData() {
        postService.initPosts();
    }

    public void initRoute() {
        get("/healthcheck", (request, response) -> "App is running !");

        get("/xebia/blog/search/:textToSearch", (request, response) -> {
            response.type("application/json");
            String textToSearch = request.params("textToSearch");
            return postService.search(textToSearch);
        }, objectMapper::writeValueAsString);
    }

}
