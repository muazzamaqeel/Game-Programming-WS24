package Components;

import Interfaces.Observer;

public class CarComponent implements Observer {
    private int positionX = 431;
    private int positionY = 445;

    @Override
    public void update() {
        // No additional logic needed for UI update
    }

    public void moveLeft() {
        positionX = Math.max(185, positionX - 3); // Prevent moving out of bounds
    }

    public void moveRight() {
        positionX = Math.min(520, positionX + 3); // Prevent moving out of bounds
    }

    public void moveUp() {
        positionY = Math.max(0, positionY - 3); // Prevent moving out of bounds
    }

    public void moveDown() {
        positionY = Math.min(445, positionY + 3); // Prevent moving out of bounds
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }
}
