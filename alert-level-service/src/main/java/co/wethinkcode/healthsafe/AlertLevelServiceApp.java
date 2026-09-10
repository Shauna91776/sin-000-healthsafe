package co.wethinkcode.healthsafe;

import io.javalin.Javalin;

public class AlertLevelServiceApp {

    public static void main(String[] args) {
        co.wethinkcode.healthsafe.AlertLevel alertLevel = new co.wethinkcode.healthsafe.AlertLevel(0);
        Javalin app = Javalin.create().start(7032);

        app.get("/health", ctx -> ctx.result("OK"));
        app.get("/alert-level", ctx -> ctx.json(alertLevel));
        app.put("/alert-level", ctx -> {

            try {
                int newLevel = ctx.bodyAsClass(co.wethinkcode.healthsafe.AlertLevel.class).getLevel();

                alertLevel.setLevel(newLevel);

                ctx.json(alertLevel);

            } catch (Exception e) {
                ctx.status(400);
                ctx.result("Alert level must be between 0 and 8");
            }
        });

        // TODO (Tracks the hospital Emergency Status (0-8, 8 = full Code Blue).)
        // Add domain endpoints for alert-level-service here.
    }
}
