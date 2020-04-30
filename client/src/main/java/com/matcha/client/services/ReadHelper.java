package com.matcha.client.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadHelper {
    public static String readJSON(InputStream inputData) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputData));
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}

