package rules;

import junit.framework.TestCase;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class ElasticSearchRule extends TestCase {


//    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchRule.class);
//
//    public static final String ES_INDEX = "test-index";
//
//    private String availablePort;
//    private String esBaseUrl;
//    private String esSearchUrl;
//
//    private static final String DATA_DIRECTORY = "src/test/resources/elasticsearch/data";
//
//    private Node node;
//    private ObjectMapper objectMapper = new MetamorphoseObjectMapper();
//
//    public Client transportClient() {
//        return node.client();
//    }
//
//    public JestClient httpClient() {
//        return EsConfig.buildJestClient(esBaseUrl);
//    }
//
//    public void prepareIndex(String documentType, Object... data) {
//        for (Object datum : data) {
//            prepareIndex(documentType, datum);
//        }
//    }
//
//    public void prepareIndex(String documentType, Object data) {
//        String json = unchecked(() -> objectMapper.writeValueAsString(data));
//
//        prepareIndex(documentType, json);
//    }
//
//    public void prepareIndexDynamoCase(String documentType, Object... data) {
//        for (Object datum : data) {
//            prepareIndexDynamoCase(documentType, datum);
//        }
//    }
//
//    public void prepareIndexDynamoCase(String documentType, Object data) {
//        String capitalizedJson = constructDynamoCaseJson(data);
//
//        prepareIndex(documentType, capitalizedJson);
//    }
//
//    private String constructDynamoCaseJson(Object data) {
//        String json = unchecked(() -> objectMapper.writeValueAsString(data));
//        JsonNode jsonNode = unchecked(() -> objectMapper.readTree(json));
//
//        return capitalizeObjectNode(jsonNode).toString();
//    }
//
//    private void prepareIndex(String documentType, String json) {
//        transportClient().prepareIndex(ES_INDEX, documentType, UUID.randomUUID().toString())
//                .setSource(json)
//                .execute()
//                .actionGet();
//
//        await().atMost(30, SECONDS)
//                .until(() -> new RestTemplate().getForObject(esSearchUrl, String.class).contains(json));
//    }
//
//    private JsonNode capitalizeObjectNode(JsonNode jsonNode) {
//        Iterable<String> fields = jsonNode::fieldNames;
//
//        ObjectNode capitalizedNode = objectMapper.createObjectNode();
//        fields.forEach(field -> {
//            JsonNode node = jsonNode.get(field);
//
//            if (node.isObject()) {
//                node = capitalizeObjectNode(jsonNode.get(field));
//            }
//
//            capitalizedNode.set(capitalize(field), node);
//        });
//        return capitalizedNode;
//    }
//
//    public void createMappingForNotAnalyzedField(String index, String type, String... fieldNames) throws IOException, ExecutionException, InterruptedException {
//
//        XContentBuilder mappingBuilder = XContentFactory.jsonBuilder()
//                .startObject()
//                .startObject(type)
//                .startObject("properties");
//
//        for (String fieldName : fieldNames) {
//            mappingBuilder
//                    .startObject(fieldName)
//                    .field("type", "string")
//                    .field("index", "not_analyzed")
//                    .endObject();
//        }
//
//        mappingBuilder
//                .endObject()
//                .endObject()
//                .endObject();
//
//        logger.info("mappingBuilder: " + mappingBuilder.string());
//
//        httpClient().execute(new PutMapping.Builder(index, type, mappingBuilder.string()).build());
//
//        logger.info(transportClient().admin().indices().getMappings(new GetMappingsRequest().indices(ES_INDEX)).get().mappings().toString());
//    }
//
//    private void startServer() {
//        availablePort = ExceptionUtils.unchecked(NetworkUtils::findAvailablePort);
//        esBaseUrl = "http://localhost:" + availablePort + "/";
//        esSearchUrl = esBaseUrl + "_search";
//
//        Settings settings = builder()
//                .put("path.home", DATA_DIRECTORY)
//                .put("http.port", availablePort)
//                .build();
//
//        node = nodeBuilder()
//                .local(true)
//                .settings(settings)
//                .node();
//    }
//
//    private void stopServer() throws IOException {
//        node.close();
//        FileUtils.deleteDirectory(new File(DATA_DIRECTORY));
//    }
//
//    @Override
//    public Statement apply(Statement base, Description description) {
//        return new Statement() {
//            @Override
//            public void evaluate() throws Throwable {
//                startServer();
//
//                createIndex(ES_INDEX);
//
//                try {
//                    base.evaluate();
//                } finally {
//                    stopServer();
//                }
//            }
//        };
//    }
//
//    private JestResult createIndex(String index) throws IOException {
//        logger.info("Creating index : {}", index);
//        return httpClient().execute(new CreateIndex.Builder(index).build());
//    }
//
}
