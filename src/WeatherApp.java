import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// Retrieves logic
public class WeatherApp {
    public static JSONObject getWeatherData(String locationName) {
        // Get location coordinates using geolocation api
        JSONArray locationData = getLocationData(locationName);

        JSONObject location = (JSONObject) locationData.getFirst();
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude +"&hourly=temperature_2m,weather_code,wind_speed_10m,relative_humidity_2m&timezone=Europe%2FBerlin";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200) {
                System.out.println("Could not fetch weather data");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }

            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJSONObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            JSONObject hourly = (JSONObject) resultJSONObj.get("hourly");

            // we want to get hourly data
            // so we need to get the index of our current hour
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            // Get temperature data
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            // Get weather code
            JSONArray weatherData = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weatherData.get(index));

            // Get humidity data
            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            // Get windspeed data
            JSONArray windspeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) windspeedData.get(index);

            // Build the weather json that we pass to our frontend
            JSONObject weather = new JSONObject();
            weather.put("temperature", temperature);
            weather.put("weatherCondition", weatherCondition);
            weather.put("windspeed", windspeed);
            weather.put("humidity", humidity);

            return weather;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONArray getLocationData(String locationName) {
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

    private static int findIndexOfCurrentTime (JSONArray timeList) {
        String currentTime = getCurrentTime();

        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }
        return 0;
    }

    private static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedTime = currentDateTime.format(formatter);
        return formattedTime;
    }

    private static String convertWeatherCode (long weathercode) {
        String weatherCondition = "";
        if (weathercode == 0L) {
            weatherCondition = "Clear";
        } else if (weathercode <= 3L && weathercode > 0L) {
            weatherCondition = "Cloudy";
        } else if ((weathercode >=57L && weathercode <=67L) || (weathercode >= 80L && weathercode <= 99L)) {
weatherCondition = "Rainy";
        } else if (weathercode >= 71L && weathercode <=77L) {
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }
}
