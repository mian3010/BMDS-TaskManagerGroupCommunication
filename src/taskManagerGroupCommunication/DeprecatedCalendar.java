package taskManagerGroupCommunication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "cal")
@XmlType(propOrder={"users", "tasks"})
public class DeprecatedCalendar {
  private HashMap<Integer, DeprecatedUser> users = new HashMap<>();
  private HashMap<Integer, HashMap<Integer, DeprecatedTask>> tasks = new HashMap<>();
	
	public DeprecatedCalendar() {}
	
	public DeprecatedCalendar(ArrayList<DeprecatedUser> userlist, ArrayList<DeprecatedTask> tasklist) {
	  for (DeprecatedUser user : userlist) {
	    this.addUser(user);
	  }
		for (DeprecatedTask task : tasklist) {
		  if (!tasks.containsKey(task.getAttendantid())) throw new IllegalArgumentException("Orphaned task found");
		  tasks.get(task.getAttendantid()).put(task.getId(), task);
		}
	}
	
	public DeprecatedUser getUser(int id) {
	  return users.get(id);
	}
	
	public void addUser(DeprecatedUser user) {
		users.put(user.getId(), user);
		tasks.put(user.getId(), new HashMap<Integer, DeprecatedTask>());
	}
	
	public DeprecatedTask getTask(int id) {
	  for(HashMap<Integer, DeprecatedTask> tsks : tasks.values()){
      if(tsks.containsKey(id)){
        return tsks.get(id);
      }
    }
    return null;
	}
	
	public int removeTask(int id){
	  for(HashMap<Integer, DeprecatedTask> tsks : tasks.values()){
	    if(tsks.containsKey(id)){
	      DeprecatedTask removed = tsks.remove(id);
	      return (removed != null) ? removed.getAttendantid() : -1;
	    }
	  }
	  return -1;
	}
	
	public void removeUser(String userid) {
		users.remove(userid);
	}
	
	@XmlElementWrapper(name = "users")
	@XmlElement(name = "user")
	public ArrayList<DeprecatedUser> getUsers() {
	  ArrayList<DeprecatedUser> usersReturn = new ArrayList<>();
	  for (Map.Entry<Integer, DeprecatedUser> user : users.entrySet()) {
	    usersReturn.add(user.getValue());
	  }
		return usersReturn;
	}

	public void setUsers(ArrayList<DeprecatedUser> userlist) {
	  users.clear();
	  tasks.clear();
		for (DeprecatedUser user : userlist) {
		  this.addUser(user);
		}
	}

	public void addTask(DeprecatedTask task) throws NullPointerException {
		tasks.get(task.getAttendantid()).put(task.getId(), task);
	}
	
	public void removeTask(DeprecatedTask task) {
		tasks.get(task.getAttendantid()).remove(task);
	}
	
	@XmlElementWrapper(name = "tasks")
	@XmlElement(name = "task")
	public ArrayList<DeprecatedTask> getTasks() {
	  ArrayList<DeprecatedTask> tasksReturn = new ArrayList<>();
	  for (Map.Entry<Integer, HashMap<Integer, DeprecatedTask>> taskslist : tasks.entrySet()) {
	    tasksReturn.addAll(taskslist.getValue().values());
	  }
		return tasksReturn;
	}


	public void setTasks(ArrayList<DeprecatedTask> tasklist) {
		for (DeprecatedTask task : tasklist) {
		  if (tasks.containsKey(task.getAttendantid())) tasks.get(task.getAttendantid()).put(task.getId(), task);
		  else throw new IllegalArgumentException("No such user");
		}
	}
	
	public void updateTask(DeprecatedTask update) {
	  tasks.get(update.getAttendantid()).put(update.getId(), update);
	}
	
	public ArrayList<DeprecatedTask> getListOfTasks(int userid) throws NullPointerException {
	  ArrayList<DeprecatedTask> tasksReturn = new ArrayList<DeprecatedTask>();
	  tasksReturn.addAll(tasks.get(userid).values());
	  return tasksReturn;
	}
	
  public static void generateEmptyCalendar(File calendarfile) throws IOException {
    calendarfile.createNewFile();
    DeprecatedCalendar c = new DeprecatedCalendar();
    ObjectMarshaller.marshall(c, new FileOutputStream(calendarfile));
  }
  
  public static DeprecatedCalendar loadCalendar(File calendarfile) throws JAXBException, IOException {
    if (!calendarfile.exists()) DeprecatedCalendar.generateEmptyCalendar(calendarfile);
     return (DeprecatedCalendar) ObjectMarshaller.getUnmarshaller(DeprecatedCalendar.class).unmarshal(calendarfile);
  }
  
  public static void saveCalendar(DeprecatedCalendar calendar, File calendarfile) throws FileNotFoundException {
    ObjectMarshaller.marshall(calendar, new FileOutputStream(calendarfile));
  }
  
  public void marshallCalendar(File file) {
    ObjectMarshaller.marshall(this, file);
  }

	public static void main (String[] args) {
		DeprecatedCalendar cal = new DeprecatedCalendar();
		
		DeprecatedUser user1 = new DeprecatedUser("Niclas Tollstorff", "pw123");
		DeprecatedUser user2 = new DeprecatedUser("Niels Rosen Abildgaard", "bieberrox");
		DeprecatedUser user3 = new DeprecatedUser("Michael Soeby Andersen", "bieberrox");
		DeprecatedUser user4 = new DeprecatedUser("Claus L. Henriksen", "bieberrox");

		DeprecatedTask task1 = new DeprecatedTask(1, "Do MDS Mandatory Exercise 1", "17-09-12", "nearly done", "Handin one", user1.getId());
		DeprecatedTask task2 = new DeprecatedTask(2, "Do MDS Mandatory Exercise 1", "17-09-12", "nearly done", "Handin one", user2.getId());
		DeprecatedTask task3 = new DeprecatedTask(3, "Do MDS Mandatory Exercise 1", "17-09-12", "nearly done", "Handin one", user3.getId());
		DeprecatedTask task4 = new DeprecatedTask(4, "Do MDS Mandatory Exercise 1", "17-09-12", "nearly done", "Handin one", user4.getId());

		cal.addUser(user1);
	  cal.addUser(user2);
	  cal.addUser(user3);
	  cal.addUser(user4);
		cal.addTask(task1);
		cal.addTask(task2);
		cal.addTask(task3);
		cal.addTask(task4);
		
		//marshall
		ObjectMarshaller.marshall(cal, "./calendar.xml");
		System.out.println("Marshall succesfull");
		//unmarshall
		DeprecatedCalendar cal2 = new DeprecatedCalendar();
	    try {
	      cal2 = (DeprecatedCalendar) ObjectMarshaller.getUnmarshaller(DeprecatedCalendar.class).unmarshal(new File("./bossen.xml"));
	  		for (DeprecatedTask task : cal2.getTasks()) {
	  		  System.out.println(task);
	  		}
	  		System.out.println("Unmarshall succesfull");
	    } catch (JAXBException e) {
	      e.printStackTrace();
	    }
	}
}
