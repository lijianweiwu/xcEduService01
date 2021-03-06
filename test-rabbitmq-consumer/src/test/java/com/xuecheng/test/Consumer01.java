package com.xuecheng.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者入门程序
 */
public class Consumer01 {
    //队列名称
    private static final String QUEUE = "helloworld";
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
            /**
             * 声明队列，如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE, true, false, false, null);


            DefaultConsumer consumer = new DefaultConsumer(channel){
                /**
                 * 当消费者监听到消息这个方法就会被调用
                 * @param consumerTag 消费者标签，可设可不设，在监听队列设置
                 * @param envelope  信封
                 * @param properties 消息属性
                 * @param body
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String exchange = envelope.getExchange();//可以拿到交换机
                    long deliveryTag = envelope.getDeliveryTag();//消息的id
                    String message = new String(body, "utf-8");//把字节转为utf-8字符串
                    System.out.println("接收到消息："+message+"\n"+"交换机"+exchange+"\n"+"消息id"+deliveryTag);

                }
            };

            /**
             * 监听队列String queue, boolean autoAck,Consumer callback
             * 参数明细
             * 1、队列名称
             * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动回复
             * 3、消费消息的方法，消费者接收到消息后调用此方法
             */
            channel.basicConsume(QUEUE, true, consumer);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
