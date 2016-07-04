package fr.xebia.xebicon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;

public class InitVcubData {

    public static void main(String[] args) throws IOException {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder("http://192.168.99.100:9200")
                .multiThreaded(true)
                .build());
        JestClient client = factory.getObject();

        boolean indexExists = client.execute(new IndicesExists.Builder("vcub").build()).isSucceeded();

        if (indexExists) {
            client.execute(new DeleteIndex.Builder("vcub").build());
        }

        client.execute(new CreateIndex.Builder("vcub").build());

        Bulk.Builder bulkIndexBuilder = new Bulk.Builder();

        List<Station> stations = readStationFile();

        for (Station station : stations) {
            bulkIndexBuilder.addAction(new Index.Builder(station).index("vcub").type("station").build());
        }

        client.execute(bulkIndexBuilder.build());
    }

    private static List<Station> readStationFile() {
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        try {
            //read json file data to String
            byte[] jsonData = Files.readAllBytes(Paths.get(Resources.getResource("data_bdx_vcub.json").toURI()));

            //convert json string to object
            Station[] stations = mapper.readValue(jsonData, Station[].class);

            return asList(stations);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
