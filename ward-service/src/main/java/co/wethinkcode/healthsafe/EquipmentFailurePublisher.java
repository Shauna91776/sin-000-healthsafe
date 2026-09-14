package co.wethinkcode.healthsafe;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import co.wethinkcode.healthsafe.mq.MqConfig;
import javax.jms.DeliveryMode;

public class EquipmentFailurePublisher {

    public void publish(EquipmentFailureEvent event) throws Exception {

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(MqConfig.BROKER_URL);

        Connection connection = connectionFactory.createConnection();

        Session session = connection.createSession(
                false,
                Session.AUTO_ACKNOWLEDGE
        );

        javax.jms.Queue queue = session.createQueue(MqConfig.QUEUE);

        MessageProducer producer = session.createProducer(queue);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        String message = event.getWardId() + "," + event.getEquipment();

        TextMessage textMessage = session.createTextMessage(message);

        producer.send(textMessage);

        producer.close();
        session.close();
        connection.close();
    }
}