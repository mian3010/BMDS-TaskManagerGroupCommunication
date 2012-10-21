package taskManagerGroupCommunication;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "task")
@XmlType(propOrder={"id", "attendantid", "name", "date", "status", "description"})
public class DeprecatedTask {

	private int id, attendantid;
	private String name, date, status, description;
	private static int highestId = 0;
	
	/**
	 * Creates an object in an unstable state, but is used by XML Reflection generation.
	 */
	@SuppressWarnings("unused")
  private DeprecatedTask() {}
	
	public DeprecatedTask(int id, String name, String date, String status, String description,int attendantid) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.status = status;
		this.description = description;
		this.attendantid = attendantid;
		if (highestId < id) highestId = id;
	}
	
	public DeprecatedTask(String name, String date, String status, String description,int attendantid) {
	  this.id = ++highestId;
    this.name = name;
    this.date = date;
    this.status = status;
    this.description = description;
    this.attendantid = attendantid;
  }
	
	@XmlAttribute
	public int getId() {
		return id;
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}

	@XmlAttribute
	public String getDate() {
		return date;
	}

	@XmlAttribute
	public String getStatus() {
		return status;
	}

	public String getDescription() {
		return description;
	}
	
	@XmlAttribute
	public int getAttendantid() {
		return attendantid;
	}
	
	public void setId(int id) {
		this.id = id;
		if (highestId < id) highestId = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setAttendantid(int attendantid) {
		this.attendantid = attendantid;
	}
	
	@Override
	public String toString(){
	  String print = "";
	  print += "#1 Task ID: " + id + "\n";
	  print += "#2 Task name: " + name + "\n";
	  print += "#3 Task date: " + date + "\n";
	  print += "#4 Task status: " + status + "\n";
	  print += "#5 Task description: " + description + "\n";
	  print += "#6 Task attendant: " + attendantid;
	  return print;
	}
	
	/**
	 * This implementation assumes that all users are unique by their id.
	 */
	public int hashCode() {
		return 37 * id;
	}
	
	/**
	 * This implementation assumes that all users are unique by their id.
	 */
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o instanceof DeprecatedTask) {
			DeprecatedTask t = (DeprecatedTask)o;
			if(t.getId() == getId()) return true;
		}
		return false;
	}
	
	public void setRightId() {
	  this.id = ++highestId;
	}
}
