package Components;

// --- NEW - Nitrous:
public class Nitrous {
    private boolean active = false;

    // Activate nitrous
    public void activate() {
        active = true;
    }

    // Deactivate nitrous
    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
