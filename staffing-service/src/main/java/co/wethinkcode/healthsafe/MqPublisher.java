package co.wethinkcode.healthsafe;

import co.wethinkcode.healthsafe.mq.MqConfig;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MqPublisher {

    public void publish(StaffingEvent event) throws Exception {

        ConnectionFactory factory =
                new ActiveMQConnectionFactory(MqConfig.BROKER_URL);

        try (Connection connection = factory.createConnection()) {

            Session session = connection.createSession(
                    false,
                    Session.AUTO_ACKNOWLEDGE
            );

            var topic = session.createTopic(MqConfig.TOPIC);

            MessageProducer producer = session.createProducer(topic);

            String message = event.getWardId() + "," + event.getAlertLevel();

            TextMessage textMessage = session.createTextMessage(message);

            producer.send(textMessage);
        }
    }
}