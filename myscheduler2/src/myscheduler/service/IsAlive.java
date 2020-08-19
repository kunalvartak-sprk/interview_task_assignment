package myscheduler.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import myscheduler.entity.Job;
import myscheduler.rabbitmq.PublishDataOnQueue;
import myscheduler.repository.JobsRepository;

public class IsAlive implements Runnable {
	JobsRepository jr;
	
	
	
	public IsAlive() {
		jr =new JobsRepository();
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		sendupdates();
		
	}



	private void sendupdates() {
		
		while(true)
		{
		       try {
		    	   System.out.println("IsAliveSignals");
			       jr.sendSignal();
			       Thread.sleep( 120000 );
			} catch (InterruptedException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		}
	}

}
