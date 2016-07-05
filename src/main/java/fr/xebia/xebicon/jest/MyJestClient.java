package fr.xebia.xebicon.jest;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

public enum MyJestClient {
    INSTANCE;

    private JestClient client;

    MyJestClient() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://192.168.99.100:9200")
                .multiThreaded(true)
                .build());
        this.client = factory.getObject();
    }

    public static JestClient getJestClient() {
        return INSTANCE.client;
    }
}
