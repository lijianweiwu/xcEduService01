package com.xuecheng.test.rabbitmq;

import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer05_topics_springboot {
    //引入mq模板
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testSendByTopics() {
        for (int i = 1; i <= 5; i++) {
            String message = "sms inform to user"+i;
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM,"inform.sms",message);
            System.out.println("Send Message is:'" + message + "'");
        }

    }
}