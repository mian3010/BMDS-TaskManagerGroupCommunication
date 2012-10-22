package utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskManagerGroupCommunication.Task;
import taskManagerGroupCommunication.TaskList;

public class JGroupHelperTest {
  
  static JGroupHelper target;

  @Before
  public void setUpBefore() throws Exception {
    target = new JGroupHelper("TestGroup");
  }

  @After
  public void tearDownAfter() throws Exception {
    target.close();
  }
  
  @Test
  public void testAddTask() {
    TaskList tasks = target.getTasks();
    assertTrue(tasks.getList().isEmpty());
    
    Task task = new Task("1", "Test task", "", "", "", "");
    target.addTask(task);
    
    //Sleep
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }
    
    tasks = target.getTasks();
    assertTrue(tasks.getList().contains(task));
  }

  @Test
  public void testAddTaskList() {
    TaskList tasks = target.getTasks();
    assertTrue(tasks.getList().isEmpty());
    
    Task task = new Task("1", "Test task", "", "", "", "");
    TaskList taskList = new TaskList();
    taskList.getList().add(task);
    
    target.addTaskList(taskList);
    
    //Sleep
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }

    tasks = target.getTasks();
    boolean containsSameTasks = true;
    
    assertFalse(tasks.getList().isEmpty());
    assertFalse(taskList.getList().isEmpty());
    
    for(Task t : taskList.getList()){
      if(!tasks.getList().contains(t)){
        containsSameTasks = false;
        break;
      }
    }
    assertTrue(containsSameTasks);
  }

  @Test
  public void testGetTasks() {
    TaskList tasks = target.getTasks();
    assertTrue(tasks.getList().isEmpty());
    
    Task task = new Task("1", "Test task", "", "", "", "");
    target.addTask(task);
    
    //Sleep
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }
    
    tasks = target.getTasks();
    assertTrue(tasks.getList().contains(task));
  }
  
  @Test
  public void testGroup(){
    TaskList tasks = target.getTasks();
    assertTrue(tasks.getList().isEmpty());
    
    Task task = new Task("1", "Test task", "", "", "", "");
    target.addTask(task);
    
    //Sleep
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }
    
    JGroupHelper target2 = new JGroupHelper("TestGroup");
    
    tasks = target2.getTasks();
    assertTrue(tasks.getList().contains(task));
    
    //Test adding with the second target and getting with the first
    Task task2 = new Task("2", "Test task2", "", "", "", "");
    target2.addTask(task2);
    
    //Sleep
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }
    
    tasks = target.getTasks();
    assertTrue(tasks.getList().contains(task2));
    
    target2.close();
  }
}
