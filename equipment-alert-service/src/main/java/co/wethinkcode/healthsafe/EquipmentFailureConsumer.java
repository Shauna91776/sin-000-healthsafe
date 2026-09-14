package co.wethinkcode.healthsafe;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import co.wethinkcode.healthsafe.mq.MqConfig;

public class EquipmentFailureConsumer {

    public void consume() throws Exception {

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(MqConfig.BROKER_URL);

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(
                false,
                Session.CLIENT_ACKNOWLEDGE
        );

        Queue queue = session.createQueue(MqConfig.QUEUE);

        MessageConsumer consumer = session.createConsumer(queue);

        consumer.setMessageListener(message -> {

            try {
                if (message instanceof javax.jms.TextMessage) {

                    String text =
                            ((javax.jms.TextMessage) message).getText();

                    System.out.println(processEvent(text));

                    message.acknowledge();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public String processEvent(String text) {

        String[] parts = text.split(",");

        String wardId = parts[0];
        String equipment = parts[1];

        return "Equipment failure alert: "
                + equipment
                + " failed in ward "
                + wardId;
    }
}