import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

// Retrieves logic
public class WeatherApp {
    public static JSONObject getWeatherData(String locationName) {
        // Get location coordinates using geolocation api
        JSONArray locationData = getLocationData(locationName);
        return null;
    }

    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            System.out.println(conn);

            if (conn.getResponseCode() != 200) {
                System.out.println("Could not retrieve weather data");
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while(scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

                scanner.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultsJSONObj = (JSONObject) parser.parse(String.valueOf(resultJson));
                JSONArray locationData = (JSONArray) resultsJSONObj.get("results");
                return locationData;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL newUrl = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) newUrl.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
