package util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestClient_Patch {
    public static String sendPatchRequest(String urlString, String jsonInputString) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonInputString))
                .header("Content-Type", "application/json; utf-8")
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int responseCode = response.statusCode();
        if (responseCode == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }
}