import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing Racing Game...");
        Racing game = new Racing();
        game.start();
    }
}

class Racing extends Thread implements KeyListener {
    JFrame f;
    JLayeredPane pane;
    JLabel back, car, arrow, timer;

    Racing() {
        // Initialize JFrame
        f = new JFrame("Racing");
        f.setSize(740, 530);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);
        f.setResizable(false);

        // Initialize labels and components
        back = new JLabel();
        back.setBounds(0, 0, 740, 500);

        car = new JLabel();
        car.setBounds(431, 445, 36, 57);

        arrow = new JLabel();
        arrow.setBounds(280, 250, 200, 150);

        timer = new JLabel("00:00", JLabel.CENTER);
        timer.setFont(new Font("Arial", Font.BOLD, 20));
        timer.setForeground(Color.RED);
        timer.setBounds(600, 10, 100, 50);

        // Load resources
        car.setIcon(new ImageIcon(getClass().getResource("/Resources/car_mask.png")));
        arrow.setIcon(new ImageIcon(getClass().getResource("/Resources/arrow.png")));
        back.setIcon(new ImageIcon(getClass().getResource("/Resources/race1.png")));

        // Add components to layered pane
        pane = f.getLayeredPane();
        pane.add(back, new Integer(1));
        pane.add(car, new Integer(2));
        pane.add(arrow, new Integer(2));
        pane.add(timer, new Integer(2));

        // Add key listener
        f.addKeyListener(this);
    }

    int time_min = 0, time_sec = 0, time_msec = 0;

    public void run() {
        while (true) {
            try {
                // Update timer and background
                timer.setText(String.format("%02d:%02d", time_min, time_sec));
                back.setIcon(new ImageIcon(getClass().getResource("/Resources/race1.png")));
                Thread.sleep(100);
                time_msec += 100;
                if (time_msec == 1000) {
                    time_sec++;
                    time_msec = 0;
                }
                if (time_sec == 60) {
                    time_min++;
                    time_sec = 0;
                }
                timer.setText(String.format("%02d:%02d", time_min, time_sec));
                back.setIcon(new ImageIcon(getClass().getResource("/Resources/race2.png")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void keyPressed(KeyEvent ke) {
        pane.remove(arrow); // Remove arrow on key press
        int c = ke.getKeyCode();
        if (c == KeyEvent.VK_LEFT && car.getX() >= 185) {
            car.setBounds(car.getX() - 3, car.getY(), 36, 57);
        } else if (c == KeyEvent.VK_RIGHT && car.getX() <= 520) {
            car.setBounds(car.getX() + 3, car.getY(), 36, 57);
        } else if (c == KeyEvent.VK_UP && car.getY() >= 0) {
            car.setBounds(car.getX(), car.getY() - 3, 36, 57);
        } else if (c == KeyEvent.VK_DOWN && car.getY() <= 445) {
            car.setBounds(car.getX(), car.getY() + 3, 36, 57);
        }
    }

    public void keyReleased(KeyEvent ke1) {}

    public void keyTyped(KeyEvent ke2) {}
}
