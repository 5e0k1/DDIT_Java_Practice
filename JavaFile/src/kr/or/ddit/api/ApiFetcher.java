package kr.or.ddit.api;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiFetcher {
    public static void main(String[] args) {
        String apiUrl = "https://jsonplaceholder.typicode.com/todos/1"; // 샘플 API
        try {
            String response = fetchApiData(apiUrl);
            System.out.println("API 응답: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String fetchApiData(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("HTTP 오류 코드: " + connection.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();

        return response.toString();
    }
}
