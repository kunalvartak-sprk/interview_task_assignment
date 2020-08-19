package myscheduler.rabbitmq;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import myscheduler.entity.Job;
import myscheduler.service.ExecuteJobs;
public class ConsumeDataFromQueue
{
	
	
	private ConnectionFactory cf;
	private Connection connection;
	private Channel channel;
	
	public ConsumeDataFromQueue() {
		
	}

	public  void consumeFromQueue() throws IOException, TimeoutException
	{
		open();		
		
		
		DeliverCallback deliveryCallBack = ( consumerTag , delivery   ) ->
		{
			System.out.println("CallBacks"); 
		    String message=new String ( delivery.getBody() );
		    System.out.println("Message Received "+message);
		   
		    ObjectMapper objectMapper = new ObjectMapper();
		    Job job = objectMapper.readValue(message, Job.class);		   
		   
		    ExecuteJobs execjobs = new ExecuteJobs();
		    execjobs.job = job;
		    execjobs.afterconsume();
		   
		   //return j;
		};
		
		System.out.println("DelayQueue Consumer calling");
		channel.basicConsume("DelayQueue", true, deliveryCallBack , consumerTag->{}  );
		
		
		//close();
	}
	
	
	private void open() throws IOException, TimeoutException {
		cf = new ConnectionFactory();
		connection = cf.newConnection();
		channel = connection.createChannel();
	}
	
	
}
