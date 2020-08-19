package myscheduler;



import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Scanner;



import myscheduler.entity.Job;
import myscheduler.entity.Schedule;
import myscheduler.repository.JobsRepository;

public class JobConfig {
	public static void main(String[] args) throws Exception {		
		int ch=-1;

		Scanner scan = new Scanner(System.in);
		 System.out.println(" Job Configuration ");
		 System.out.println("1. Add new Job ");
		 System.out.println("2. Delete an Existing Job");
		 System.out.println("3. View Deatils of a Job based on Name");
 		 System.out.println("4. Modify the job based on Name");
		 System.out.println("5. Exit ");
 		 System.out.println("Enter your Choice ");
		 ch  = Integer.parseInt(scan.next())  ;
		 scan.reset();

		 switch(ch)
		 {  
			case 1:
				addJob();	
			break;
		
			case 2:
				removeJob();
			break;
			
			case 3:
				viewDetails();
			break;
			
			case 4:
				modifyJobConfig();
			break;
			
			case 5:
				System.out.println("Thank you ");
				scan.close();						
			break;
			
			default:
				System.out.println("Please Enter a valid option ");
			break;
		 }//switch
				
		scan.close();	 			
	}


	private static void modifyJobConfig() 
	{
		System.out.println("Enter the name of the job to modify ");
		Scanner scanner =  new Scanner(System.in);
		String name = scanner.next();
		scanner.reset();
		JobsRepository jr = new JobsRepository();
		try 
		{
			jr.updateJobDetailsForJobName(name);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void viewDetails()
	{
		JobsRepository jr = new JobsRepository();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the job name to view the details");
		String name = sc.next(); 
		sc.close();
		try 
		{
			jr.getJobDetailsForJobName(name);
			
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	private static void removeJob() {	
		
		JobsRepository jr = new JobsRepository();
		String jname;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the job name which you want to remove ");
		jname = sc.next();
		try {
			jr.removeJob(jname);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.reset();
		sc.close();
		
	}

	private static void addJob() {
		JobsRepository jr = new JobsRepository();
		Job job = new Job();	
		Schedule s = new Schedule();
		Scanner stringscanner = new Scanner(System.in);
		Scanner integerscanner = new Scanner(System.in);
		
		System.out.println("***********Enter job Deatils************");
		while(true)
		{
			System.out.println("Enter the name of the job");
			String jname = stringscanner.next() ;
			job.setJobname( jname );
			
			try
			{
				if (!jr.isJobNamePresent(jname))
				{
					break;
				}
				else
				{
					System.out.println(" job with the given name already present. please use a different job name ");
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		System.out.println("Enter the Job Description");
		job.setDescription( stringscanner.next() );
		job.setLastrunstatus("INACTIVE");
		System.out.println("Enter the Command to Execute");
		job.setCommand( stringscanner.next());	
		
		String ch = "y";
		String host = "";
		while( ch.toLowerCase().compareTo("y") ==0 )
			{
				System.out.println("Please enter the Host MAC ADDRESS on which you want to run the job");
				if(host=="")
					host = stringscanner.next();
				else
					host = host + ","+stringscanner.next();
				
				System.out.println("Do you Want to add more host y/n ");
				ch = stringscanner.next();
			}
			
		job.setHostid(host);
		
		job.setLastrundestination( host.split(",")[0]  );
		
		LocalDateTime lt = LocalDateTime.now(ZoneOffset.UTC);
		System.out.println(".......Enter Schedule Details......");
		while(true)
		{
			System.out.println("Enter at what hour do you want to schedule the job : [0-23] ");
			int hr= Integer.parseInt (integerscanner.next());
			if(hr<24 && hr >=0)
			{
				s.setHours(  hr  );
				break;
			}
			else
			{
				System.out.println("hours should be between [ 0-23 ]");
			}
		}
		
		while(true)
		{
			System.out.println("Enter at what Minute do you want to schedule the job : [0-59] ");
			int min = Integer.parseInt(   integerscanner.next());
			if(min >=0 && min <=59)
			{
				s.setMinutes( min );
				break;
			}
			else
			{
				System.out.println("hours should be between [ 0-59 ]");
			}
		}
		
		LocalTime t =LocalTime.of(s.getHours(),s.getMinutes());			
		t = t.minusHours(5);
		t= t.minusMinutes(30);
		lt = LocalDateTime.of(lt.toLocalDate() , t);
		
		//System.out.println("DAte....");
		//System.out.println(lt.toString());
		job.setUpdatedDate( lt );
		job.setLastrundate( lt);
		job.setNextrundate( lt );			
		
		System.out.println("Enter the days on which you want to schedule the job: comma sepearted \n eg: 1,3,4 \n For Everyday please enter -1 \n Based on weekdays enter 0 ");
		String jobrunday = stringscanner.next();
		s.setJobrundayid(jobrunday);
 		if(s.getJobrundayid().compareTo("0")==0)
		{
			System.out.println("  Enter Complete Weekdays on which you want to schedul the job: comma seperated: \n eg: MONDAY,TUESDAY,SATURDAY");
			//--------------------------------

			String ch1 = "y";
			String dayofweek = "";
			while( ch1.toLowerCase().compareTo("y") ==0 )
				{
					System.out.println("Please Enter the day on which you want to run the process ");
					for (DayOfWeek c : DayOfWeek.values())
					{
					    System.out.println( c.ordinal() +". "+c);
					}
					 
					int dayno = Integer.parseInt(  stringscanner.next() );					
					if(dayofweek.compareTo("")==0)
					{
						dayofweek = DayOfWeek.of(dayno+1).toString();
					}
					else
						dayofweek = dayofweek + ","+DayOfWeek.of(dayno+1).toString();
					
					System.out.println("Do you Want to add more week days [ y/n ] ");
					ch1 = stringscanner.next();
				}
			s.setWeekdays(dayofweek);
		}
		else
		{
			s.setWeekdays("");
		}
 		
		s.setSchedulename(job.getJobname());
		System.out.println("job configured ");
		System.out.println(job);
		System.out.println(s);
		System.out.println("Adding the details to database");

		//JobsRepository jr = new JobsRepository();
		jr.insertjob(job, s, "");
		integerscanner.reset();
		stringscanner.reset();
		stringscanner.close();
		integerscanner.close();
		}
	
}



