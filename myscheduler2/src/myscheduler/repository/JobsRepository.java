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
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

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
	
	
	public JobsRepository() {
		Properties appProps = new Properties();
		   try 
		   {
				appProps.load(new FileInputStream("resource/database.properties"));
		        setUserid(appProps.getProperty("db.username"));;
		        setPassword(appProps.getProperty("db.password"));;
		        setUrl(appProps.getProperty("db.url"));
		        setDriver(appProps.getProperty("db.driverClassName"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
   private List<myscheduler.entity.Job> myjobs = new ArrayList<>();
	
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

	public List<myscheduler.entity.Job> getMyjobs() {
		return myjobs;
	}

	public void setMyjobs(List<myscheduler.entity.Job> myjobs) {
		this.myjobs = myjobs;
	}

    public List<myscheduler.entity.Job> getJobDetailsWithSchedule() throws ClassNotFoundException, SQLException
    {
			intializeDataBaseConnection();
			myjobs = new ArrayList<Job>();
			execute(myjobs, prepareJobDetailsWithScheduleQuery()  );
			closeConnection();	
			return myjobs;
    }
   
	private void execute(List<myscheduler.entity.Job> mylist, String sql) throws SQLException {
		
		
		
		LocalDateTime lt =  LocalDateTime.now(ZoneOffset.UTC);		
		stmt=con.prepareStatement(sql);
		System.out.println(sql);

		
		stmt.setString(1, "%"+lt.getDayOfMonth()+"%"  );
		stmt.setString(2, "%"+lt.getDayOfWeek().name()+"%");		
		
		CalculateMACAddress cma = new CalculateMACAddress();
		StringBuilder sb = new StringBuilder();
		for( String s : cma.getMACAddress())
		{
			if(sb.length()==0)
				sb.append(s);
			else
				sb.append(",").append(s);
		}
		stmt.setString(3, "%"+ sb.toString() +"%");
		
		System.out.println("==============================================");
		System.out.println("get jobs parameters :");
		System.out.println(lt.toString());
		System.out.println("%"+lt.getDayOfMonth()+"%" );
		System.out.println("%"+lt.getDayOfWeek().name()+"%" );
		System.out.println("%"+ sb.toString() +"%");	
		System.out.println("==============================================\n\n\n\n");
		ResultSet rs = stmt.executeQuery();
        
		while(rs.next())
        {
			Calendar utc ;	
			utc = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        	Job temp = new Job( 
        			rs.getInt("id") , 
        			rs.getString("jobname") ,
        			rs.getString("command") , 
        			rs.getString("lastrunstatus") , 
        			rs.getString("host") , 
        			rs.getTimestamp("UPDATEDDDATE",utc),
        			rs.getString("lastrundestination"),
        			rs.getInt("hours"), 
        			rs.getInt("minutes"),
        			rs.getString("weekdays"),
        			rs.getString("jobrundayid"),
        			rs.getTimestamp("lastrundate", utc)
        			);
        	
        	System.out.println("====================Job object================================");
        	System.out.println(temp);
        	//utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        	//System.out.println("====================================================");
        	//System.out.println("lastrundate   "+  rs.getTimestamp("lastrundate") +"----"+ rs.getTimestamp("lastrundate", utc) );
        	//System.out.println("====================================================");
        	//utc = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        	//System.out.println("lastrundate   "+  rs.getTimestamp("lastrundate") +"----"+ rs.getTimestamp("lastrundate", utc) );
        	System.out.println("====================================================");
        	mylist.add(temp);
        }
	}
   
	private String prepareJobDetailsWithScheduleQuery() throws SQLException {
	   
	   //prepare statement to execute
		String sql=  " select j.id, j.jobname, j.command, j.lastrunstatus, j.UPDATEDDDATE , j.hostid as host , "
        + "j.lastrundestination,  s.jobrundayid , s.hours, s.minutes, s.weekdays ,  j.lastrundate from jobs j , schedules s " 
       + " where j.scheduleid = s.scheduleid  and "
       + "( s.jobrundayid = '-1' or s.jobrundayid like ? or s.weekdays like ? ) "
       + " and  j.lastrunstatus <> 'RUNNING'  and j.lastrunstatus <> 'QUEUED'  and j.hostid like ? ; ";
       
		return sql;		
}   
	
	public int updateStatus(Job job , String status)
	{
		try 
		{
			System.out.println("updating the status for job "+job.getJobid() + " to "+status);
			intializeDataBaseConnection();
			String sql = "UPDATE databasetest.jobs SET lastrunstatus = ? WHERE (id = ? )";
			executeupdatecommand( sql , job, status );
			
			String sql2 = "UPDATE databasetest.jobs SET lastrundate = ? WHERE (id = ?)";
			executeupdateRunDate(sql2, job, status);
			
			if(status.toLowerCase().compareTo("queued")!=0)
			{
				String sql3 = "UPDATE databasetest.jobs  SET updatedddate = ? WHERE (id = ?)";
				executeupdateUpdatedDate(sql3, job);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.out.println( "Error occured while updating the status ");
			return -1;
		}
		System.out.println( "jobid: " + job.getJobid() + "status "+ status);
		return 0;
	}
   
	private void executeupdatecommand( String sql , Job job , String status) throws SQLException {
	
		stmt=con.prepareStatement(sql);
		stmt.setInt(2, job.getJobid());
		stmt.setString(1, status);
		
		int rowcount = stmt.executeUpdate();
		System.out.println("Status Updated "+rowcount);
	}

	private void executeupdateRunDate( String sql , Job job , String status) throws SQLException {
		
		stmt=con.prepareStatement(sql);
		LocalDateTime lt =  LocalDateTime.now(ZoneOffset.UTC);
		DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		System.out.println(" utc  " + lt.toString());
		//this is time in IST
		System.out.println( "updadte in db "+ lt.format(dateformat)+" "+job.getHours()+":"+job.getMinutes()+":00.0"  );
		
		stmt.setString(1, lt.format(dateformat)+" "+job.getHours()+":"+job.getMinutes()+":00"   );
		stmt.setInt(2, job.getJobid());
				
		//"UPDATE databasetest.jobs SET lastrundate = ? WHERE (id = ?)";
		int rowcount = stmt.executeUpdate();
		System.out.println("Status Updated "+rowcount);
	}

	private void executeupdateUpdatedDate( String sql , Job job ) throws SQLException {
		stmt=con.prepareStatement(sql);
		LocalDateTime lt =  LocalDateTime.now(ZoneOffset.UTC);
		DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		System.out.println(" utc  " + lt.toString());
		System.out.println( "updadte in db "+ lt.format(dateformat)  );
		
		stmt.setString(1, lt.format(dateformat)  );
		stmt.setInt(2, job.getJobid());
				
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

	public void sendSignal() throws ClassNotFoundException, SQLException {
		
		//open the connection
		intializeDataBaseConnection();
		//get current mac/ip address
		CalculateMACAddress calcualteHostAddress = new CalculateMACAddress();
		List<String> hostaddress = calcualteHostAddress.getMACAddress();
		
		for(String host : hostaddress )
		{
			removeHost(host);
			insertHost(host);
		}
		 
		
		// delete the row for given mac/ip address
		// insert a row again for given mac/ip address
		//close the connection
		closeConnection();
		
	}

	private void insertHost(String host) {
		String sql = "INSERT INTO destinations (address) VALUES ( ? );";
		try 
		{
			stmt=con.prepareStatement(sql);		
			stmt.setString(1, host);
			int rowcount = stmt.executeUpdate();
			System.out.println("Status Updated "+rowcount);
		  } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	private void removeHost(String host) {

		String sql = "DELETE FROM destinations WHERE (address = ?);";
		try 
		{
			stmt=con.prepareStatement(sql);		
			stmt.setString(1, host);
			int rowcount = stmt.executeUpdate();
			System.out.println("Status Updated "+rowcount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Timestamp checklastRunHostIsAlive(String lastrundestination) {
		String sql = "select lastupdatereceived FROM destinations WHERE (address = ?);";
		Timestamp lastupdatereceived=null;
		try 
		{
			intializeDataBaseConnection();
			stmt=con.prepareStatement(sql);		
			stmt.setString(1, lastrundestination);
			ResultSet rs  = stmt.executeQuery();

			Calendar utc ;	
			utc = Calendar.getInstance(TimeZone.getTimeZone("IST"));
			
			Boolean datapresent = false;
			while(rs.next())
	        {
				datapresent= true;
				lastupdatereceived=rs.getTimestamp("lastupdatereceived", utc);
	        }			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastupdatereceived;
	}

	public void updateLastRunHost(Job job) {
	
		//open the connection
		try 
		{
		  intializeDataBaseConnection();
		  CalculateMACAddress calcualteHostAddress = new CalculateMACAddress();
		  List<String> hostaddress = calcualteHostAddress.getMACAddress();
		  // update the lastrunhost id
		  
		  String hostid = hostaddress.get(0);
		  String sql = "UPDATE jobs SET lastrundestination = ? WHERE (id = ? );";
		  updateLastRunHost(sql,hostid,job.getJobid());
		  
		  //close the connection
		  closeConnection();
		
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void updateLastRunHost( String sql ,String hostid , int jobid) throws SQLException {
		
		stmt=con.prepareStatement(sql);		
		stmt.setString(1, hostid   );
		stmt.setInt(2, jobid);
		
		int rowcount = stmt.executeUpdate();
		System.out.println("LastRunHost Updated "+rowcount);
	}

	public Timestamp getUpdatedDate(Job job) {
		
		String sql = "select updatedddate from jobs WHERE (jobname = ?);";
		Timestamp lastupdatereceived=null;
		try 
		{
			intializeDataBaseConnection();
			stmt=con.prepareStatement(sql);		
			stmt.setString(1, job.getJobname());
			ResultSet rs  = stmt.executeQuery();

			Calendar utc ;	
			utc = Calendar.getInstance(TimeZone.getTimeZone("IST"));			
			Boolean datapresent = false;
			
			while(rs.next())
	        {
				datapresent= true;
				lastupdatereceived=rs.getTimestamp("updatedddate", utc);
	        }			
			
			// job defination was deleted and is no longer avaiable in the database.
			if(datapresent==false)
				return null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastupdatereceived;

		
	}
	
	
}
