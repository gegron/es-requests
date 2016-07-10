package fr.xebia.xebicon.blog;

import fr.xebia.xebicon.common.IndexService;
import fr.xebia.xebicon.common.JsonParser;
import fr.xebia.xebicon.common.Resources;
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
    private final IndexService indexService;

    public PostService(JestClient jestClient) {
        this.client = jestClient;
        jsonParser = new JsonParser();
        indexService = new IndexService(jestClient);
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

    public double countPosts() {
        try {
            CountResult count = client.execute(new Count.Builder()
                    .addIndex(POST_INDEX)
                    .addType(POST_TYPE)
                    .build());

            if (!count.isSucceeded()) {
                return 0;
            }

            return count.getCount();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void initPosts() {
        String mapping = Resources.getFile("mappings/blog.json").lines().collect(Collectors.joining("\n"));

        if (!indexService.indexContainsMapping(POST_INDEX, POST_TYPE, mapping)) {
            LOGGER.info("mapping change reinitialize xebia index");

            indexService.deleteIndex(POST_INDEX);
            indexService.createIndex(POST_INDEX, mapping);
        }

        if (countPosts() == 0) {
            try (Stream<String> lines = getResource("xebiablog.json").lines()) {

                List<Post> posts = lines
                        .map(jsonParser::asPost)
                        .collect(Collectors.toList());

                indexAll(posts);
            }
        }
    }

}
