import Components.CarComponent;
import Components.TimerComponent;
import Interfaces.Subject;
import StateManagement.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

class Racing implements KeyListener {
    private final Subject gameState; // Use the Interfaces.Subject interface
    private final TimerComponent timer;
    private final CarComponent car;
    private final JLabel carLabel;
    private final JLabel timerLabel;
    private final JLabel background;

    Racing() {
        // Initialize game logic components
        gameState = new GameState(); // Concrete implementation assigned
        timer = new TimerComponent();
        car = new CarComponent();

        // Register observers
        gameState.addObserver(timer);
        gameState.addObserver(car);

        // Initialize Swing components
        JFrame frame = new JFrame("Racing Game");
        frame.setSize(740, 530);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        background = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/race1.png"))));
        background.setBounds(0, 0, 740, 500);

        carLabel = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/car_mask.png"))));
        carLabel.setBounds(car.getPositionX(), car.getPositionY(), 36, 57);

        timerLabel = new JLabel("00:00", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.RED);
        timerLabel.setBounds(600, 10, 100, 30);

        frame.add(carLabel);
        frame.add(timerLabel);
        frame.add(background);

        frame.addKeyListener(this);
        frame.setVisible(true);
    }

    public void startGame() {
        // Simulate game loop
        new Timer(1000, e -> {
            gameState.notifyObservers(); // Use the Interfaces.Subject interface method
            updateUI();
        }).start();
    }

    private void updateUI() {
        // Update timer label
        timerLabel.setText(timer.getFormattedTime());

        // Update car position
        carLabel.setBounds(car.getPositionX(), car.getPositionY(), 36, 57);

        // Update background (optional animation can be added here)
        background.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/race1.png"))));
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyCode = ke.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            car.moveLeft();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            car.moveRight();
        } else if (keyCode == KeyEvent.VK_UP) {
            car.moveUp();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            car.moveDown();
        }
        gameState.notifyObservers(); // Use the Interfaces.Subject interface method
        updateUI(); // Update the UI immediately after the movement
    }

    @Override
    public void keyReleased(KeyEvent ke) {}

    @Override
    public void keyTyped(KeyEvent ke) {}
}
