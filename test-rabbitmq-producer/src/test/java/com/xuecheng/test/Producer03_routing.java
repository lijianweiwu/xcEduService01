package com.xuecheng.test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * mq路由模式生产者
 */
public class Producer03_routing {
    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_ROUTING_INFORM="exchange_routing_inform";
    public static void main(String[] args) {
        //通过连接工厂创建新的连接和mq连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);//端口
        connectionFactory.setUsername("guest");//用户名
        connectionFactory.setPassword("guest");//密码
        connectionFactory.setVirtualHost("/");//rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务器（可多个）
        //创建与RabbitMQ服务的TCP连接
        Connection connection=null;
        Channel channel=null;

        try {
            //创建连接
            connection= connectionFactory.newConnection();
            //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            channel= connection.createChannel();
            //声明交换机 String exchange, BuiltinExchangeType type
            /**
             * 参数明细
             * 1、交换机名称
             * 2、交换机类型，fanout、topic、direct、headers
             */
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);
            //声明队列
            // (String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
            /**
             * 参数明细：
             * 1、队列名称
             * 2、是否持久化
             * 3、是否独占此队列
             * 4、队列不用是否自动删除
             * 5、参数
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            //交换机和队列绑定String queue, String exchange, String routingKey
            /**
             * 参数明细
             * 1、队列名称
             * 2、交换机名称
             * 3、路由key
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_SMS);
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,"inform");
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,"inform");



            /**
             * 消息发布方法
             * param1：Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
             * param3:消息包含的属性
             * param4：消息体
             */
            /**
             * 这里没有指定交换机，消息将发送给默认交换机，每个队列也会绑定那个默认的交换机，但是不能显示绑定或解除绑定
             *　默认的交换机，routingKey等于队列名称
             */

            for (int i=1;i<=5;i++){
                String inform = "inform to user"+i;
                String email = "email to user"+i;
                String sms = "sms to user"+i;

                //向交换机发送消息 String exchange, String routingKey, BasicProperties props, byte[] body
                /**
                 * 参数明细
                 * 1、交换机名称，不指令使用默认交换机名称 Default Exchange
                 * 2、routingKey（路由key），根据key名称将消息转发到具体的队列，这里填写队列名称表示消息将发到此队列
                 * 3、消息属性
                 * 4、消息内容
                 */
                channel.basicPublish(EXCHANGE_ROUTING_INFORM, "inform", null, inform.getBytes());
                System.out.println("Send Message is:'" + inform + "'");
                channel.basicPublish(EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_EMAIL, null, email.getBytes());
                System.out.println("Send Message is:'" + email + "'");
                channel.basicPublish(EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_SMS, null, sms.getBytes());
                System.out.println("Send Message is:'" + sms + "'");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();//先关闭通道
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();//最后关闭连接
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
