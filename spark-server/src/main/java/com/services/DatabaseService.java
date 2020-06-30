package com.services;

import spark.servlet.SparkApplication;

import static spark.Spark.get;

public class DatabaseService implements SparkApplication {
    public static void main(String[] args) {
        new DatabaseService().init();
    }

    @Override
    public void init() {
        get("/hello", (req, res) -> "Hello World");
    }
}
