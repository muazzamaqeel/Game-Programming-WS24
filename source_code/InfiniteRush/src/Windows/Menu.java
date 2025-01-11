package Windows;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Menu extends JFrame {
    public Menu() {
        // Set the title, size, and close operation
        setTitle("Game Menu");
        setSize(800, 600); // Window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel with background image
        JPanel panel = new BackgroundPanel("/Resources/Menu.png");
        panel.setLayout(new BorderLayout());

        // Panel to hold buttons at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Horizontal spacing and padding
        buttonPanel.setOpaque(false); // Make panel transparent

        // Button styles
        JButton playButton = createStyledButton("Play");
        JButton settingsButton = createStyledButton("Settings");
        JButton quitButton = createStyledButton("Quit");

        // Add actions to buttons
        playButton.addActionListener(e -> {
            this.dispose(); // Close the menu window
            new Racing();   // Start the Racing game
        });
        // Inside the Menu constructor after creating settingsButton
        settingsButton.addActionListener(e -> {
            new Settings(); // Open the Settings window
        });
        quitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the button panel
        buttonPanel.add(playButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(quitButton);

        // Add button panel to the bottom of the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to the frame
        add(panel);
        setVisible(true);
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(50, 150, 250)); // Light blue background
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::new);
    }

    // Custom JPanel to draw background image
    static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();

            // Debugging check
            if (backgroundImage == null) {
                System.err.println("Image not found at: " + imagePath);
            } else {
                System.out.println("Image loaded successfully!");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image scaled to fit the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
