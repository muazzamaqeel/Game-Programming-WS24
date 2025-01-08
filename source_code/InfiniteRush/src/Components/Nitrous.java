package Components;
public class Nitrous {
    private boolean active = false;

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
