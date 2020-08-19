# interview_task_assignment
 Job Scheduler documentation
PROJECT COMPONENTS:

1. Database Structure:
	Total no of tables: 3
	1. jobs          :use to store the meta data of the job like.
			1. id
			2. jobname
			3. jobdescription
			4. scheduleid
			5. lastrunstatus
			6. command
			7. hostid
			8. updatedddate
			9. lastrundate
			10.lastrundestination`
	2. schedules 		: use to sotre the job schedule detials
			1. scheduleid
			2. hours
			3. minutes
			4. jobrundayid
			5. weekdays
			6. schedulename`	
	
	3. destinations		: use to capture the heartbeat of the server, it stores the timestamp 
							indicating host was accessible at that time.
			1. id
			2. lastupdatereceived
			3. address`
	
please refer video : "Assignment_DataBase_walkthrough" for the details.


2.  	RabbitMq : 
					 Is used to store the job that are suppose to run today.
					 All the jobs which are suppose to run today at a specific time are published on rabbitMQ.					  
				     I am using delayed messaging queue , i.e jobs are already sorted in the queue based on the timings.
					  
please refer vide : "RabbitMq Walkthrough"		
		
		
3.		Producer (GetJobs )	:
		
		Producer reads the database for the job configuraation and publish it to the RabbitMq ( dealyed rabbitmq)
		by adding appropriate dealy to the messages.
		message stored in the rabbitMQ is the json message which has all the information required by the producer to 
		schedule the call.
		process from the message form the MQ will be ablet  
		
4. 		Consumer: (ExecuteJobs)










