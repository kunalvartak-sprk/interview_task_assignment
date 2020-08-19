package myscheduler.rabbitmq;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


import myscheduler.entity.Job;

public class PublishDataOnQueue {
	
	private ConnectionFactory cf;
	private Connection connection;
	private  Channel channel;

	
	
	public PublishDataOnQueue() throws IOException, TimeoutException {
	}

	public void publishtoQueue( Job job) throws IOException, TimeoutException
	{
		open();		
		
		//Creating the ObjectMapper object	    
		ObjectMapper mapper = new ObjectMapper();
		//Converting the Object to JSONString
		
	    String jsonString = mapper.writeValueAsString(job);		
		byte[] messageBodyBytes = jsonString.getBytes("UTF-8");
	    
		Map<String, Object> headers = new HashMap<String, Object>();		
		
		long delay = calculateDelay( job );
		System.out.println("Adding delay of "+ delay + " for job id"+job.getJobid());		
		headers.put("x-delay", delay);
		
		AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder().headers(headers);
		channel.basicPublish("DelayExchane", "", props.build(), messageBodyBytes);
		close();
		
	}
	
	// Job is suppose to run today if yes schedule it accordingly ...
	// note:1 if job was not executed yesterday and it is suppose to run today it will run as per the schedule time and not immediately 
	// 		2. if job was suppose to run today and has missed the deadline because of some xyz and incase it comes up in the queue , it will be schedule with 0 dealy 
	private long calculateDelay(Job j) {
		
		long delay =-1;
		LocalDateTime lt =  LocalDateTime.now(ZoneOffset.UTC);
		
		System.out.println("Calculating Delay ");
		
		List <String> dateToRun = Arrays.asList(j.getJobrundayid().split(","));
		
		if( dateToRun.contains("-1") || (dateToRun.contains( String.valueOf( lt.getDayOfMonth()) )) ||  runBasedOnWeekDays(j, lt, dateToRun))
		{ 		
			
		  long jobtime=	(j.getHours() * 60 * 60 * 1000) + ( j.getMinutes() * 60 * 1000 );
		  long currtime = ( lt.getHour() * 60 * 60 * 1000 ) + ( lt.getMinute()* 60 * 1000);
		  
		  if( lt.getDayOfMonth() -  j.getLastrundate().getDay() >1  )
		   
		  delay = jobtime - currtime;
		  if(delay<0)
			  delay =0;
		}

		System.out.println("\n\n======================calcualte delay=====================================================");
		System.out.println("current UTC TIME: "+lt.getHour() + " "+ lt.getMinute()+" "+ lt.getDayOfMonth() + " " + lt.getDayOfWeek().name() );
		System.out.println("Job Scheduled Time in UTC "+j .getHours() + " " + j.getMinutes()+ " "+j.getJobrundayid() + " "+j.getWeekdays());
		System.out.println("delay "+delay);
		System.out.println("==============================================================================================\n\n");

		return delay;
	}

	
	private boolean runBasedOnWeekDays(Job j, LocalDateTime lt, List<String> dateToRun) {
		if(dateToRun.contains("0"))	   //run on weekdays
		{
			//run based on weekdays
			List <String> daysToRun = Arrays.asList(j.getWeekdays().split(","));
			if( daysToRun.contains(lt.getDayOfWeek().name()))
			{
				return true;
			}

		}
		return false;
	}

	private void open() throws IOException, TimeoutException {
		cf = new ConnectionFactory();
		connection = cf.newConnection();
		channel = connection.createChannel();
	}



	private void close() throws IOException, TimeoutException {
		channel.close();
		connection.close();
	}
	

}
