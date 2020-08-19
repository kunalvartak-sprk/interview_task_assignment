package myscheduler.entity;




import java.time.LocalDateTime;

public class Job {
	
	private int jobid;
	private String jobname;
	private String description;
	private int scheduleid;	
	private String lastrunstatus;
	private String command;
	private String hostid;
	private LocalDateTime updateddate;
	private LocalDateTime lastrundate;
	private LocalDateTime nextrundate;
	private String lastrundestination;

	public Job(int jobid, String jobname, String description, int scheduleid, String lastrunstatus, String command,
			String hostid, LocalDateTime updateddate, LocalDateTime lastrundate, LocalDateTime nextrundate,
			String lastrundestination) {
		this.jobid = jobid;
		this.jobname = jobname;
		this.description = description;
		this.scheduleid = scheduleid;
		this.lastrunstatus = lastrunstatus;
		this.command = command;
		this.hostid = hostid;
		this.updateddate = updateddate;
		this.lastrundate = lastrundate;
		this.nextrundate = nextrundate;
		this.lastrundestination = lastrundestination;
	}

	public Job() {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public LocalDateTime getUpdateddate() {
		return updateddate;
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

	public String getLastrundestination() {
		return lastrundestination;
	}

	public void setLastrundestination(String lastrundestination) {
		this.lastrundestination = lastrundestination;
	}

	public void setUpdatedDate(LocalDateTime updateddate) {
		this.updateddate = updateddate;
		
	}

	@Override
	public String toString() {
		return "Job [jobid=" + jobid + ", jobname=" + jobname + ", description=" + description + ", scheduleid="
				+ scheduleid + ", lastrunstatus=" + lastrunstatus + ", command=" + command + ", hostid=" + hostid
				+ ", updateddate=" + updateddate + ", lastrundate=" + lastrundate + ", nextrundate=" + nextrundate
				+ ", lastrundestination=" + lastrundestination + "]";
	}
	

	

	
	
	
}
