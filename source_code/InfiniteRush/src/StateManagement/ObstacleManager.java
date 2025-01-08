package StateManagement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ObstacleManager {
    private final List<JLabel> obstacles;
    private final JFrame frame;
    private final Random random;

    // --- NEW - Difficulty: track current obstacle speed
    private int currentSpeed;

    public ObstacleManager(JFrame frame) {
        this.frame = frame;
        this.obstacles = new ArrayList<>();
        this.random = new Random();

        // Initialize speed to the default from GameConfig
        this.currentSpeed = GameConfig.OBSTACLE_SPEED;
    }

    public void spawnObstacle() {
        for (int i = 0; i < 3; i++) {
            JLabel obstacle = new JLabel();
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/car_mask.png")));
            Image scaledImage = icon.getImage().getScaledInstance(GameConfig.CAR_WIDTH, GameConfig.CAR_HEIGHT, Image.SCALE_SMOOTH);
            obstacle.setIcon(new ImageIcon(scaledImage));

            int x = random.nextInt(GameConfig.RIGHT_MARGIN - GameConfig.LEFT_MARGIN) + GameConfig.LEFT_MARGIN;
            obstacle.setBounds(x, -100 - (i * 150), GameConfig.CAR_WIDTH, GameConfig.CAR_HEIGHT);

            obstacles.add(obstacle);
            frame.add(obstacle, 0);
        }
    }

    public void moveObstacles() {
        List<JLabel> toRemove = new ArrayList<>();
        for (JLabel obstacle : obstacles) {
            // Use currentSpeed instead of GameConfig.OBSTACLE_SPEED
            int newY = obstacle.getY() + currentSpeed;
            obstacle.setBounds(obstacle.getX(), newY, GameConfig.CAR_WIDTH, GameConfig.CAR_HEIGHT);

            if (newY > GameConfig.FRAME_HEIGHT) {
                toRemove.add(obstacle);
            }
        }
        obstacles.removeAll(toRemove);
        toRemove.forEach(frame::remove);
    }


    // --- NEW - Difficulty: allow external classes to raise the speed
    public void increaseSpeed(int amount) {
        this.currentSpeed += amount;
        // Optionally clamp or log to avoid too big speeds if you want
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public List<JLabel> getObstacles() {
        return obstacles;
    }
}
