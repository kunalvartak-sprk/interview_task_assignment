DROP TABLE IF EXISTS jobs;
 
CREATE TABLE jobs(
  id INT AUTO_INCREMENT  PRIMARY KEY,
  jobname VARCHAR(100) NOT NULL,
  jobdescription VARCHAR(1000) NOT NULL,
  scheduleid int not null,
  lastrunstatus VARCHAR(100),
  command VARCHAR(100),
  hostid int not null,
  currentstatus VARCHAR(100),
  updatedddate DATETIME DEFAULT CURRENT_TIMESTAMP,
  lastrundate DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO jobs(  jobname ,jobdescription,scheduleid,lastrunstatus,command,hostid,currentstatus ) 
VALUES  (   'testjob' ,'this is a testjob' , 1, 'SUCCESS' , 'notepad.exe' , 1, 'QUEUED' );
 


INSERT INTO jobs(  jobname ,jobdescription,scheduleid,lastrunstatus,command,hostid,currentstatus ) 
VALUES  (   'testjob2' ,'this is a testjob2' , 2, 'SUCCESS' , 'D:\\jarfiles\\NoErrorTest.jar' , 1, 'QUEUED' );


INSERT INTO jobs(  jobname ,jobdescription,scheduleid,lastrunstatus,command,hostid,currentstatus ) 
VALUES  (   'testjob3' ,'this is a testjob2' , 3, 'SUCCESS' , 'D:\\jarfiles\\LongRunning.jar' , 1, 'QUEUED' );


CREATE TABLE `schedules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `destinationid` int(11) NOT NULL,
  `lastrundestination` varchar(1000) NOT NULL,
  `hours` int DEFAULT 0 check (hours between 0 and 23),
  `minutes` int DEFAULT 0 check (minutes between 0 and 59),
  `jobrundayid` varchar(250) NOT NULL default "-1",
  `weekdays` varchar(250) NOT NULL default "",
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO schedules(  destinationid ,lastrundestination,jobrundayid , hours , minutes, weekdays ) 
VALUES  (   1 ,'localhost' , '1,5' , 14 , 30 , 'mon,tue,wed,thu,fri,sat,sun');

select* from schedules;

/*


INSERT INTO schedules(  destinationid ,lastrundestination , hours , minutes) 
VALUES  (   1 ,'localhost' ,  14 , 30 );

select* from schedules;
*/

 
DROP TABLE IF EXISTS destinations;
 
CREATE TABLE destinations(
  id INT AUTO_INCREMENT  PRIMARY KEY,
  destinationid int not null,
  address VARCHAR(1000) not null
);

INSERT INTO destinations(  destinationid ,address ) 
VALUES  (   1 ,'localhost'  );
INSERT INTO destinations(  destinationid ,address )
VALUES  (   1 ,'localhost2'  );
INSERT INTO destinations(  destinationid ,address )
VALUES  (   1 ,'localhost3'  );