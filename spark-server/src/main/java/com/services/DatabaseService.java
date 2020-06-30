package com.services;

import static spark.Spark.*;

public class DatabaseService {

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}
