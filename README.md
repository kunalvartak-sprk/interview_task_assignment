# interview_task_assignment
Job Scheduler documentation
PROJECT COMPONENTS:

1. Database Structure:

	Total no of tables: 3
	1. jobs          				:use to store the meta data of the job like.
			1. id					:idenity autoincremented column.
			2. jobname				: sotres the name of the job.
			3. jobdescription		: sotres the description of the job.
			4. scheduleid			: sotres the scheduleid which points to the detais of the schedule in the schedulel table.
			5. lastrunstatus		: it stores the status of the last run : status include :
									  QUEUED , RUNNING , SUCCESS , FAILED
									  When job is added/modifed from the job configuration project ,
									  it mark the status as INACTIVE  and when the producer publish this job in RabbitMQ 
									  it will change it status to QUEUED state.
			6. command				: job locaiton ( jar file )  which we want to schedule
			7. hostid				: MAC address of the system on which job is suppose to run.
									  for multiple system use comma seperated values.
										
			8. updatedddate			: it stores the last updated date for the given job.
									: it also indicates the time and date of change in state from QUEUED - RUNNING - FAILED/SUCCESS.
			9. lastrundate			: this column indicates when the job was suppose to get executed in the lastrun.
			10.lastrundestination`	: it indicates the locaiton ( MAC address ) of system the job got executed.
										
										
	
	2. schedules 					: use to store the job schedule detials
			1. scheduleid			: autoincremented id when the new schedule is added.
									: it is tied up with the job based on scheduleid column present in job tables
			2. hours				: it indicates at what hour the job is suppose to get executed
			3. minutes				: it indicates at what minute the job is suppose to get executed
			4. jobrundayid			: 
									 1. set it to -1 to run the job everyday
									 2. set it to comma seperated day no if you want the job to get executed
									    for specific days 
									    eg1: 18,19,20 : the job will get executed on every 18 , 19 and 20 day of the month.
									    eg2: 5        : the job will get executed on every 5 day of the month
									 3. set it to 0 if you want the job to get executed based on weekdays.
									  
			5. weekdays				: when jobrundayid is set to 0 , the job will get executed based on the 
									  weekdays mention in this column
									  eg1: MONDAY,TUESDAY,SATURDAY : the job will get executed every MONDAY , Tuesday and Saturday.
									  eg2: FRIDAY				   : the job will get executed every Friday.
									   
			6. schedulename`		: it the name given to schedule and it is same as the jobname which this schedule
									   is tied to .
	
	
	3. destinations					: use to capture the heartbeat of the server/system on which our jobs are runnig.
									  it stores the timestamp indicating host was alive at that time.
			1. id
			2. lastupdatereceived
			3. address`
	
	Database Details:
	Database is hosted in AWS AND it is mysql instance.
	to access the database form the  mysql workbench please use the following details:
	
	1. connection string : 
		a. db.url=jdbc:mysql://databasetest.cwh8n4exaldm.ap-south-1.rds.amazonaws.com:3306/databasetest?user=admin
		b. db.driverClassName=com.mysql.cj.jdbc.Driver
		c. db.username=admin
		d. db.password=password	
	
	please use your own database instance as i will be deleting this instance in a month.		
please refer video : "Assignment_DataBase_walkthrough" for the details.


2.  	RabbitMQ : 
			- Is used to store the job that are suppose to run today.
			- All the jobs which are suppose to run today at a specific time are published on rabbitMQ.					  
			- I am using delayed messaging queue in RabbitMQ, 
			  i.e jobs are already sorted in the queue based on the timings and they will be available to 
			  the consumer to consume only when there time is expired. 
					  
please refer video : "RabbitMq Walkthrough"	for additional details.
		
		
3.	Producer (GetJobsjava )	:
		
	-Producer reads the database for the job configuraation and publish it to the Delayed Queue of RabbitMQ
	 by adding appropriate dealy to the messages.
	-Message stored in the rabbitMQ is the json format which has all the information required by the Consumer to 
	 start he schedule job.
		
		
4	Consumer: (ExecuteJobs):
		
	-Consumer consumes the job from the RabbitMQ and process it as per the details mentioned in the messages of the 
	 Queue.
	-Note: Cosumer will Consume the job from the RabbitMQ only when the job is visible 



5.	Sample Jobs:
	1.	LongRunning.jar : is the job which simulates the behaviour of longRunning process.( infinte loop in this case )
	2.	Test.jar :  is the job which simulates the behaviour of the process that throws the Excepiton and fails.
	3.	NoErrorTest.jar : is the job that runs to completion without any issues.

	pleae store all the above jar files at : D:\jarfiles\ locaiton.
	if you wish to store it at some other location then please dont forget to update the command column of job tabel in the database.
		
		

6.	Job Cofiguation : 
	I have added one more basic core java project called Job Configured which will allow you to 
	1. Add more jobs in the database.
	2. delete job based on jobname
	3. modify job based on jobname
	4. View job based on jobname.





