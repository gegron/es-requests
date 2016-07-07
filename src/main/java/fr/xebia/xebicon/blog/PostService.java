package fr.xebia.xebicon.blog;

import fr.xebia.xebicon.common.JsonParser;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.xebia.xebicon.blog.Post.*;
import static fr.xebia.xebicon.common.Resources.getResource;
import static java.util.stream.Collectors.toList;

public class PostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final JsonParser jsonParser;
    private final JestClient client;

    public PostService(JestClient jestClient) {
        this.client = jestClient;
        jsonParser = new JsonParser();
        initPosts();
    }

    public List<Post> searchByTitle(String searchedTitle) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder
                .query(new MatchQueryBuilder(FIELD_TITLE, searchedTitle));

        try {
            LOGGER.info("Request : {}", searchSourceBuilder.toString());

            SearchResult result = client.execute(new Search.Builder(searchSourceBuilder.toString())
                    .addIndex(POST_INDEX)
                    .addType(POST_TYPE)
                    .build());

            return result.getHits(Post.class)
                    .stream()
                    .map(hit -> hit.source)
                    .collect(toList());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public int indexAll(List<Post> posts) {
        List<Index> bulkActions = posts.stream()
                .map(Index.Builder::new)
                .map(Index.Builder::build)
                .collect(Collectors.toList());

        try {
            BulkResult bulkResult = client.execute(new Bulk.Builder()
                    .defaultIndex(POST_INDEX)
                    .defaultType(POST_TYPE)
                    .addAction(bulkActions)
                    .build());

            LOGGER.info("{} successfully indexed", bulkResult.getItems().size());
            return bulkResult.getItems().size();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Double countPosts() {
        try {
            CountResult count = client.execute(new Count.Builder()
                    .addIndex(POST_INDEX)
                    .addType(POST_TYPE)
                    .build());

            return count.getCount();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void initPosts() {
        if (countPosts() > 0) {
            return;
        }

        try (Stream<String> lines = getResource("xebiablog.json").lines()) {

            List<Post> posts = lines
                    .map(jsonParser::asPost)
                    .collect(Collectors.toList());

            indexAll(posts);
        }
    }

}
