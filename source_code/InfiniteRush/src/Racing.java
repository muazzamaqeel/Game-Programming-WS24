import Components.PlayerCar;
import Components.TimerComponent;
import Interfaces.Subject;
import StateManagement.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

class Racing implements KeyListener {
    private final Subject gameState;
    private final TimerComponent timer;
    private final PlayerCar playerCar;
    private final List<JLabel> obstacleCars;
    private final Random random;

    private JLabel playerCarLabel;
    private JLabel timerLabel;
    private JLabel background1;
    private JLabel background2;

    private JFrame frame;
    private boolean gameOver;

    private int backgroundY1 = 0;
    private int backgroundY2 = -500;

    Racing() {
        gameState = new GameState();
        timer = new TimerComponent();
        playerCar = new PlayerCar();
        obstacleCars = new ArrayList<>();
        random = new Random();
        gameOver = false;

        gameState.addObserver(timer);
        gameState.addObserver(playerCar);

        frame = new JFrame("Racing Game");
        frame.setSize(740, 530);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        try {
            // Load resources and set initial positions
            background1 = loadImage("/race1.png");
            background2 = loadImage("/race1.png");
            playerCarLabel = loadImage("/player_car.png");

            background1.setBounds(0, backgroundY1, 740, 500);
            background2.setBounds(0, backgroundY2, 740, 500);

            playerCarLabel.setBounds(playerCar.getPositionX(), playerCar.getPositionY(), 36, 57);
            playerCarLabel.setVisible(true); // Ensure player car is visible

            timerLabel = new JLabel("00:00", JLabel.CENTER);
            timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            timerLabel.setForeground(Color.RED);
            timerLabel.setBounds(600, 10, 100, 30);

            System.out.println("PlayerCar initial position: X = " + playerCar.getPositionX() +
                    ", Y = " + playerCar.getPositionY());

        } catch (Exception e) {
            System.err.println("Error loading resources. Ensure files are placed in 'src/Resources' and paths are correct.");
            e.printStackTrace();
            background1 = new JLabel();
            background2 = new JLabel();
            playerCarLabel = new JLabel();
            timerLabel = new JLabel("Error", JLabel.CENTER);
        }

        // Add components in the correct order to ensure proper layering
        frame.add(background1);
        frame.add(background2);
        frame.add(timerLabel);
        frame.add(playerCarLabel, 0); // Add the player car to the top layer

        // Debugging player car visibility
        System.out.println("PlayerCarLabel Bounds: " + playerCarLabel.getBounds());
        System.out.println("PlayerCarLabel Visible: " + playerCarLabel.isVisible());

        frame.addKeyListener(this);
        frame.setVisible(true);

        // Trigger a repaint to ensure all components are visible
        frame.repaint();
    }

    private JLabel loadImage(String resourcePath) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources" + resourcePath)));
            System.out.println("Successfully loaded resource: " + resourcePath);
        } catch (Exception e) {
            System.err.println("Failed to load resource: " + resourcePath);
            e.printStackTrace();
        }
        return new JLabel(icon);
    }

    public void startGame() {
        new Timer(50, e -> {
            if (!gameOver) {
                moveBackground();
                moveObstacles();
                checkCollisions();
                gameState.notifyObservers();
                updateUI();
            }
        }).start();

        new Timer(2000, e -> {
            if (!gameOver) {
                spawnObstacleCar();
            }
        }).start();
    }

    private void moveBackground() {
        backgroundY1 += 5;
        backgroundY2 += 5;

        if (backgroundY1 >= 500) {
            backgroundY1 = -500;
        }
        if (backgroundY2 >= 500) {
            backgroundY2 = -500;
        }

        background1.setBounds(0, backgroundY1, 740, 500);
        background2.setBounds(0, backgroundY2, 740, 500);
    }

    private void spawnObstacleCar() {
        int x = random.nextInt(300) + 200;
        JLabel obstacleCar = loadImage("/car_mask.png");
        if (obstacleCar.getIcon() != null) {
            obstacleCar.setBounds(x, -100, 36, 57);
            obstacleCars.add(obstacleCar);
            frame.add(obstacleCar, 0);
        } else {
            System.err.println("Skipping obstacle car due to missing resource.");
        }
    }

    private void moveObstacles() {
        List<JLabel> carsToRemove = new ArrayList<>();
        for (JLabel obstacleCar : obstacleCars) {
            int newY = obstacleCar.getY() + 5;
            obstacleCar.setBounds(obstacleCar.getX(), newY, 36, 57);

            if (newY > 530) {
                carsToRemove.add(obstacleCar);
            }
        }
        obstacleCars.removeAll(carsToRemove);
        carsToRemove.forEach(frame::remove);
    }

    private void checkCollisions() {
        Rectangle playerBounds = playerCarLabel.getBounds();
        for (JLabel obstacleCar : obstacleCars) {
            if (playerBounds.intersects(obstacleCar.getBounds())) {
                endGame();
                break;
            }
        }
    }

    private void endGame() {
        gameOver = true;
        JOptionPane.showMessageDialog(frame, "Game Over! You lasted: " + timer.getFormattedTime(), "Game Over", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    private void updateUI() {
        timerLabel.setText(timer.getFormattedTime());
        playerCarLabel.setBounds(playerCar.getPositionX(), playerCar.getPositionY(), 36, 57);
        System.out.println("Updated playerCarLabel position: " + playerCarLabel.getBounds());
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (!gameOver) {
            int keyCode = ke.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT) {
                playerCar.moveLeft();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                playerCar.moveRight();
            } else if (keyCode == KeyEvent.VK_UP) {
                playerCar.moveUp();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                playerCar.moveDown();
            }
            updateUI();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {}

    @Override
    public void keyTyped(KeyEvent ke) {}
}
