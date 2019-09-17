package cn.gasin.test.rabbit;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wongyiming
 * @description
 * @date 2019/9/8 12:37
 */
public class RPCClient {

	private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";
	private String replyQueueName;
//	private QueueingConsumer consumer;


	public RPCClient() throws IOException, TimeoutException {
		// 1.创建connection和channel
		Channel replyQueueName = RPCServer.getChannel(connection);
//		new QueueingCon
	}
}
