import org.elasticsearch.test.ESIntegTestCase;
import org.elasticsearch.test.ESIntegTestCase.ClusterScope;
import org.junit.Test;

import static org.elasticsearch.test.ESIntegTestCase.Scope.SUITE;

@ClusterScope(scope = SUITE, numDataNodes = 1)
public class EsTest extends ESIntegTestCase {

    @Test
    public void should_test_elastic_search() {
        // Given
        createIndex("testIndex");

        ensureGreen("testIndex");


        // When


        // Then
    }


}
