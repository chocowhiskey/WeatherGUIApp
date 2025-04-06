import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class WeatherAppGUIApp extends JFrame {
    private JSONObject weatherData;

    public WeatherAppGUIApp() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(450, 650);

        setLocationRelativeTo(null);

        setLayout(null);

        setResizable(false);

        addGUIComponents();
    }

    private void addGUIComponents() {
        // Add text field
        JTextField searchTextField = new JTextField();

        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchTextField);

        // Add weather image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/clear.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

        // Add temperature text
        JLabel temparatureText = new JLabel("20 C");
        temparatureText.setBounds(0,350,450,54);
        temparatureText.setFont(new Font("Dialog", Font.BOLD, 30));
        temparatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temparatureText);

        // Add description
        JLabel weatherDesc = new JLabel("Sunny");
        weatherDesc.setBounds(0,405,450,36);
        weatherDesc.setFont(new Font("Dialog", Font.PLAIN, 20));
        weatherDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherDesc);

        // Add humidity image
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        // Add humidity description
        JLabel humidityDesc = new JLabel("<html><b>Humditiy level</b> 100%</html>");
        humidityDesc.setBounds(90,500,85,55);
        humidityDesc.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityDesc);

        // Add windspeed image
        JLabel windspeedImg = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImg.setBounds(220,500,4,66);
        add(windspeedImg);

        // Add windspeed description
        JLabel windspeedDesc = new JLabel("<html><b>Windspeed</b> 15km/h");
        windspeedDesc.setBounds(310,500,85,55);
        windspeedDesc.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windspeedDesc);

        // Add button for text field
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Get user inut
                String userInput = searchTextField.getText();

                // Validate input and trim
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }

                weatherData = WeatherApp.getWeatherData(userInput);

                // Update GUI
                String weatherCondition = (String) weatherData.get("weatherCondition");
                // Update image corresponding to weatherCondition
                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;

                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                }

                // UPdate temperature
                double temperature = (double) weatherData.get("temperature");
                temparatureText.setText(temperature + " C");

                // Update weather condition desc
                weatherDesc.setText(weatherCondition);

                // Update windspeed
                double windspeed = (double) weatherData.get("windspeed");
                windspeedDesc.setText("<html><b>Windspeed " + windspeed + " km/h</html>");

                // Update humidity
                long humidity = (long) weatherData.get("humidity");
                humidityDesc.setText("<html><b>Humidity " + humidity + " %</html>");
            }
        });
        add(searchButton);
    }

    private ImageIcon loadImage(String ressourcePath) {
try {
    BufferedImage image = ImageIO.read(new File(ressourcePath));
    return new ImageIcon(image);
} catch (Exception e) {
    e.printStackTrace();
}
System.out.println("Could not find asset.");
        return null;
    }
}