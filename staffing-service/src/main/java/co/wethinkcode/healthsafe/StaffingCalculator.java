package co.wethinkcode.healthsafe;

public class StaffingCalculator {

    public int calculateDoctorsOnCall(int alertLevel) {

        if (alertLevel <= 2) {
            return 1;
        } else if (alertLevel <= 5) {
            return 2;
        } else {
            return 3;
        }
    }
}