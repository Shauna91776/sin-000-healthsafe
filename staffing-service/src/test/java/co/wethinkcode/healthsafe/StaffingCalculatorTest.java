package co.wethinkcode.healthsafe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StaffingCalculatorTest {

    @Test
    void alertLevelZeroToTwoShouldHaveOneDoctor() {
        StaffingCalculator calculator = new StaffingCalculator();

        assertEquals(1, calculator.calculateDoctorsOnCall(0));
        assertEquals(1, calculator.calculateDoctorsOnCall(2));
    }

    @Test
    void alertLevelThreeToFiveShouldHaveTwoDoctors() {
        StaffingCalculator calculator = new StaffingCalculator();

        assertEquals(2, calculator.calculateDoctorsOnCall(3));
        assertEquals(2, calculator.calculateDoctorsOnCall(5));
    }

    @Test
    void alertLevelSixToEightShouldHaveThreeDoctors() {
        StaffingCalculator calculator = new StaffingCalculator();

        assertEquals(3, calculator.calculateDoctorsOnCall(6));
        assertEquals(3, calculator.calculateDoctorsOnCall(8));
    }
}