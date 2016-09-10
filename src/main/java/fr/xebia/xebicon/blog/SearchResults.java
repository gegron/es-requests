package fr.xebia.xebicon.blog;

import java.util.List;

public class SearchResults {

    public final List<Post> posts;
    public final List<Aggregation> filters;

    public SearchResults(List<Post> posts, List<Aggregation> filters) {
        this.posts = posts;
        this.filters = filters;
    }
}
