package co.wethinkcode.healthsafe;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AlertLevelClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AlertLevelClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public int getAlertLevel() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:7032/alert-level"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        AlertLevel alertLevel = objectMapper.readValue(
                response.body(),
                AlertLevel.class
        );

        return alertLevel.getLevel();
    }
}