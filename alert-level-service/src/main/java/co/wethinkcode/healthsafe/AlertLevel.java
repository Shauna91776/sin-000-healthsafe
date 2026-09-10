package co.wethinkcode.healthsafe;

public class AlertLevel {

    private int level;

    public AlertLevel() {
    }

    public AlertLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level < 0 || level > 8) {
            throw new IllegalArgumentException("Alert level must be between 0 and 8");
        }

        this.level = level;
    }
}