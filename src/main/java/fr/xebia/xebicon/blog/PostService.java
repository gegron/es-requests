package fr.xebia.xebicon.blog;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static fr.xebia.xebicon.blog.Post.*;
import static java.util.stream.Collectors.toList;

public class PostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private JestClient client;

    public PostService(JestClient jestClient) {
        this.client = jestClient;
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
            throw new RuntimeException(e);
        }
    }
}
