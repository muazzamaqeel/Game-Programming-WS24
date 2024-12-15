import Components.PlayerCar;
import StateManagement.GameConfig;
import StateManagement.ObstacleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Racing implements KeyListener {
    private final PlayerCar playerCar;
    private final ObstacleManager obstacleManager;

    private JLabel playerCarLabel;
    private JLabel background1;
    private JLabel background2;

    private JFrame frame;
    private int backgroundY1 = 0;
    private int backgroundY2 = -GameConfig.FRAME_HEIGHT;
    private boolean gameOver;

    Racing() {
        playerCar = new PlayerCar(GameConfig.LEFT_MARGIN, GameConfig.FRAME_HEIGHT - 150);
        frame = new JFrame("Racing Game");
        obstacleManager = new ObstacleManager(frame);
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

        frame.add(background1);
        frame.add(background2);
        frame.add(playerCarLabel, 0);

        frame.addKeyListener(this);
        frame.setVisible(true);

        startGame();
    }

    private JLabel loadBackground() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Resources/race1.png"));
        Image scaledImage = icon.getImage().getScaledInstance(GameConfig.FRAME_WIDTH, GameConfig.FRAME_HEIGHT, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    private JLabel loadCarImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Resources/player_car.png"));
        Image scaledImage = icon.getImage().getScaledInstance(GameConfig.CAR_WIDTH, GameConfig.CAR_HEIGHT, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    public void startGame() {
        new Timer(50, e -> {
            if (!gameOver) {
                moveBackground();
                obstacleManager.moveObstacles();
                checkCollisions();
                updateUI();
            }
        }).start();

        new Timer(2000, e -> obstacleManager.spawnObstacle()).start();
    }

    private void moveBackground() {
        backgroundY1 += GameConfig.BACKGROUND_SPEED;
        backgroundY2 += GameConfig.BACKGROUND_SPEED;

        if (backgroundY1 >= GameConfig.FRAME_HEIGHT) backgroundY1 = -GameConfig.FRAME_HEIGHT;
        if (backgroundY2 >= GameConfig.FRAME_HEIGHT) backgroundY2 = -GameConfig.FRAME_HEIGHT;

        background1.setBounds(0, backgroundY1, GameConfig.FRAME_WIDTH, GameConfig.FRAME_HEIGHT);
        background2.setBounds(0, backgroundY2, GameConfig.FRAME_WIDTH, GameConfig.FRAME_HEIGHT);
    }

    private void checkCollisions() {
        // Get shrunken bounds for the player car
        Rectangle playerBounds = shrinkRectangle(playerCarLabel.getBounds(), 5, 10);

        for (JLabel obstacle : obstacleManager.getObstacles()) {
            // Get shrunken bounds for each obstacle
            Rectangle obstacleBounds = shrinkRectangle(obstacle.getBounds(), 5, 10);

            // Check for intersection between the shrunken bounds
            if (playerBounds.intersects(obstacleBounds)) {
                gameOver();
                break;
            }
        }
    }

    // Helper method to shrink a rectangle (add padding)
    private Rectangle shrinkRectangle(Rectangle rect, int dx, int dy) {
        return new Rectangle(
                rect.x + dx,                // Move x inward
                rect.y + dy,                // Move y inward
                rect.width - 2 * dx,        // Reduce width
                rect.height - 2 * dy        // Reduce height
        );
    }


    private void gameOver() {
        gameOver = true;
        JOptionPane.showMessageDialog(frame, "Game Over!", "Game Over", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    private void updateUI() {
        playerCarLabel.setBounds(playerCar.getPositionX(), playerCar.getPositionY(), GameConfig.CAR_WIDTH, GameConfig.CAR_HEIGHT);
        frame.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> playerCar.moveLeft();
            case KeyEvent.VK_RIGHT -> playerCar.moveRight();
            case KeyEvent.VK_UP -> playerCar.moveUp();
            case KeyEvent.VK_DOWN -> playerCar.moveDown();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
