package fr.xebia.xebicon.blog;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static fr.xebia.xebicon.jest.MyJestClient.getJestClient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class PostServiceTest {

    private PostService postService;

    @Before
    public void setUp() throws Exception {
        postService = new PostService(getJestClient());
    }

    /**
     * Recherche full text
     */
    @Test
    public void should_find_post_related_to_kodo_kojo() {
        //Given
        String term = "kodo kojo";

        //when
        SearchResults result = postService.search(term);

        //Then
        assertThat(extractProperty("title").from(result.posts))
                .containsOnly(
                        "Kodo Kojo : un projet open source pour gérer vos usines logicielles",
                        "Kodo Kojo ouvre ses sources !");
    }

    /**
     * Recherche full text
     * (strip HTML par le mapping)
     */
    @Test
    public void should_find_post_related_to_full_text_search() {
        //Given
        String term = "recherche full text";

        //when
        SearchResults result = postService.search(term);

        //Then
        assertThat(result.posts).hasSize(10);
        assertThat(result.posts.get(0).getTitle()).isEqualTo("Xebia accueille le Mongo User Group le 2 avril");
        assertThat(result.posts.get(1).getTitle()).isEqualTo("Logstash, ElasticSearch, Kibana – S01E00 – Analyse de vos données en temps réel cloud-ready");
    }

    /**
     * Recherche par synonyme (changement de mapping)
     */
    @Test
    public void should_find_post_related_to_typesafe_when_search_word_lightbend() {
        //Given
        String term = "lightbend";

        //when
        SearchResults results = postService.search(term);

        //Then
        assertThat(results.posts).hasSize(10);
        assertThat(results.posts.get(0).getTitle()).isEqualTo("Typesafe, partenaire de Xebia, devient Lightbend");
        assertThat(results.posts.get(1).getTitle()).isEqualTo("Xebia organise un Hands’on Akka Java/Scala le 18 juin");
    }

    /**
     * Recherche exacte (changement de mapping)
     */
    @Test
    public void should_find_posts_by_creator() {
        // Given
        String expectedCreator = "Jean-Louis Rigau";

        // When
        List<Post> posts = postService.searchByCreator(expectedCreator).posts;

        // Then
        assertThat(posts.size()).isPositive();
        assertThat(posts).allMatch(post -> post.getCreator().equals(expectedCreator));
    }

    /**
     * TODO: Rechercher des articles en tenant compte des accents
     * Si un auteur se nomme Léa
     * <p>
     * Recherche avec lea ou léa ou Léa doit retourner les mêmes articles
     */
    @Test
    public void should_find_posts_according_to_accent() {
        // Given
        String expectedCreator = "Séven Le Mesle";
        String creatorWithoutAccent = "Seven Le Mesle";

        // When
        List<Post> postsWithoutAccent = postService.searchByCreator(creatorWithoutAccent).posts;
        List<Post> postsWithAccent = postService.searchByCreator(expectedCreator).posts;

        // Then
        assertThat(postsWithoutAccent.size()).isPositive();
        assertThat(postsWithAccent.size()).isPositive();
        assertThat(postsWithoutAccent).allMatch(post -> post.getCreator().equals("Séven Le Mesle"));
        assertThat(postsWithAccent).containsOnly(postsWithoutAccent.stream().toArray(Post[]::new));
    }

    /**
     * TODO: use aggregate
     */
    @Test
    public void should_use_aggregate_to_find_all_categories() {
        String term = "java";

        //when
        List<Aggregation> filters = postService.search(term).filters;

        //Then
        assertThat(filters).contains(new Aggregation("creator", Arrays.asList("Xebia France", "Xavier Bucchiotty", "Francois Sarradin", "François Sarradin", "Seven Le Mesle", "Séven Le Mesle", "Alexis Kinsella", "Bertrand Dechoux", "Julien Smadja", "Pierre Laporte")));
        assertThat(filters).contains(new Aggregation("category", Arrays.asList("events", "front", "agile", "mobile", "java", "back", "data", "jee", "craft", "divers")));

    }

    /**
     * TODO: use completion suggester
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters-completion.html#search-suggesters-completion
     * <p>
     * Prend en compte les erreurs, ne retourne pas les documents complets
     */
    @Test
    public void should_use_completion_to_help_build_search_request() {
        // Given

        // When

        // Then
    }

    /**
     * TODO: trouver un cas d'utilisation
     */
    @Test
    public void should_use_multi_match_query() {
        // Given

        // When

        // Then
    }

    /**
     * TODO:
     */
    @Test
    public void should_use_stop_words() {
        // Given

        // When

        // Then
    }


}