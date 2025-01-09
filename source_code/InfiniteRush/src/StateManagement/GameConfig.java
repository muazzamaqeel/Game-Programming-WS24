package StateManagement;

import java.awt.*;

public class GameConfig {
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int FRAME_WIDTH = SCREEN_SIZE.width;
    public static final int FRAME_HEIGHT = SCREEN_SIZE.height;
    public static final int CAR_WIDTH = 50;
    public static final int CAR_HEIGHT = 100;
    public static final int NORMAL_SPEED = 15;
    public static final int NITRO_SPEED = 50;
    public static final int OBSTACLE_SPEED = 8;
    public static final int MARGIN_OFFSET = -50;
    public static final int LEFT_MARGIN = FRAME_WIDTH / 4 + MARGIN_OFFSET;
    public static final int RIGHT_MARGIN = FRAME_WIDTH * 3 / 4 - CAR_WIDTH - MARGIN_OFFSET;

    // Background speed
    public static final int BACKGROUND_SPEED = 5;
}
