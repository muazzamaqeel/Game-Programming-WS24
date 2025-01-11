package Windows;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Settings extends JFrame {
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
        tabbedPane.addTab("Instructions", createInstructionsPanel());
        tabbedPane.addTab("Audio Settings", createAudioSettingsPanel());
        tabbedPane.addTab("Graphics Settings", createGraphicsSettingsPanel());
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

    private JPanel createInstructionsPanel() {
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBackground(new Color(20, 20, 20)); // Dark theme background

        JLabel titleLabel = new JLabel("Game Instructions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JTable table = createInstructionTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(new Color(20, 20, 20));
        scrollPane.getViewport().setBackground(new Color(20, 20, 20));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        instructionsPanel.add(titleLabel, BorderLayout.NORTH);
        instructionsPanel.add(scrollPane, BorderLayout.CENTER);

        return instructionsPanel;
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

    private JTable createInstructionTable() {
        String[] columnNames = {"Action", "Description"};
        Object[][] data = {
                {"Arrow Keys", "Move the car (Up, Down, Left, Right)"},
                {"Space Bar", "Resume/Pause the game"},
                {"ESC", "Exit the game"},
                {"A Key", "Shoot bullets"},
                {"N Key", "Activate Nitrous Boost (5s duration, 10s cooldown)"}
        };

        return createTable(columnNames, data);
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

    private JPanel createGraphicsSettingsPanel() {
        JPanel graphicsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        graphicsPanel.setBackground(new Color(20, 20, 20));

        JLabel resolutionLabel = new JLabel("Resolution:");
        resolutionLabel.setForeground(Color.WHITE);
        JComboBox<String> resolutionCombo = new JComboBox<>(new String[]{"800x600", "1024x768", "1280x720", "1920x1080"});
        resolutionCombo.setBackground(new Color(30, 30, 30));
        resolutionCombo.setForeground(Color.WHITE);

        JLabel fullscreenLabel = new JLabel("Fullscreen:");
        fullscreenLabel.setForeground(Color.WHITE);
        JCheckBox fullscreenCheck = new JCheckBox();
        fullscreenCheck.setOpaque(false);

        JLabel vsyncLabel = new JLabel("V-Sync:");
        vsyncLabel.setForeground(Color.WHITE);
        JCheckBox vsyncCheck = new JCheckBox();
        vsyncCheck.setOpaque(false);

        graphicsPanel.add(resolutionLabel);
        graphicsPanel.add(resolutionCombo);
        graphicsPanel.add(fullscreenLabel);
        graphicsPanel.add(fullscreenCheck);
        graphicsPanel.add(vsyncLabel);
        graphicsPanel.add(vsyncCheck);
        return graphicsPanel;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Settings::new);
    }
}
