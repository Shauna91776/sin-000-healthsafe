package co.wethinkcode.healthsafe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MqSubscriberTest {

    @Test
    void shouldProcessStaffingEvent() {
        co.wethinkcode.healthsafe.MqSubscriber subscriber = new co.wethinkcode.healthsafe.MqSubscriber();

        String result = subscriber.processEvent("W-01,0");

        assertEquals(
                "Ward W-01 received staffing update. Alert level: 0",
                result
        );
    }
}