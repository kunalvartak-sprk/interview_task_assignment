package myscheduler.entity;

public class Schedule {
	
	private int id;
	private int hours;
	private int minutes;
	private String jobrundayid;
	private String weekdays;
	private String schedulename;
	
	public Schedule(int id, int hours, int minutes, String jobrundayid, String weekdays , String schedulename) {
		super();
		this.id = id;
		this.hours = hours;
		this.minutes = minutes;
		this.jobrundayid = jobrundayid;
		this.weekdays = weekdays;
		this.schedulename = schedulename;
	}
	
	public Schedule() {
	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Schedule [id=" + id + ", hours=" + hours + ", minutes=" + minutes + ", jobrundayid=" + jobrundayid
				+ ", weekdays=" + weekdays + "]";
	}
	
	
	

}
