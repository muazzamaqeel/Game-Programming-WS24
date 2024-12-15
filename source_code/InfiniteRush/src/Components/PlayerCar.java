package Components;

import StateManagement.GameConfig;

public class PlayerCar {
    private int positionX;
    private int positionY;
    private final int speed;

    public PlayerCar(int startX, int startY) {
        this.positionX = startX;
        this.positionY = startY;
        this.speed = GameConfig.NORMAL_SPEED;
    }

    public void moveLeft() {
        positionX = Math.max(GameConfig.LEFT_MARGIN, positionX - speed);
    }

    public void moveRight() {
        positionX = Math.min(GameConfig.RIGHT_MARGIN, positionX + speed);
    }

    public void moveUp() {
        positionY = Math.max(0, positionY - speed);
    }

    public void moveDown() {
        positionY = Math.min(GameConfig.FRAME_HEIGHT - GameConfig.CAR_HEIGHT, positionY + speed);
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }
}
