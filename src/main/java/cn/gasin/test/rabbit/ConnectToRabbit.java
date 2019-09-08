package cn.gasin.test.rabbit;

import com.rabbitmq.client.*;
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wongyiming
 * @description
 * @date 2019/9/5 21:26
 */
public class ConnectToRabbit {

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = null;
		Channel channel = getChannel(connection);


		// 生成交换器和队列
		channel.exchangeDeclare("exchangeName", "direct", true);
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, "exchangeName", "routingKey");


		channel.close();
		connection.close();
	}

	/** 创建连接 */
	private static Channel getChannel(Connection connection) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("root");
		factory.setPassword("admin");
		factory.setVirtualHost("wongyk");
		factory.setHost("192.168.2.4");
		factory.setPort(5672);
		connection = factory.newConnection();
		return connection.createChannel(1);
	}


	@Test
	public void testSendMessage() throws Exception {
		Connection connection = null;
		Channel channel = getChannel(connection);

		channel.exchangeDeclare("exchangeName", "direct", true);
		//		String queueName = channel.queueDeclare("queueName", false, true, true, null).getQueue();
		channel.queueBind("queueName", "exchangeName", "routingKey");


		try {
			byte[] messageBodyBytes = "My name is liming !".getBytes();
			channel.basicPublish("exchangeName", "routingKey", null, messageBodyBytes);


			channel.basicPublish("exchangeName", "routingKey"
					, true, MessageProperties.PERSISTENT_TEXT_PLAIN
					, messageBodyBytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			channel.close();
			connection.close();
		}
	}


	@Test
	public void testPullMessage() throws Exception {
		Connection connection = null;
		Channel channel = getChannel(connection);

		channel.exchangeDeclare("exchangeName", "direct", true);
		//		String queueName = channel.queueDeclare("queueName", false, true, true, null).getQueue();
		channel.queueBind("queueName", "exchangeName", "routingKey");


		boolean autoAck = true; // 如果服务器应该期望显式确认
		channel.basicQos(1);
		DefaultConsumer callback = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String routingKey = envelope.getRoutingKey();
				long deliveryTag = envelope.getDeliveryTag();
				String contentType = properties.getContentType();
				// process the message components here ...
				String x = new String(body);
				System.out.println(x);
				channel.basicAck(deliveryTag, false);
			}
		};
		channel.basicConsume("queueName", autoAck,
				callback);

		channel.close();
		//		connection.close();

	}

	@Test
	public void testGetMessage() throws Exception {
		Connection connection = null;
		Channel channel = getChannel(connection);

		channel.exchangeDeclare("exchangeName", "direct", true);
		//		String queueName = channel.queueDeclare("queueName", false, true, true, null).getQueue();
		channel.queueBind("queueName", "exchangeName", "routingKey");


		GetResponse response = channel.basicGet("queueName", false);

		System.out.println(new String(response.getBody()));
		channel.basicAck(response.getEnvelope().getDeliveryTag(), false);


		channel.close();


	}

}
