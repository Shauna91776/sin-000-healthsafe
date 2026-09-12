package co.wethinkcode.healthsafe;

import io.javalin.Javalin;
import java.util.List;
import java.io.IOException;

public class StaffingServiceApp {

    public static void main(String[] args) {
        AlertLevelClient alertLevelClient = new AlertLevelClient();
        WardClient wardClient = new WardClient();
        MqPublisher mqPublisher = new MqPublisher();
        Javalin app = Javalin.create().start(7033);

        app.get("/health", ctx -> ctx.result("OK"));


        // TODO (Provides on-call schedules for doctors based on ward and status.)
        // Add domain endpoints for staffing-service here.




        app.get("/staffing/{id}", ctx -> {

            String wardId = ctx.pathParam("id");

            try {
                Ward ward = wardClient.getWard(wardId);

                if (ward == null) {
                    ctx.status(404);
                    ctx.result("Ward not found");
                    return;
                }

                int alertLevel = alertLevelClient.getAlertLevel();

                StaffingCalculator calculator = new StaffingCalculator();
                int doctorsOnCall = calculator.calculateDoctorsOnCall(alertLevel);

                mqPublisher.publish(new StaffingEvent(
                        ward.getWardId(),
                        alertLevel
                ));

                Staffing staffing = new Staffing(
                        ward.getWardId(),
                        alertLevel,
                        doctorsOnCall + " doctors on call"
                );

                ctx.json(staffing);

            } catch (IOException | InterruptedException e) {
                ctx.status(502);
                ctx.result("Downstream service unavailable");
            }
        });
    }
}

// MQ TODO: publishes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.healthsafe.mq.MqConfig)
