package fr.xebia.xebicon.common;

import java.io.*;

public class Resources {
    public static BufferedReader getResource(String resourceName) {
        try {
            return new BufferedReader(new InputStreamReader(com.google.common.io.Resources.getResource(resourceName).openStream()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static BufferedReader getFile(String filePath) {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
