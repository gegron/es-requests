package fr.xebia.xebicon.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

public class Resources {
    public static BufferedReader getResource(String resourceName)  {
        try {
            return new BufferedReader(new InputStreamReader(com.google.common.io.Resources.getResource(resourceName).openStream()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
