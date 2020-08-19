package myscheduler.entity;



import java.sql.Timestamp;
import java.util.List;
public class Job {
	
	private int jobid;
	private String jobname;
	private String commandToExecute;
	private String currentstatus;	
	private String destinationhosts;
	private Timestamp localDateTime;
	private String lastrundestination;
	private int hours;
	private int minutes;
	private String weekdays;
	private String jobrundayid;
	private Timestamp lastrundate;
	
	
	public Job() {
	
	}


	public Job(int jobid, String jobname, String commandToExecute, String currentstatus, String destinationhosts,
			Timestamp localDateTime, String lastrundestination, int hours, int minutes, String weekdays,
			String jobrundayid , Timestamp nextRunDate) {
		super();
		this.jobid = jobid;
		this.jobname = jobname;
		this.commandToExecute = commandToExecute;
		this.currentstatus = currentstatus;
		this.destinationhosts = destinationhosts;
		this.localDateTime = localDateTime;
		this.lastrundestination = lastrundestination;
		this.hours = hours;
		this.minutes = minutes;
		this.weekdays = weekdays;
		this.jobrundayid = jobrundayid;
		this.lastrundate = nextRunDate;
	}


	public Timestamp getLastrundate() {
		return lastrundate;
	}


	public void setLastrundate(Timestamp lastrundate) {
		this.lastrundate = lastrundate;
	}


	public int getJobid() {
		return jobid;
	}


	public void setJobid(int jobid) {
		this.jobid = jobid;
	}


	public String getJobname() {
		return jobname;
	}


	public void setJobname(String jobname) {
		this.jobname = jobname;
	}


	public String getCommandToExecute() {
		return commandToExecute;
	}


	public void setCommandToExecute(String commandToExecute) {
		this.commandToExecute = commandToExecute;
	}


	public String getCurrentstatus() {
		return currentstatus;
	}


	public void setCurrentstatus(String currentstatus) {
		this.currentstatus = currentstatus;
	}


	public String getDestinationhosts() {
		return destinationhosts;
	}


	public void setDestinationhosts(String destinationhosts) {
		this.destinationhosts = destinationhosts;
	}


	public Timestamp getLocalDateTime() {
		return localDateTime;
	}


	public void setLocalDateTime(Timestamp localDateTime) {
		
		this.localDateTime = localDateTime;
	}


	public String getLastrundestination() {
		return lastrundestination;
	}


	public void setLastrundestination(String lastrundestination) {
		this.lastrundestination = lastrundestination;
	}


	public int getHours() {
		return hours;
	}


	public void setHours(int hours) {
		this.hours = hours;
	}


	public int getMinutes() {
		return minutes;
	}


	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}


	public String getWeekdays() {
		return weekdays;
	}


	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}


	public String getJobrundayid() {
		return jobrundayid;
	}


	public void setJobrundayid(String jobrundayid) {
		this.jobrundayid = jobrundayid;
	}

	
	

	public Timestamp getNextRunDate() {
		return lastrundate;
	}


	public void setNextRunDate(Timestamp nextRunDate) {
		this.lastrundate = nextRunDate;
	}


	@Override
	public String toString() {
		return "Job [jobid=" + jobid + ", jobname=" + jobname + ", commandToExecute=" + commandToExecute
				+ ", currentstatus=" + currentstatus + ", destinationhosts=" + destinationhosts + ", localDateTime="
				+ localDateTime + ", lastrundestination=" + lastrundestination + ", hours=" + hours + ", minutes="
				+ minutes + ", weekdays=" + weekdays + ", jobrundayid=" + jobrundayid + ", lastrundate=" + lastrundate
				+ "]";
	}





	
	
	
}
