package co.wethinkcode.healthsafe;

import io.javalin.Javalin;

import java.util.List;

public class WardServiceApp {

    public static void main(String[] args) {
        WardClient wardClient = new WardClient();
        Javalin app = Javalin.create().start(7031);

        app.get("/health", ctx -> ctx.result("OK"));


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
    }
}

// MQ TODO: subscribes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.healthsafe.mq.MqConfig)
// MQ TODO: publishes to ActiveMQ queue MqConfig.QUEUE when it detects an equipment failure on one of its wards.
