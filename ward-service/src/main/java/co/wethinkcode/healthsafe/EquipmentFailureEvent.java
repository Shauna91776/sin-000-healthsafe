package co.wethinkcode.healthsafe;

public class EquipmentFailureEvent {

    private String wardId;
    private String equipment;

    public EquipmentFailureEvent() {
    }

    public EquipmentFailureEvent(String wardId, String equipment) {
        this.wardId = wardId;
        this.equipment = equipment;
    }

    public String getWardId() {
        return wardId;
    }

    public String getEquipment() {
        return equipment;
    }
}