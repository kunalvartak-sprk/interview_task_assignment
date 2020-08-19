package myscheduler.entity;

import java.time.LocalDateTime;

public class JobSchedule {
	 public String jobname;
	 public String jobdescription;
	 public int scheduleid;
	 public String lastrunstatus;
	 public String command;
	 public String hostid;
	 public LocalDateTime updatedddate;
	 public LocalDateTime lastrundate;
	 public LocalDateTime nextrundate;
	 public LocalDateTime lastrundestination;
	 public int hours;
	 public int minutes;
	 public String jobrundayid;
	 public String weekdays;
	 public String schedulename;
	
	 public JobSchedule() {
		
	}
	
	 public JobSchedule(String jobname, String jobdescription, int scheduleid, String lastrunstatus, String command,
			String hostid, LocalDateTime updatedddate, LocalDateTime lastrundate, LocalDateTime nextrundate,
			LocalDateTime lastrundestination, int hours, int minutes, String jobrundayid, String weekdays,
			String schedulename) {
		
		this.jobname = jobname;
		this.jobdescription = jobdescription;
		this.scheduleid = scheduleid;
		this.lastrunstatus = lastrunstatus;
		this.command = command;
		this.hostid = hostid;
		this.updatedddate = updatedddate;
		this.lastrundate = lastrundate;
		this.nextrundate = nextrundate;
		this.lastrundestination = lastrundestination;
		this.hours = hours;
		this.minutes = minutes;
		this.jobrundayid = jobrundayid;
		this.weekdays = weekdays;
		this.schedulename = schedulename;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public String getJobdescription() {
		return jobdescription;
	}
	public void setJobdescription(String jobdescription) {
		this.jobdescription = jobdescription;
	}
	public int getScheduleid() {
		return scheduleid;
	}
	public void setScheduleid(int scheduleid) {
		this.scheduleid = scheduleid;
	}
	public String getLastrunstatus() {
		return lastrunstatus;
	}
	public void setLastrunstatus(String lastrunstatus) {
		this.lastrunstatus = lastrunstatus;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getHostid() {
		return hostid;
	}
	public void setHostid(String hostid) {
		this.hostid = hostid;
	}
	public LocalDateTime getUpdatedddate() {
		return updatedddate;
	}
	public void setUpdatedddate(LocalDateTime updatedddate) {
		this.updatedddate = updatedddate;
	}
	public LocalDateTime getLastrundate() {
		return lastrundate;
	}
	public void setLastrundate(LocalDateTime lastrundate) {
		this.lastrundate = lastrundate;
	}
	public LocalDateTime getNextrundate() {
		return nextrundate;
	}
	public void setNextrundate(LocalDateTime nextrundate) {
		this.nextrundate = nextrundate;
	}
	public LocalDateTime getLastrundestination() {
		return lastrundestination;
	}
	public void setLastrundestination(LocalDateTime lastrundestination) {
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
	public String getJobrundayid() {
		return jobrundayid;
	}
	public void setJobrundayid(String jobrundayid) {
		this.jobrundayid = jobrundayid;
	}
	public String getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}
	public String getSchedulename() {
		return schedulename;
	}
	public void setSchedulename(String schedulename) {
		this.schedulename = schedulename;
	}
	 
	 
	 
	 
	
		
}


