import org.json.simple.JSONArray;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Scanner;

// Retrieves logic
public class WeatherApp {
    public static WeatherData getWeatherData(String locationName) {
        // Get location coordinates using geolocation api
        JSONArray locationData = getLocationData(locationName);
    }

    private static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try {
            HttpsURLConnection conn = fetchApiResponse(urlString);

            if (conn.getResponseCode() != 200) {
                System.out.println("Could not retrieve weather data");
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while(scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpsURLConnection fetchApiResponse(String urlString) {
        try {
            URL newUrl = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) newUrl.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
