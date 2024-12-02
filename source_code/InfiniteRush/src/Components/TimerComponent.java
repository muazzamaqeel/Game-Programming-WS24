package Components;

// Import the custom Observer interface correctly

import Interfaces.Observer;

public class TimerComponent implements Observer {
    private int minutes = 0;
    private int seconds = 0;

    @Override
    public void update() {
        seconds++;
        if (seconds == 60) {
            seconds = 0;
            minutes++;
        }
    }

    public String getFormattedTime() {
        return String.format("%02d:%02d", minutes, seconds);
    }
}
