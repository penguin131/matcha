package com.services;

import static spark.Spark.*;

public class DatabaseService {

    public static void main(String[] args) {
        port(8888);
        get("/hello", (req, res) -> "Hello World");
    }
}
