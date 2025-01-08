package Windows;

import Components.PlayerCar;
import Components.Bullet;
import StateManagement.GameConfig;
import StateManagement.ObstacleManager;
import Components.Music;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Racing implements KeyListener {
    private final PlayerCar playerCar;
    private final ObstacleManager obstacleManager;
    private final List<Bullet> bullets = new ArrayList<>();

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


        frame.addKeyListener(this);
        frame.setVisible(true);

        startGame();

        frame.add(pauseLabel);
        frame.setComponentZOrder(pauseLabel, 0);
        pauseLabel.setVisible(false);

    }
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
        new Timer(50, e -> {
            if (!gameOver && !paused) {
                moveBackground();
                obstacleManager.moveObstacles();
                moveBullets();
                checkBulletCollisions();
                checkCollisions();
                updateUI();
            }
        }).start();

        new Timer(2000, e -> {
            if (!paused) {
                obstacleManager.spawnObstacle();
            }
        }).start();
    }
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

    private void checkBulletCollisions() {
        List<JLabel> obstaclesToRemove = new ArrayList<>();
        List<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            Rectangle bulletBounds = bullet.getBulletLabel().getBounds();
            for (JLabel obstacle : obstacleManager.getObstacles()) {
                if (bulletBounds.intersects(obstacle.getBounds())) {
                    obstaclesToRemove.add(obstacle);
                    bulletsToRemove.add(bullet);
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
        if(!paused){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> playerCar.moveLeft();
            case KeyEvent.VK_RIGHT -> playerCar.moveRight();
            case KeyEvent.VK_UP -> playerCar.moveUp();
            case KeyEvent.VK_DOWN -> playerCar.moveDown();
            case KeyEvent.VK_A -> shootBullet(); // Press 'A' to shoot
        }
    }
}

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
