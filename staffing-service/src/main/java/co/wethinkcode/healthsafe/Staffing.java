package co.wethinkcode.healthsafe;

public class Staffing {

    private String wardId;
    private int alertLevel;
    private String onCallDoctor;

    public Staffing(String wardId, int alertLevel, String onCallDoctor) {
        this.wardId = wardId;
        this.alertLevel = alertLevel;
        this.onCallDoctor = onCallDoctor;
    }

    public String getWardId() {
        return wardId;
    }

    public int getAlertLevel() {
        return alertLevel;
    }

    public String getOnCallDoctor() {
        return onCallDoctor;
    }
}