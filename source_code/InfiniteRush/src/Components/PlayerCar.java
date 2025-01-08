package Components;

import StateManagement.GameConfig;

public class PlayerCar {
    private int positionX;
    private int positionY;
    private final Nitrous nitrous = new Nitrous();

    public PlayerCar(int startX, int startY) {
        this.positionX = startX;
        this.positionY = startY;
    }

    // --- NEW - Nitrous: public methods to toggle
    public void enableNitrous() {
        nitrous.activate();
    }

    public void disableNitrous() {
        nitrous.deactivate();
    }

    // Now each move uses nitrous if active, otherwise normal speed

    public void moveLeft() {
        int usedSpeed = nitrous.isActive() ? GameConfig.NITRO_SPEED : GameConfig.NORMAL_SPEED;
        positionX = Math.max(GameConfig.LEFT_MARGIN, positionX - usedSpeed);
    }

    public void moveRight() {
        int usedSpeed = nitrous.isActive() ? GameConfig.NITRO_SPEED : GameConfig.NORMAL_SPEED;
        positionX = Math.min(GameConfig.RIGHT_MARGIN, positionX + usedSpeed);
    }

    public void moveUp() {
        int usedSpeed = nitrous.isActive() ? GameConfig.NITRO_SPEED : GameConfig.NORMAL_SPEED;
        positionY = Math.max(0, positionY - usedSpeed);
    }

    public void moveDown() {
        int usedSpeed = nitrous.isActive() ? GameConfig.NITRO_SPEED : GameConfig.NORMAL_SPEED;
        positionY = Math.min(GameConfig.FRAME_HEIGHT - GameConfig.CAR_HEIGHT, positionY + usedSpeed);
    }

    // Accessors
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
}
