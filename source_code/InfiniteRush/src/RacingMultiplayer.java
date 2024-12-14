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

class RacingMultiplayer implements KeyListener {
    private final Subject gameState;
    private final TimerComponent timer;
    private final List<PlayerCar> players;
    private final List<JLabel> playerLabels;
    private final List<JLabel> obstacleCars;
    private final Random random;

    private JLabel timerLabel;
    private JLabel background1;
    private JLabel background2;

    private JFrame frame;
    private boolean gameOver;

    private int backgroundY1 = 0;
    private int backgroundY2 = -500;

    RacingMultiplayer(int numPlayers) {
        gameState = new GameState();
        timer = new TimerComponent();
        players = new ArrayList<>();
        playerLabels = new ArrayList<>();
        obstacleCars = new ArrayList<>();
        random = new Random();
        gameOver = false;

        for (int i = 0; i < numPlayers; i++) {
            PlayerCar playerCar = new PlayerCar();
            players.add(playerCar);
            gameState.addObserver(playerCar);
        }
        gameState.addObserver(timer);

        frame = new JFrame("Racing Multiplayer");
        frame.setSize(740, 530);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        try {
            background1 = loadImage("/race1.png");
            background2 = loadImage("/race1.png");
            background1.setBounds(0, backgroundY1, 740, 500);
            background2.setBounds(0, backgroundY2, 740, 500);

            for (int i = 0; i < numPlayers; i++) {
                JLabel playerLabel = loadImage("/player_car.png");
                playerLabel.setBounds(players.get(i).getPositionX(), players.get(i).getPositionY(), 36, 57);
                playerLabels.add(playerLabel);
                frame.add(playerLabel, 0); // Add players to the top layer
            }

            timerLabel = new JLabel("00:00", JLabel.CENTER);
            timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            timerLabel.setForeground(Color.RED);
            timerLabel.setBounds(600, 10, 100, 30);

        } catch (Exception e) {
            System.err.println("Error loading resources. Ensure files are placed in 'src/Resources' and paths are correct.");
            e.printStackTrace();
        }

        frame.add(background1);
        frame.add(background2);
        frame.add(timerLabel);

        frame.addKeyListener(this);
        frame.setVisible(true);
        frame.repaint();
    }

    private JLabel loadImage(String resourcePath) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources" + resourcePath)));
        } catch (Exception e) {
            System.err.println("Failed to load resource: " + resourcePath);
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
        for (int i = 0; i < players.size(); i++) {
            Rectangle playerBounds = playerLabels.get(i).getBounds();
            for (JLabel obstacleCar : obstacleCars) {
                if (playerBounds.intersects(obstacleCar.getBounds())) {
                    System.out.println("Player " + (i + 1) + " crashed!");
                    players.remove(i);
                    playerLabels.remove(i);
                    frame.remove(playerLabels.get(i));
                    if (players.isEmpty()) {
                        endGame();
                    }
                    break;
                }
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
        for (int i = 0; i < players.size(); i++) {
            playerLabels.get(i).setBounds(players.get(i).getPositionX(), players.get(i).getPositionY(), 36, 57);
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (!gameOver) {
            int keyCode = ke.getKeyCode();
            // Assign specific keys for each player
            if (keyCode == KeyEvent.VK_A) {
                players.get(0).moveLeft(); // Player 1 keys
            } else if (keyCode == KeyEvent.VK_D) {
                players.get(0).moveRight();
            } else if (keyCode == KeyEvent.VK_W) {
                players.get(0).moveUp();
            } else if (keyCode == KeyEvent.VK_S) {
                players.get(0).moveDown();
            } else if (keyCode == KeyEvent.VK_LEFT) {
                players.get(1).moveLeft(); // Player 2 keys
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                players.get(1).moveRight();
            } else if (keyCode == KeyEvent.VK_UP) {
                players.get(1).moveUp();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                players.get(1).moveDown();
            }
            updateUI();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {}

    @Override
    public void keyTyped(KeyEvent ke) {}
}
