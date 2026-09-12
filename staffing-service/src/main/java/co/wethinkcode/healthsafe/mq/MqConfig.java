package co.wethinkcode.healthsafe.mq;

/**
 * Shared by every producer/consumer service that talks to the "staffing-events-topic"
 * ActiveMQ topic. Duplicated into each participating service's own source tree,
 * since these are independent Maven projects with no shared parent pom.
 */
public final class MqConfig {

//    tell services where mq is running
    public static final String BROKER_URL = "tcp://localhost:61616";
//    destination where events will be published
    public static final String TOPIC = "staffing-events-topic";

    private MqConfig() {
    }
}
