package CopiedStuffFromHandInOne;

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
public class Calendar {
  private HashMap<Integer, User> users = new HashMap<>();
  private HashMap<Integer, HashMap<Integer, Task>> tasks = new HashMap<>();
	
	public Calendar() {}
	
	public Calendar(ArrayList<User> userlist, ArrayList<Task> tasklist) {
	  for (User user : userlist) {
	    this.addUser(user);
	  }
		for (Task task : tasklist) {
		  if (!tasks.containsKey(task.getAttendantid())) throw new IllegalArgumentException("Orphaned task found");
		  tasks.get(task.getAttendantid()).put(task.getId(), task);
		}
	}
	
	public User getUser(int id) {
	  return users.get(id);
	}
	
	public void addUser(User user) {
		users.put(user.getId(), user);
		tasks.put(user.getId(), new HashMap<Integer, Task>());
	}
	
	public Task getTask(int id) {
	  for(HashMap<Integer, Task> tsks : tasks.values()){
      if(tsks.containsKey(id)){
        return tsks.get(id);
      }
    }
    return null;
	}
	
	public int removeTask(int id){
	  for(HashMap<Integer, Task> tsks : tasks.values()){
	    if(tsks.containsKey(id)){
	      Task removed = tsks.remove(id);
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
	public ArrayList<User> getUsers() {
	  ArrayList<User> usersReturn = new ArrayList<>();
	  for (Map.Entry<Integer, User> user : users.entrySet()) {
	    usersReturn.add(user.getValue());
	  }
		return usersReturn;
	}

	public void setUsers(ArrayList<User> userlist) {
	  users.clear();
	  tasks.clear();
		for (User user : userlist) {
		  this.addUser(user);
		}
	}

	public void addTask(Task task) throws NullPointerException {
		tasks.get(task.getAttendantid()).put(task.getId(), task);
	}
	
	public void removeTask(Task task) {
		tasks.get(task.getAttendantid()).remove(task);
	}
	
	@XmlElementWrapper(name = "tasks")
	@XmlElement(name = "task")
	public ArrayList<Task> getTasks() {
	  ArrayList<Task> tasksReturn = new ArrayList<>();
	  for (Map.Entry<Integer, HashMap<Integer, Task>> taskslist : tasks.entrySet()) {
	    tasksReturn.addAll(taskslist.getValue().values());
	  }
		return tasksReturn;
	}


	public void setTasks(ArrayList<Task> tasklist) {
		for (Task task : tasklist) {
		  if (tasks.containsKey(task.getAttendantid())) tasks.get(task.getAttendantid()).put(task.getId(), task);
		  else throw new IllegalArgumentException("No such user");
		}
	}
	
	public void updateTask(Task update) {
	  tasks.get(update.getAttendantid()).put(update.getId(), update);
	}
	
	public ArrayList<Task> getListOfTasks(int userid) throws NullPointerException {
	  ArrayList<Task> tasksReturn = new ArrayList<Task>();
	  tasksReturn.addAll(tasks.get(userid).values());
	  return tasksReturn;
	}
	
  public static void generateEmptyCalendar(File calendarfile) throws IOException {
    calendarfile.createNewFile();
    Calendar c = new Calendar();
    ObjectMarshaller.marshall(c, new FileOutputStream(calendarfile));
  }
  
  public static Calendar loadCalendar(File calendarfile) throws JAXBException, IOException {
    if (!calendarfile.exists()) Calendar.generateEmptyCalendar(calendarfile);
     return (Calendar) ObjectMarshaller.getUnmarshaller(Calendar.class).unmarshal(calendarfile);
  }
  
  public static void saveCalendar(Calendar calendar, File calendarfile) throws FileNotFoundException {
    ObjectMarshaller.marshall(calendar, new FileOutputStream(calendarfile));
  }
  
  public void marshallCalendar(File file) {
    ObjectMarshaller.marshall(this, file);
  }

	public static void main (String[] args) {
		Calendar cal = new Calendar();
		
		User user1 = new User("Niclas Tollstorff", "pw123");
		User user2 = new User("Niels Rosen Abildgaard", "bieberrox");
		User user3 = new User("Michael Soeby Andersen", "bieberrox");
		User user4 = new User("Claus L. Henriksen", "bieberrox");

		Task task1 = new Task(1, "Do MDS Mandatory Exercise 1", "17-09-12", "nearly done", "Handin one", user1.getId());
		Task task2 = new Task(2, "Do MDS Mandatory Exercise 1", "17-09-12", "nearly done", "Handin one", user2.getId());
		Task task3 = new Task(3, "Do MDS Mandatory Exercise 1", "17-09-12", "nearly done", "Handin one", user3.getId());
		Task task4 = new Task(4, "Do MDS Mandatory Exercise 1", "17-09-12", "nearly done", "Handin one", user4.getId());

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
		Calendar cal2 = new Calendar();
	    try {
	      cal2 = (Calendar) ObjectMarshaller.getUnmarshaller(Calendar.class).unmarshal(new File("./bossen.xml"));
	  		for (Task task : cal2.getTasks()) {
	  		  System.out.println(task);
	  		}
	  		System.out.println("Unmarshall succesfull");
	    } catch (JAXBException e) {
	      e.printStackTrace();
	    }
	}
}
