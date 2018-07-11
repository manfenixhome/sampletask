package com.example.sampletask;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Component
public class RequestUtil {

    public String get(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = conn.getInputStream().read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
