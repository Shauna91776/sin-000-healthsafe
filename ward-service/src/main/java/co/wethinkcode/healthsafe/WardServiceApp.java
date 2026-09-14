package co.wethinkcode.healthsafe;

import io.javalin.Javalin;

import java.util.List;

public class WardServiceApp {


    public static void main(String[] args) {
        EquipmentFailurePublisher equipmentFailurePublisher =
                new EquipmentFailurePublisher();
        WardClient wardClient = new WardClient();
        MqSubscriber mqSubscriber = new MqSubscriber();
        Javalin app = Javalin.create().start(7031);

        app.get("/health", ctx -> ctx.result("OK"));
        try {
            mqSubscriber.subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // TODO (Provides lists of wards and departments.)
        // Add domain endpoints for ward-service here.
        app.get("/wards", ctx -> {

            List<Ward> wards = wardClient.getWards();

            ctx.json(wards);
        });

        app.get("/wards/{id}", ctx -> {

            String wardId = ctx.pathParam("id");

            List<Ward> wards = wardClient.getWards();

            for (Ward ward : wards) {
                if (ward.getWardId().equalsIgnoreCase(wardId)) {
                    ctx.json(ward);
                    return;
                }
            }

            ctx.status(404);
            ctx.result("Ward not found");
        });

        app.post("/wards/{id}/equipment-failure", ctx -> {

            String wardId = ctx.pathParam("id");

            Ward ward = null;
            List<Ward> wards = wardClient.getWards();

            for (Ward currentWard : wards) {
                if (currentWard.getWardId().equalsIgnoreCase(wardId)) {
                    ward = currentWard;
                    break;
                }
            }

            if (ward == null) {
                ctx.status(404);
                ctx.result("Ward not found");
                return;
            }

            EquipmentFailureEvent event =
                    ctx.bodyAsClass(EquipmentFailureEvent.class);

            EquipmentFailureEvent failureEvent =
                    new EquipmentFailureEvent(
                            ward.getWardId(),
                            event.getEquipment()
                    );

            try {
                equipmentFailurePublisher.publish(failureEvent);

                ctx.status(202);
                ctx.json(failureEvent);

            } catch (Exception e) {
                ctx.status(502);
                ctx.result("Unable to publish equipment failure");
            }
        });
    }
}

// MQ TODO: subscribes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.healthsafe.mq.MqConfig)
// MQ TODO: publishes to ActiveMQ queue MqConfig.QUEUE when it detects an equipment failure on one of its wards.
