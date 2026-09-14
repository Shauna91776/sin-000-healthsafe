package co.wethinkcode.healthsafe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EquipmentFailureConsumerTest {

    @Test
    void shouldProcessEquipmentFailureEvent() {
        co.wethinkcode.healthsafe.EquipmentFailureConsumer consumer =
                new EquipmentFailureConsumer();

        String result =
                consumer.processEvent("W-05,ventilator");

        assertEquals(
                "Equipment failure alert: ventilator failed in ward W-05",
                result
        );
    }
}