package fr.xebia.xebicon.common;

import com.fasterxml.jackson.databind.JsonNode;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.cluster.Health;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.settings.GetSettings;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

public class IndexService {

    private final JsonParser jsonParser;
    private final JestClient client;

    public IndexService(JestClient jestClient) {
        this.client = jestClient;
        jsonParser = new JsonParser();
    }

    public Optional<String> getMappingOf(String indexName, String type) {
        try {
            JestResult getMappingResult = client.execute(new GetMapping.Builder().addIndex(indexName).addType(type).build());
            if (!getMappingResult.isSucceeded()) {
                return Optional.empty();
            }

            return Optional.ofNullable(getMappingResult.getJsonString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Optional<String> getSettingsOf(String indexName) {
        try {
            JestResult getMappingResult = client.execute(new GetSettings.Builder().addIndex(indexName).build());
            if (!getMappingResult.isSucceeded()) {
                return Optional.empty();
            }
            return Optional.of(getMappingResult.getJsonString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean indexContainsMapping(String indexName, String type, String expectedMapping) {
        return getMappingOf(indexName, type)
                .map(currentMapping -> {
                    JsonNode currentMappingNode = jsonParser.asJsonNode(currentMapping);
                    JsonNode expectedMappingNode = jsonParser.asJsonNode(expectedMapping);

                    return currentMappingNode.get(indexName).get("mappings").equals(expectedMappingNode.get("mappings"));
                }).orElse(false);

    }

    public boolean indexContainsSettings(String indexName, String mappingAndSettings) {
        return getSettingsOf(indexName)
                .map(currentSettings -> {
                    JsonNode currentSettingsNode = jsonParser.asJsonNode(currentSettings);
                    JsonNode expectedSettingsNode = jsonParser.asJsonNode(mappingAndSettings);

                    return currentSettingsNode.get(indexName).get("settings").equals(expectedSettingsNode.get("settings"));
                }).orElse(false);
    }

    public void createIndex(String indexName, String mapping) {
        try {
            client.execute(new CreateIndex.Builder(indexName).settings(mapping).build());
            client.execute(new Health.Builder().setParameter("wait_for_status", "yellow").build());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void deleteIndex(String indexName) {

        try {
            client.execute(new DeleteIndex.Builder(indexName).build());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
