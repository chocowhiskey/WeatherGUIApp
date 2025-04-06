import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class WeatherAppGUIApp extends JFrame {
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

        // Add button for text field
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);
        add(searchButton);

        // Add weather image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/clear.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);
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