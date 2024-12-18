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

    public ObstacleManager(JFrame frame) {
        this.frame = frame;
        this.obstacles = new ArrayList<>();
        this.random = new Random();
    }

    public void spawnObstacle() {
        for (int i = 0; i < 3; i++) { // Spawn 3 cars at a time
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
            int newY = obstacle.getY() + GameConfig.OBSTACLE_SPEED;
            obstacle.setBounds(obstacle.getX(), newY, GameConfig.CAR_WIDTH, GameConfig.CAR_HEIGHT);
            if (newY > GameConfig.FRAME_HEIGHT) toRemove.add(obstacle);
        }
        obstacles.removeAll(toRemove);
        toRemove.forEach(frame::remove);
    }

    public List<JLabel> getObstacles() {
        return obstacles;
    }
}
