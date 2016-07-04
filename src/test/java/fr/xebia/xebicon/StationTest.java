package fr.xebia.xebicon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StationTest {

    @Test
    public void should_transform_into_vcub_station() throws IOException, URISyntaxException {
        // Given
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        //read json file data to String
        byte[] jsonData = Files.readAllBytes(Paths.get(Resources.getResource("datum_vcub.json").toURI()));

        //convert json string to object
        Station station = mapper.readValue(jsonData, Station.class);

        // When
        System.out.println("Result: " + station);


        // Then
    }

    @Test
    public void should_transform_into_vcub_stations() throws IOException, URISyntaxException {
        // Given
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        //read json file data to String
        byte[] jsonData = Files.readAllBytes(Paths.get(Resources.getResource("data_bdx_vcub.json").toURI()));

        //convert json string to object
        Station[] stations = mapper.readValue(jsonData, Station[].class);

        // When
        Stream.of(stations)
                .forEach(System.out::println);
    }


}