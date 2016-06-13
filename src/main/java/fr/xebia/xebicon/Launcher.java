package fr.xebia.xebicon;

import static spark.Spark.get;

public class Launcher {

    public static void main(String[] args) {
        get("/healthcheck", ((request, response) -> "App is running !"));
    }

}
