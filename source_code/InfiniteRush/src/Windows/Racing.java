package Windows;

import Components.PlayerCar;
import Components.Bullet;
import StateManagement.GameConfig;
import StateManagement.ObstacleManager;
import Components.Music;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class Racing implements KeyListener {
    private final PlayerCar playerCar;
    private final ObstacleManager obstacleManager;
    private final List<Bullet> bullets = new ArrayList<>();
    // --- NEW FIELDS FOR SCORE FUNCTIONALITY ---
    private int score = 0;
    private JLabel scoreLabel;
    private final Map<JLabel, Boolean> obstaclePassed = new HashMap<>();
    // --- NEW: Car destruction limit fields ---
    private int carLimit = 5;           // We start with a limit of 5 cars to destroy
    private JLabel limitLabel;          // Label to display the "Limit: 5 Cars"
    // -----------------------------------------
    private JLabel playerCarLabel;
    private JLabel background1;
    private JLabel background2;
    private JLabel pauseLabel;

    private final JFrame frame;
    private int backgroundY1 = 0;
    private int backgroundY2 = -GameConfig.FRAME_HEIGHT;
    private boolean gameOver;
    private boolean paused = false;

    private final Music backgroundMusic;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean limitReached = false;

    Racing() {
        playerCar = new PlayerCar(GameConfig.LEFT_MARGIN, GameConfig.FRAME_HEIGHT - 150);
        frame = new JFrame("Windows.Racing Game");
        obstacleManager = new ObstacleManager(frame);

        // Initialize background music
        backgroundMusic = new Music("/Resources/MusicData/track1.wav");
        backgroundMusic.playLoop(); // Start playing music
        initUI();
    }


    private void initUI() {
        // Set to full window size without fullscreen


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(GameConfig.FRAME_WIDTH, GameConfig.FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        background1 = loadBackground();
        background2 = loadBackground();
        playerCarLabel = loadCarImage();
        pauseLabel = createPauseLabel();

        frame.add(background1);
        frame.add(background2);
        frame.add(playerCarLabel, 0);
        initScoreboard();
        initLimitLabel();    // --- NEW: Adds the “Limit: 5 Cars” label

        frame.addKeyListener(this);
        frame.setVisible(true);

        startGame();

        frame.add(pauseLabel);
        frame.setComponentZOrder(pauseLabel, 0);
        pauseLabel.setVisible(false);

    }

    /**
     * Creates the scoreboard label and positions it on the frame.
     */
    private void initScoreboard() {
        scoreLabel = new JLabel("Score: 0");

        // Make label opaque (so background color is visible)
        scoreLabel.setOpaque(true);

        // Example: semi-transparent black background
        scoreLabel.setBackground(new Color(0, 0, 0, 180));

        // Font styling
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setForeground(Color.WHITE);

        // Optional: add a colored border (with rounded corners if you wish)
        // BorderFactory.createLineBorder(Color color, int thickness, boolean roundedCorners)
        scoreLabel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2, true));

        // Center the text vertically and horizontally inside the label
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Position it on the frame. Adjust size and position as needed.
        scoreLabel.setBounds(50, 50, 200, 50);

        // Finally, add to the frame (and ensure it's on top of backgrounds)
        frame.add(scoreLabel, 0);
    }
    // --- NEW: Separate label for “Limit: 5 Cars” ---
    private void initLimitLabel() {
        limitLabel = new JLabel("Limit: " + carLimit + " Cars");

        // Make label similarly styled
        limitLabel.setOpaque(true);
        limitLabel.setBackground(new Color(0, 0, 0, 180));
        limitLabel.setFont(new Font("Arial", Font.BOLD, 30));
        limitLabel.setForeground(Color.WHITE);
        limitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        limitLabel.setVerticalAlignment(SwingConstants.CENTER);
        limitLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2, true));

        // Position slightly below the score label
        limitLabel.setBounds(50, 110, 250, 50);
        frame.add(limitLabel, 0);
    }
    // --- NEW: Update limit label text
    private void updateLimitLabel() {
        limitLabel.setText("Limit: " + carLimit + " Cars");
    }
    // --------------------------------------------------------------------------------------------
    private JLabel loadBackground() {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/race1.png")));
        Image scaledImage = icon.getImage().getScaledInstance(GameConfig.FRAME_WIDTH, GameConfig.FRAME_HEIGHT, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }
    private JLabel loadCarImage() {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/player_car.png")));
        Image scaledImage = icon.getImage().getScaledInstance(GameConfig.CAR_WIDTH, GameConfig.CAR_HEIGHT, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }


    // not working
    private JLabel createPauseLabel() {
        JLabel label = new JLabel("Paused", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 48)); // Large font for visibility
        label.setForeground(Color.RED); // Bright red for contrast

        int labelWidth = 300;
        int labelHeight = 100;
        int x = (GameConfig.FRAME_WIDTH - labelWidth) / 2;
        int y = (GameConfig.FRAME_HEIGHT - labelHeight) / 2;

        label.setBounds(x, y, labelWidth, labelHeight); // Temporary bounds, will be centered later
        return label;
    }

    private void updatePauseLabelPosition() {
        int labelWidth = 300;
        int labelHeight = 100;
        int x = (frame.getWidth() - labelWidth) / 2;
        int y = (frame.getHeight() - labelHeight) / 2;
        pauseLabel.setBounds(x, y, labelWidth, labelHeight);
    }


    public void startGame() {
        // The main game timer
        new Timer(50, e -> {
            if (!gameOver && !paused) {

                // --- NEW - Faster: Check which keys are pressed and move car accordingly ---
                if (leftPressed) {
                    playerCar.moveLeft();
                }
                if (rightPressed) {
                    playerCar.moveRight();
                }
                if (upPressed) {
                    playerCar.moveUp();
                }
                if (downPressed) {
                    playerCar.moveDown();
                }
                // ---------------------------------------------------------------------------

                moveBackground();
                obstacleManager.moveObstacles();
                moveBullets();
                checkBulletCollisions();
                checkCollisions();
                checkObstaclesPassed();

                updateUI();
            }
        }).start();

        // The obstacle spawn timer
        new Timer(2000, e -> {
            if (!paused) {
                obstacleManager.spawnObstacle();
            }
        }).start();
    }

    /**
     * Checks which obstacles have passed below the player car and increments score.
     */
    private void checkObstaclesPassed() {
        int playerBottom = playerCar.getPositionY() + GameConfig.CAR_HEIGHT;

        for (JLabel obstacle : obstacleManager.getObstacles()) {
            // Make sure we track each obstacle in the map
            obstaclePassed.putIfAbsent(obstacle, false);

            // If not marked passed yet, and it's now below the player's bottom
            if (!obstaclePassed.get(obstacle) && obstacle.getY() > playerBottom) {
                obstaclePassed.put(obstacle, true);
                score++;
                updateScoreLabel();
            }
        }
    }

    /**
     * Updates the text of the score label to reflect the current score.
     */
    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }
    // ------------------------------------
    private void moveBackground() {
        backgroundY1 += GameConfig.BACKGROUND_SPEED;
        backgroundY2 += GameConfig.BACKGROUND_SPEED;

        if (backgroundY1 >= GameConfig.FRAME_HEIGHT) backgroundY1 = -GameConfig.FRAME_HEIGHT;
        if (backgroundY2 >= GameConfig.FRAME_HEIGHT) backgroundY2 = -GameConfig.FRAME_HEIGHT;

        background1.setBounds(0, backgroundY1, GameConfig.FRAME_WIDTH, GameConfig.FRAME_HEIGHT);
        background2.setBounds(0, backgroundY2, GameConfig.FRAME_WIDTH, GameConfig.FRAME_HEIGHT);
    }
    private void moveBullets() {
        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.moveUp();
            if (bullet.isOutOfScreen()) toRemove.add(bullet);
        }
        bullets.removeAll(toRemove);
        toRemove.forEach(b -> frame.remove(b.getBulletLabel()));
    }

    /**
     * Checks if bullets have collided with obstacles, removing both on collision
     * and decrementing the limit by 1. If limit hits 0, show Game Over -> go to Menu.
     */
    private void checkBulletCollisions() {
        // If we've already triggered the limit-based game over, skip
        if (limitReached) return;

        List<JLabel> obstaclesToRemove = new ArrayList<>();
        List<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            Rectangle bulletBounds = bullet.getBulletLabel().getBounds();
            for (JLabel obstacle : obstacleManager.getObstacles()) {
                if (bulletBounds.intersects(obstacle.getBounds())) {
                    obstaclesToRemove.add(obstacle);
                    bulletsToRemove.add(bullet);

                    // Only decrement limit if we haven't reached game over yet
                    if (!limitReached) {
                        carLimit--;
                        updateLimitLabel();

                        // If limit is now 0 or below, trigger game over once
                        if (carLimit <= 0) {
                            limitReached = true; // Prevent repeated triggers
                            backgroundMusic.stop();

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Game Over! You destroyed all 5 cars.",
                                    "Game Over",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            // Dispose of current game window, return to Menu
                            frame.dispose();
                            new Menu();
                            return; // Exit the entire method immediately
                        }
                    }

                    // Break out of obstacle loop for this bullet
                    break;
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
        bulletsToRemove.forEach(b -> frame.remove(b.getBulletLabel()));

        obstacleManager.getObstacles().removeAll(obstaclesToRemove);
        obstaclesToRemove.forEach(frame::remove);
    }


    private void checkCollisions() {
        Rectangle playerBounds = shrinkRectangle(playerCarLabel.getBounds(), 5, 10);

        for (JLabel obstacle : obstacleManager.getObstacles()) {
            Rectangle obstacleBounds = shrinkRectangle(obstacle.getBounds(), 5, 10);
            if (playerBounds.intersects(obstacleBounds)) {
                gameOver();
                break;
            }
        }
    }

    private Rectangle shrinkRectangle(Rectangle rect, int dx, int dy) {
        return new Rectangle(
                rect.x + dx,
                rect.y + dy,
                rect.width - 2 * dx,
                rect.height - 2 * dy
        );
    }

    private void gameOver() {
        gameOver = true;
        backgroundMusic.stop(); // Stop the music when the game ends
        JOptionPane.showMessageDialog(frame, "Game Over!", "Game Over", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    private void shootBullet() {
        // Adjust bullet starting position to center it above the player car
        int bulletWidth = 30;  // Match the bullet width in Bullet class
        int bulletHeight = 30; // Match the bullet height
        int bulletX = playerCar.getPositionX() + GameConfig.CAR_WIDTH / 2 - bulletWidth / 2;
        int bulletY = playerCar.getPositionY() - bulletHeight;

        // Create and add the bullet to the game
        Bullet bullet = new Bullet(bulletX, bulletY);
        bullets.add(bullet);
        frame.add(bullet.getBulletLabel(), 0); // Add bullet to the frame
        frame.repaint();
    }


    private void updateUI() {
        playerCarLabel.setBounds(playerCar.getPositionX(), playerCar.getPositionY(), GameConfig.CAR_WIDTH, GameConfig.CAR_HEIGHT);
        frame.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = !paused;
            if (paused) {
                updatePauseLabelPosition();
                pauseLabel.setVisible(true);
                backgroundMusic.stop(); // Pause music
            } else {
                pauseLabel.setVisible(false);
                backgroundMusic.playLoop(); // Resume music
            }
            frame.revalidate();
            frame.repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        if (!paused) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> leftPressed = true;
                case KeyEvent.VK_RIGHT -> rightPressed = true;
                case KeyEvent.VK_UP -> upPressed = true;
                case KeyEvent.VK_DOWN -> downPressed = true;
                case KeyEvent.VK_A -> shootBullet(); // Press 'A' to shoot
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = false;
            case KeyEvent.VK_RIGHT -> rightPressed = false;
            case KeyEvent.VK_UP -> upPressed = false;
            case KeyEvent.VK_DOWN -> downPressed = false;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
}
