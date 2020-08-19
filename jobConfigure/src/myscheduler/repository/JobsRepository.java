package myscheduler.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Scanner;


import myscheduler.entity.*;
import myscheduler.macaddresscalcluator.CalculateMACAddress;

public class JobsRepository 
{

	
	private Connection con;
	private PreparedStatement stmt;
	private String url;
	private String  userid;
	private String  password;
	private String driver;
	private Boolean hasrows;; 
	
	
	public JobsRepository() {
		
		Properties appProps = new Properties();
		   try 
		   {
				appProps.load(new FileInputStream("resource/database.properties"));
		        setUserid(appProps.getProperty("db.username"));;
		        setPassword(appProps.getProperty("db.password"));;
		        setUrl(appProps.getProperty("db.url"));
		        setDriver(appProps.getProperty("db.driverClassName"));
		        hasrows = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
   public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
		
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PreparedStatement getStmt() {
		return stmt;
	}

	public void setStmt(PreparedStatement stmt) {
		this.stmt = stmt;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    public int getJobDetailsForSchedulename(String name) throws ClassNotFoundException, SQLException
    {
			//intializeDataBaseConnection();
			int id= execute(prepareJobDetailsWithScheduleName() , name  );
			//closeConnection();	
			return id;
    }
    
    public Boolean isJobNamePresent(String name) throws ClassNotFoundException, SQLException {
		intializeDataBaseConnection();
		
		 ResultSet rs = executeforjobname(prepareJobDetailsWithJobName() , name  );
		 Boolean ispresent = false;
		 while (rs.next()) {
			 ispresent = true;
			 break;
		 }
		closeConnection();
		
		return ispresent;	

    	
    }

    public void removeJob(String name) throws ClassNotFoundException, SQLException
    {
			intializeDataBaseConnection();
			 removeJobSchedule(prepareDeleteJob() , name  );
			 removeJobSchedule(prepareDeleteSchedule() , name  );
			System.out.println("Done!! ");
			closeConnection();	
			
    }    
    
    public void getJobDetailsForJobName(String name) throws ClassNotFoundException, SQLException
    {
			intializeDataBaseConnection();
			ResultSet rs = executeforjobname(prepareJobDetailsWithJobName() , name  );
			printResultSet(rs);
			closeConnection();	
			
    }

    
    public void updateJobDetailsForJobName(String name) throws ClassNotFoundException, SQLException
    {
			intializeDataBaseConnection();
			
				ResultSet rs = executeforjobname(getjobDetails() , name  );
				ResultSet rs2 = executeforjobname(getscheduleDetails() , name  );
				updateJobResult(rs , rs2);
				
				if(hasrows==false)
					System.out.println("Details for job name "+name + " is not present ");
			closeConnection();	 
			
    }    
    
	private void updateJobResult(ResultSet rs , ResultSet rs2) {
		  java.sql.ResultSetMetaData rsmd;
		  
		  Map<String, String> updatemap = new HashMap<>();
		  Map<Integer, String> updatemapkeys = new HashMap<>();
		
		  try {
			populatetablecolumnmapping(rs, updatemap, updatemapkeys, 0);
			
			if(hasrows==true)
			{
			populatetablecolumnmapping(rs2, updatemap, updatemapkeys ,( rs.getMetaData().getColumnCount()) );
			
			
			Scanner sc = new Scanner(System.in);
			
			String newvalue = "" ;
			String choice = "n";
			
			
			while(true)
			{
				System.out.println("do you want to update values [y/n]");
				choice = sc.next();
				
				if(choice.toLowerCase().compareTo("n")==0)
					break;

				System.out.println("Enter the serial number for which you want to update the value");				
				
				String s =  sc.next() ;
				System.out.println("print ");
				
				int i = Integer.parseInt(s);
				System.out.println("updating value  "+updatemapkeys.get(i) + " oldvalue is " + updatemap.get(updatemapkeys.get(i)) );
				
				newvalue = sc.next();
			    String sql = "UPDATE "+updatemapkeys.get(i).split("\\.")[0]+" SET "+updatemapkeys.get(i).split("\\.")[1]+" = ?  WHERE (scheduleid = ?)";
				stmt=con.prepareStatement(sql);			
				//stmt.setString(1, updatemapkeys.get(i).split("\\.")[0] );
				//stmt.setString(1, updatemapkeys.get(i).split("\\.")[1]  );
				stmt.setString(1, newvalue);				
				stmt.setString(2,  updatemap.get(updatemapkeys.get(i).split("\\.")[0]+".scheduleid")  );
				
				System.out.println(sql);
				int rowcount = stmt.executeUpdate();
				if(rowcount>0)
					System.out.println("Total rows Updated "+rowcount);
				else
					System.out.println("Some error occured while updating the vlaue ");
			 }
			
		 	 sc.reset();
			 sc.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void populatetablecolumnmapping(ResultSet rs, Map<String, String> updatemap, Map<Integer, String> updatemapkeys , int intialcount) throws SQLException {
		java.sql.ResultSetMetaData rsmd;
		rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		//System.out.println("columnnumber "+ columnsNumber + " initial count");
		while (rs.next()) 
		{
						
			for (int i = 1; i <= columnsNumber  ; i++) 
			{
				 hasrows= true;
		         String columnValue = rs.getString(i);
		         System.out.println("serial no: "+(i+intialcount)+"   "+rsmd.getColumnName(i) + " "+columnValue  );
		      
		         updatemap.put(rsmd.getTableName((i))+"."+rsmd.getColumnName(i), columnValue);
		         updatemapkeys.put((i+intialcount), rsmd.getTableName((i))+"."+ rsmd.getColumnName(i));
			}
		  System.out.println("");
   }
	}

	private static void printResultSet(ResultSet rs) throws SQLException {
		  java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		   int columnsNumber = rsmd.getColumnCount();
		   while (rs.next()) {
		       for (int i = 1; i <= columnsNumber; i++) {
		           if (i > 1) System.out.print(",  ");
		           String columnValue = rs.getString(i);
		           System.out.println(rsmd.getColumnName(i) + " "+columnValue );
		       }
		       System.out.println("");
		   }
		
	}    

	private int removeJobSchedule( String sql,String name) throws SQLException {
		stmt=con.prepareStatement(sql);
		stmt.setString(1, name  );
		
		int rs = stmt.executeUpdate();
		return rs;
}	
	
	private ResultSet executeforjobname( String sql,String name) throws SQLException {
		stmt=con.prepareStatement(sql);
		stmt.setString(1, name  );
		
		ResultSet rs = stmt.executeQuery();
		return rs;
}
       
	private int execute( String sql,String name) throws SQLException {
		stmt=con.prepareStatement(sql);
		stmt.setString(1, name  );
		
		ResultSet rs = stmt.executeQuery();
        int id =-1;
		while(rs.next())
        {
        	id=rs.getInt("scheduleid"); 
        }
		return id;
	}
   
	private String prepareJobDetailsWithScheduleName() throws SQLException {
	   
	   //prepare statement to execute
		String sql=  "select scheduleid from schedules where schedulename = ?";
       
		return sql;		
}   
	
	private String prepareDeleteJob() throws SQLException {
		   
		   //prepare statement to execute
			String sql=  "DELETE FROM jobs WHERE jobname = ? ;";
			return sql;		
	}   	

	private String prepareDeleteSchedule() throws SQLException {
		   
		   //prepare statement to execute
			String sql=  "DELETE FROM schedules WHERE schedulename = ? ;";
			return sql;		
	}	
	
	private String prepareJobDetailsWithJobName() throws SQLException {
		   
		   //prepare statement to execute
			String sql=  "select  *   from jobs  , schedules  where jobs.scheduleid = schedules.scheduleid and jobs.jobname = ?";
			return sql;		
	} 	
	
	private String getscheduleDetails()
	{
		return "select * from schedules where schedulename = ? ";
	}
	
	private String getjobDetails()
	{
		return "select * from jobs where jobname = ? ";
	}
	
	public int insertjob(Job job , Schedule s ,String status)
	{
		try 
		{
			System.out.println("inserting job "+job.getJobid());
			intializeDataBaseConnection();
				
			String sql1 ="INSERT INTO schedules(  hours ,minutes,jobrundayid,weekdays,schedulename) VALUES (   ? ,? , ?, ? , ? )";
			
			executeupdatecommand(sql1, s);
			
			int id = getJobDetailsForSchedulename(s.getSchedulename());
			
			job.setScheduleid(id);
			
			String sql2 = "INSERT INTO jobs(  jobname ,jobdescription,scheduleid,lastrunstatus,command,hostid,updatedddate,lastrundate,lastrundestination) VALUES  (   ? ,? , ?, ? , ? , ?, ? , ?, ?);";
			executeupdateJobInsert(sql2, job);
			
		} catch (ClassNotFoundException | SQLException e) 
		{
			e.printStackTrace();
			System.out.println( "Error occured while updating the status ");
			return -1;
		}
		return 0;
	}
   
	private void executeupdatecommand( String sql , Schedule s ) throws SQLException {
	
		stmt=con.prepareStatement(sql);
		stmt.setInt(1, s.getHours());
		stmt.setInt(2, s.getMinutes());
		stmt.setString(3, s.getJobrundayid());
		stmt.setString(4, s.getWeekdays());
		stmt.setString(5, s.getSchedulename());
		
		System.out.println(sql);
		int rowcount = stmt.executeUpdate();
		System.out.println("Status Updated "+rowcount);
	}

	private void executeupdateJobInsert( String sql , Job job ) throws SQLException {
		
		
		stmt=con.prepareStatement(sql);
		stmt.setString(1, job.getJobname()  );
		stmt.setString(2, job.getDescription()  );
		stmt.setInt(3, job. getScheduleid() );
		stmt.setString(4, job.getLastrunstatus()  );
		stmt.setString(5, job.getCommand());
		stmt.setString(6, job.getHostid());
		stmt.setString(7,job.getUpdateddate().toString());
		stmt.setString(8,job.getLastrundate().toString());
		stmt.setString(9, job.getLastrundestination());
		
		//"UPDATE databasetest.jobs SET lastrundate = ? WHERE (id = ?)";
		int rowcount = stmt.executeUpdate();
		System.out.println("Status Updated "+rowcount);
	}
	
	private void closeConnection() throws SQLException {
	if (con!=null)
		con.close();
}

	private void intializeDataBaseConnection() throws ClassNotFoundException, SQLException {
		Class.forName(driver );  
		con = DriverManager.getConnection(url,userid,password);
	}
}
