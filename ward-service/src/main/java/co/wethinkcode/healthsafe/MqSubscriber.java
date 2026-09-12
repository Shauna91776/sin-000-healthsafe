package co.wethinkcode.healthsafe;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import co.wethinkcode.healthsafe.mq.MqConfig;

public class MqSubscriber {

    public void subscribe() throws Exception {

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(MqConfig.BROKER_URL);

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(MqConfig.TOPIC);

        MessageConsumer consumer = session.createConsumer(topic);

        consumer.setMessageListener(message -> {

            try {
                if (message instanceof javax.jms.TextMessage) {
                    String text = ((javax.jms.TextMessage) message).getText();

                    System.out.println(processEvent(text));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public String processEvent(String text) {
        String[] parts = text.split(",");
        String wardId = parts[0];
        int alertLevel = Integer.parseInt(parts[1]);

        return "Ward " + wardId + " received staffing update. Alert level: " + alertLevel;
    }
}
