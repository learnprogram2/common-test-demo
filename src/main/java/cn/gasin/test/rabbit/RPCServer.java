package cn.gasin.test.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author wongyiming
 * @description rabbitMQ 实现 PRC
 * @date 2019/9/8 11:19
 */
public class RPCServer {

	public static final String RPC_QUEUE_NAME = "rpc_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		// 1.创建connection和channel
		Connection connection = null;
		Channel channel = getChannel(connection);
//		channel.confirmSelect()

		// 2.生命队列
		channel.queueDeclare(RPC_QUEUE_NAME
				, false, false, false
				, null);
		channel.basicQos(1);
		System.out.println(" [x] Awaiting RPC requests");

		// 3.创建消费者
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				AMQP.BasicProperties replyProps = new AMQP.BasicProperties().builder()
						.correlationId(properties.getCorrelationId())
						.build();
				String response = "";
				try {
					String message = new String(body, StandardCharsets.UTF_8);
					int n = Integer.parseInt(message);
					System.out.println(" [.] fib(" + message + ")");
					response += fib(n);
				} catch (RuntimeException e) {
					System.out.println(" [.] " + e.toString());
				} finally {
					channel.basicPublish("", properties.getReplyTo(),
							replyProps, response.getBytes(StandardCharsets.UTF_8));
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		channel.basicConsume(RPC_QUEUE_NAME, false, consumer);


	}

	private static int fib(int n) {
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		return fib(n - 1) + fib(n - 2);
	}

	/** 创建连接 */
	public static Channel getChannel(Connection connection) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("root");
		factory.setPassword("admin");
		factory.setVirtualHost("wongyk");
		factory.setHost("192.168.2.4");
		factory.setPort(5672);
		connection = factory.newConnection();
		return connection.createChannel(1);
	}


}
