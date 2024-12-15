package Components;

import javax.swing.*;
import java.awt.*;

public class Bullet {
    private int positionX;
    private int positionY;
    private final JLabel bulletLabel;

    public Bullet(int startX, int startY) {
        this.positionX = startX;
        this.positionY = startY;

        // Bullet size (increase dimensions here)
        int bulletWidth = 30;
        int bulletHeight = 50;

        // Load the bullet image
        ImageIcon icon = new ImageIcon(getClass().getResource("/Resources/bullet.png"));
        Image scaledImage = icon.getImage().getScaledInstance(bulletWidth, bulletHeight, Image.SCALE_SMOOTH);
        bulletLabel = new JLabel(new ImageIcon(scaledImage));

        // Set the bullet's position and size
        bulletLabel.setBounds(positionX, positionY, bulletWidth, bulletHeight);
    }

    // Move the bullet upwards
    public void moveUp() {
        positionY -= 20; // Speed of the bullet
        bulletLabel.setBounds(positionX, positionY, bulletLabel.getWidth(), bulletLabel.getHeight());
    }

    // Check if the bullet is out of the screen
    public boolean isOutOfScreen() {
        return positionY + bulletLabel.getHeight() < 0;
    }

    public JLabel getBulletLabel() {
        return bulletLabel;
    }
}
