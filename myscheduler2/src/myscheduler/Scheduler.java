package myscheduler;


import myscheduler.service.ExecuteJobs;
import myscheduler.service.GetJobs;
import myscheduler.service.IsAlive;

public class Scheduler {
	public static void main(String[] args) throws Exception {

		
		
	   //producer 
	   Thread t1 = new Thread( new GetJobs());
	   //t1.start();
	   	  
	   //consumer
	   //Thread.sleep(25000);
	   ExecuteJobs jobs = new ExecuteJobs();
	   jobs.run();

	    //Host Is Alive to Track System Failure
		Thread isalive = new Thread(new IsAlive());
		isalive.run();		
	   

	}

}



