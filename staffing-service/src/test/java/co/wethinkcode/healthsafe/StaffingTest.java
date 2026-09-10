//package test.java.co.wethinkcode.healthsafe;

package co.wethinkcode.healthsafe;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StaffingTest {

    @Test
    void shouldCreateStaffingWithCorrectValues() {

        Staffing staffing = new Staffing(
                "W-01",
                5,
                "2 doctors on call"
        );

        assertEquals("W-01", staffing.getWardId());
        assertEquals(5, staffing.getAlertLevel());
        assertEquals("2 doctors on call", staffing.getOnCallDoctor());
    }
}