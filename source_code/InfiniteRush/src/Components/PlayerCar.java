package Components;

import Interfaces.Observer;

public class PlayerCar implements Observer {
    private int positionX = 431; // Initial X position
    private int positionY = 445; // Initial Y position
    private final int normalSpeed = 3;
    private final int nitroSpeed = 100;
    private int currentSpeed = normalSpeed; // Default speed
    private boolean nitroActive = false; // Indicates if nitro is active

    @Override
    public void update() {
        // Update logic if needed
    }

    public void moveLeft() {
        positionX = Math.max(185, positionX - currentSpeed);
    }

    public void moveRight() {
        positionX = Math.min(520, positionX + currentSpeed);
    }

    public void moveUp() {
        positionY = Math.max(0, positionY - currentSpeed);
    }

    public void moveDown() {
        positionY = Math.min(445, positionY + currentSpeed);
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void activateNitro() {
        nitroActive = true;
        currentSpeed = nitroSpeed; // Set the speed to nitro
    }

    public void deactivateNitro() {
        nitroActive = false;
        currentSpeed = normalSpeed; // Reset speed to normal
    }

    public boolean isNitroActive() {
        return nitroActive;
    }
}
