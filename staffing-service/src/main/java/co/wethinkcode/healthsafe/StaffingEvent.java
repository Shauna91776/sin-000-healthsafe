package co.wethinkcode.healthsafe;

public class StaffingEvent {

    private String wardId;
    private int alertLevel;

    public StaffingEvent() {
    }

    public StaffingEvent(String wardId, int alertLevel) {
        this.wardId = wardId;
        this.alertLevel = alertLevel;
    }

    public String getWardId() {
        return wardId;
    }

    public int getAlertLevel() {
        return alertLevel;
    }
}