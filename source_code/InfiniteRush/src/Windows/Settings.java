package Windows;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Settings extends JFrame {
    public static String selectedMap = "/Resources/race1.png"; // Default map path

    public Settings() {
        // Set the title, size, and close operation
        setTitle("Settings");
        setSize(800, 600); // Window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apply a dark theme to the entire application
        applyDarkTheme();

        // Main background panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(20, 20, 20)); // Dark theme background

        // Tabbed pane for sections
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setBackground(new Color(30, 30, 30)); // Dark tabs background
        tabbedPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));

        // Add sections
        tabbedPane.addTab("Description", createDescriptionPanel());
        tabbedPane.addTab("Map", createMapPanel());
        tabbedPane.addTab("Audio Settings", createAudioSettingsPanel());
        tabbedPane.addTab("Controls", createControlsPanel());

        // Add tabbed pane to the main panel
        panel.add(tabbedPane, BorderLayout.CENTER);

        // Back button
        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Transparent button panel
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Set content pane
        setContentPane(panel);
        setVisible(true);
    }

    private JPanel createDescriptionPanel() {
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBackground(new Color(20, 20, 20)); // Dark theme background

        JLabel titleLabel = new JLabel("Game Description", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JTextArea descriptionText = new JTextArea();
        descriptionText.setText("""
                Welcome to the ultimate Racing Game!
                
                Objective:
                Navigate your car through a fast-paced highway while avoiding obstacles and other vehicles. Use your skills to dodge, shoot, and boost your way to a high score.
                
                Features:
                - Use arrow keys to steer your car.
                - Activate the Nitrous Boost for a temporary speed advantage.
                - Destroy obstacles using bullets to clear your path.
                - Choose from multiple maps to customize your racing experience.
                - Avoid collisions to stay in the race and maximize your score.
                
                Are you ready to test your reflexes and strategy in this high-speed adventure? Enjoy the thrill of the race and aim for the highest score!
                """);
        descriptionText.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionText.setForeground(Color.WHITE);
        descriptionText.setBackground(new Color(30, 30, 30));
        descriptionText.setEditable(false);
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(descriptionText);
        scrollPane.setBackground(new Color(20, 20, 20));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        descriptionPanel.add(titleLabel, BorderLayout.NORTH);
        descriptionPanel.add(scrollPane, BorderLayout.CENTER);

        return descriptionPanel;
    }

    private JPanel createMapPanel() {
        JPanel mapPanel = new JPanel(new BorderLayout());
        mapPanel.setBackground(new Color(20, 20, 20)); // Dark theme background

        JLabel titleLabel = new JLabel("Select a Map", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JLabel mapPreview = new JLabel();
        mapPreview.setHorizontalAlignment(SwingConstants.CENTER);
        mapPreview.setIcon(loadMapPreview(selectedMap));

        JPanel mapSelectionPanel = new JPanel();
        mapSelectionPanel.setBackground(new Color(20, 20, 20));
        mapSelectionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton map1Button = createStyledButton("Map 1");
        map1Button.addActionListener(e -> {
            selectedMap = "/Resources/race1.png";
            mapPreview.setIcon(loadMapPreview(selectedMap));
        });

        JButton map2Button = createStyledButton("Map 2");
        map2Button.addActionListener(e -> {
            selectedMap = "/Resources/race2.png";
            mapPreview.setIcon(loadMapPreview(selectedMap));
        });

        JButton map3Button = createStyledButton("Map 3");
        map3Button.addActionListener(e -> {
            selectedMap = "/Resources/race3.png";
            mapPreview.setIcon(loadMapPreview(selectedMap));
        });

        mapSelectionPanel.add(map1Button);
        mapSelectionPanel.add(map2Button);
        mapSelectionPanel.add(map3Button);

        mapPanel.add(titleLabel, BorderLayout.NORTH);
        mapPanel.add(mapPreview, BorderLayout.CENTER);
        mapPanel.add(mapSelectionPanel, BorderLayout.SOUTH);

        return mapPanel;
    }

    private ImageIcon loadMapPreview(String mapPath) {
        java.net.URL resource = getClass().getResource(mapPath);
        if (resource == null) {
            System.err.println("Resource not found: " + mapPath);
            return new ImageIcon(); // Return empty icon if resource is missing
        }
        ImageIcon icon = new ImageIcon(resource);
        Image scaledImage = icon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private JPanel createAudioSettingsPanel() {
        JPanel audioPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        audioPanel.setBackground(new Color(20, 20, 20));

        JLabel bgMusicLabel = new JLabel("Background Music Volume:");
        bgMusicLabel.setForeground(Color.WHITE);
        JSlider bgMusicSlider = createStyledSlider();

        JLabel sfxLabel = new JLabel("Sound Effects Volume:");
        sfxLabel.setForeground(Color.WHITE);
        JSlider sfxSlider = createStyledSlider();

        audioPanel.add(bgMusicLabel);
        audioPanel.add(bgMusicSlider);
        audioPanel.add(sfxLabel);
        audioPanel.add(sfxSlider);
        return audioPanel;
    }

    private JPanel createControlsPanel() {
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBackground(new Color(20, 20, 20)); // Dark theme background

        JLabel titleLabel = new JLabel("Control Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JTable table = createControlTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(new Color(20, 20, 20));
        scrollPane.getViewport().setBackground(new Color(20, 20, 20));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        controlsPanel.add(titleLabel, BorderLayout.NORTH);
        controlsPanel.add(scrollPane, BorderLayout.CENTER);

        return controlsPanel;
    }

    private JTable createControlTable() {
        String[] columnNames = {"Control", "Action"};
        Object[][] data = {
                {"Arrow Up", "Move Up"},
                {"Arrow Down", "Move Down"},
                {"Arrow Left", "Move Left"},
                {"Arrow Right", "Move Right"},
                {"A", "Shoot"},
                {"N", "Activate Nitrous Boost"},
                {"Space Bar", "Pause/Resume"},
                {"ESC", "Exit Game"}
        };

        return createTable(columnNames, data);
    }

    private JTable createTable(String[] columnNames, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable cell editing
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(30, 30, 30));
        table.setGridColor(new Color(60, 60, 60));
        table.setRowHeight(30);

        // Center align headers
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBackground(new Color(40, 40, 40));
        headerRenderer.setForeground(Color.WHITE);
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));

        // Center align cells
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, cellRenderer);

        return table;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 50));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 170, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 150, 250));
            }
        });

        return button;
    }

    private JSlider createStyledSlider() {
        JSlider slider = new JSlider(0, 100, 80);
        slider.setOpaque(false);
        slider.setForeground(Color.WHITE);
        return slider;
    }

    private void applyDarkTheme() {
        UIManager.put("TabbedPane.selected", new Color(30, 30, 30));
        UIManager.put("TabbedPane.contentAreaColor", new Color(20, 20, 20));
        UIManager.put("TabbedPane.background", new Color(30, 30, 30));
        UIManager.put("TabbedPane.foreground", Color.WHITE);
    }

    public String getSelectedMap() {
        return selectedMap;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Settings::new);
    }
}
