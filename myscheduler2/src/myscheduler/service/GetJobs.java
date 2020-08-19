package myscheduler.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;



import myscheduler.entity.Job;
import myscheduler.macaddresscalcluator.CalculateMACAddress;
import myscheduler.rabbitmq.PublishDataOnQueue;
import myscheduler.repository.JobsRepository;

public class GetJobs implements Runnable {
	JobsRepository db;
	
	public GetJobs()
	{
		 db = new JobsRepository();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		process();       
	}//Run Method

	private void process() 
	{
		
		System.out.println("process for uploading");
		while(true)
		{
		   try 
		   {
		       List< Job >  joblist= db.getJobDetailsWithSchedule(); 
		       PublishDataOnQueue p = new PublishDataOnQueue();
		    
		       System.out.println("\n\nCount of jobs to schedule "+  joblist.size());
		       for(Job j : joblist ) 
		       {
		    	   System.out.println("job pusblishing");
		    	   
		    	   //verify if it should be published
		    	  // if ( shouldJobPublishBasedOnLastExecutionDate(j) !=0  &&  shouldJobPublishBasedOnStickyHost(j) == 1 )
		    	  if ( shouldJobPublishBasedOnLastExecutionDate(j) !=0 )		    		   
		    	   {
		    		   System.out.println("publishing the job "+ j.getJobid());
		    		   p.publishtoQueue(j);
		    		   db.updateStatus(j, "QUEUED");  
		    	   }
		    	   else
		    	   {
		    		   System.out.println("Not Publishing job "+j.getJobid());
		    	   }
		    	   
		    	   
				} 
		       Thread.sleep(5000);
		   }
		   catch (IOException | TimeoutException | ReflectiveOperationException | SQLException | InterruptedException e) 
		   {
				e.printStackTrace();
			}   
	      }
	}

	private int shouldJobPublishBasedOnLastExecutionDate(Job j) {
		
		   LocalDateTime lt =  LocalDateTime.now(ZoneOffset.UTC);
		   DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		   		   
		   // make sure its not a 1 digit number if yes the prefix it with 0
		   String hr = (j.getHours()) < 10 ? "0"+ Integer.toString( (j.getHours())) : Integer.toString( (j.getHours()));
		   String mm = (j.getMinutes()) < 10 ? "0"+ Integer.toString( (j.getMinutes())) : Integer.toString( (j.getMinutes()));
		   
		   String compare = lt.format(dateformat).toString()+" "+ 
				   				   hr+":"+ mm +":00.0" ;		   
		   
		   System.out.println( compare + "--"+ j.getLastrundate().toString() );
		
		   
		return compare.compareTo( j.getLastrundate().toString())   ;
		
		
	}

	private int shouldJobPublishBasedOnStickyHost(Job j) {
		
		CalculateMACAddress cma = new CalculateMACAddress();
		StringBuilder sb = new StringBuilder();
		System.out.println(sb.toString());
		List<String> listofmacaddress= cma.getMACAddress();
		if(listofmacaddress.contains(j.getLastrundestination()))
			return 1;
		else
			return 0;
		    // j.getLastrundestination().compareTo( );
	}	

}//Class Terminaiton

