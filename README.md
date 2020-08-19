# interview_task_assignment
 Job Scheduler documentation

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
	
please refer view 
