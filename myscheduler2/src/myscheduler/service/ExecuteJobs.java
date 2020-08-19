package myscheduler.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;

import myscheduler.entity.Job;
import myscheduler.macaddresscalcluator.CalculateMACAddress;
import myscheduler.rabbitmq.ConsumeDataFromQueue;
import myscheduler.repository.JobsRepository;

public class ExecuteJobs  {

	JobsRepository jr;
	public Job job;
	
	public ExecuteJobs()
	{
		jr =new JobsRepository();
	}
	
	public void run() {
	
		System.out.println("ExecuteJobs");
		try 
		{
			ConsumeDataFromQueue cdf = new ConsumeDataFromQueue();
			cdf.consumeFromQueue();			
		   // check if its time to execute this jobs ??		
		   // again check if this job was recently updated in database
		}
		catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	private int checkStickyHost(Job j) {
		
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
	
  public void afterconsume()
  {
	    System.out.println(job);		
	    new Thread( new Runnable() {
			@Override
			public void run() {
			    //create a thread and then run the job
 				int status =-1;
 				int count = 1;
				try 
				{
					//compare current host with lastrunhost
					// if lastrunhost is different from current then verify if that host is alive or not..
					
					 if( checkStickyHost(job) !=1)
					 {
						while(true)
						{
							Calendar utc ;	
							utc = Calendar.getInstance(TimeZone.getTimeZone("IST"));
							
						   LocalDateTime lt =  LocalDateTime.now(ZoneOffset.UTC);

						   //wait for signal verification
							Timestamp lastupdatereceived =  jr.checklastRunHostIsAlive(job.getLastrundestination());
							LocalDateTime lt2=null;
							
							if(lastupdatereceived!=null)
							{
								lt2=lastupdatereceived.toLocalDateTime();															
								System.out.println("last update received "+lastupdatereceived);
								System.out.println("difference in year : "+(lt2.getDayOfYear() - lt.getDayOfYear() ));
								System.out.println("difference in hours: "+(lt2.getHour() - lt.getHour()));
								System.out.println("difference in minutes: "+ (lt2.getMinute() - lt.getMinute() ));
							}
							
							if( lastupdatereceived== null ||  (lt2.getDayOfYear() - lt.getDayOfYear() ) !=0 || (lt2.getHour() - lt.getHour())!=0 ||  (lt2.getMinute() - lt.getMinute() )>2  ) {  
								  count= count + 1;
								  Thread.sleep(60000);
							 }
							 else
							 {
								 break;
							 }
							 
							 if(count>5)
								 break;
						}//while
						  
					 }

					
					
					// check if not updated then execute
					 if( isupdatedAfterScheduling(job) )
					 {
						 status = -999;
						 
					 }else {
						 //update the status to INACTIVE so that it will  be picked up again by the producer with the new updates
						 
						 status =RunJob( job,job.getCommandToExecute() );
					 }
				} catch (InterruptedException | IOException e) 
				{
					e.printStackTrace();
					status = -1;
				}
				
				if(status== 0)
				{
				  jr.updateStatus(job,"SUCCESS");
				  System.out.println("SUCCESS");
				}
				else if(status == -999)
				{
					  System.out.println("RESCHEDULE Since Job was updated after queued , hence ignoring this run ");
					
				}
				else
				{
				 jr.updateStatus(job,"FAILED");
				 System.out.println("FAILED");
				}
				
				List<String> host= getcurrentHostAddress();
				
				if(status!=-999)
					jr.updateLastRunHost(job );
			}

			private boolean isupdatedAfterScheduling(Job job) {
				Calendar utc ;	
				utc = Calendar.getInstance(TimeZone.getTimeZone("IST"));				
			   LocalDateTime lct =  LocalDateTime.now(ZoneOffset.UTC);

				
			   // updated time from database
				Timestamp ts = jr.getUpdatedDate(job);
				    
				System.out.println( "time from database "+ ts.toString());
				
				// updated time stored in job object while scheduling
				System.out.println( "time in job object "+ job.getLocalDateTime().toString());

				if( job.getLocalDateTime().before(ts) )
					return true;
				else 
					return false;

			}

			private List<String> getcurrentHostAddress() {
				CalculateMACAddress cma = new CalculateMACAddress();
				StringBuilder sb = new StringBuilder();
				System.out.println(sb.toString());
				List<String> listofmacaddress= cma.getMACAddress();
				return listofmacaddress;
			}
			
		} ).start();   
  }

	private int executeJob(Job job,String jobcommand) throws IOException, InterruptedException {		
        
		Process p = Runtime.getRuntime().exec("java -jar" + " "+jobcommand);
        // BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
        synchronized (p) {
        	jr.updateStatus(job,"RUNNING");
        	System.out.println("ExecuteJobs");
            p.waitFor();
        }
        
        System.out.println(p.exitValue());                
        System.out.println("");
        System.out.println("Job Execution Completed with status "+ p.exitValue());        
        return p.exitValue();
	}

	
	private  int RunJob(Job job,String command) throws IOException, InterruptedException {
		
		
		return executeJob( job, command);	        
	}
	
	


	private static void printJobOutPut(BufferedInputStream bis) throws IOException {
		int b=0;
        
        while((b=bis.read()) >0)
        {
            System.out.print((char)b);    
        }
	}

	
	
}
