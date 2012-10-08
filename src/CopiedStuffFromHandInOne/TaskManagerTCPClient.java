package CopiedStuffFromHandInOne;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

/**
 * @author BieberFever
 * @author Claus
 * 
 */
public class TaskManagerTCPClient {
  private Socket socket;
  private Scanner keyboard = new Scanner(System.in);
  private InputStream is;
  private DataInputStream dis;
  private DataOutputStream dos;
  private InetAddress inetAddress;
  private int serverPort;

  public TaskManagerTCPClient(InetAddress inetAddress, int serverPort) {
    this.inetAddress = inetAddress;
    this.serverPort = serverPort;
    run();
  }

  private boolean initialize() {
    try {
      // Open a socket for communication.
      socket = new Socket(inetAddress, serverPort);
      // Get data output stream to send a String message to server.
      dos = new DataOutputStream(socket.getOutputStream());
      // Get the inputstream to receive data sent by server.
      is = socket.getInputStream();
      // Create data input stream.
      dis = new DataInputStream(is);
      if (socket.isConnected()) return true;
      else return false;
    } catch (IOException ex) {
      //TODO Something
      return false;
    }
  }

  private void run() {
    System.out.println("Connection created");
    System.out.println("Write QUIT to quit");
    System.out.println("Write GET to receive a list of tasks");
    System.out.println("Write PUT to change a task");
    System.out.println("Write DELETE to delete a task");
    System.out.println("Write POST to add a task");

//    String in;
    while (true) {
//      in = null; // reset

      // If user has input
      if (keyboard.hasNext()) {
        String text = keyboard.next().toLowerCase().trim();
        switch (text) {
        case "q":
        case "quit":
          stop();
          break;
        case "get":
          get();
          break;
        case "put":
          put();
          break;
        case "delete":
          delete();
          break;
        case "post":
          post();
          break;
        case "reset":
        case "r":
          close();
          initialize();
          break;
        }
      }

      // If server has message
//      try {
//        if ((in = dis.readUTF()) != null) {
//          in = "Message from server: " + in;
//          System.out.println(in);
//          Log.log(in);
//        }
//      } catch (IOException e) {
//        Log.error(e.getMessage());
//      }
    }
  }

  private void post() {
    boolean ready = initialize();
    String request = "post";
    if (ready && check(request)) {
      // int id, String name, String date, String status, String description, String attendant
      System.out.println("Please enter the name of the task");
      String name = getString();
      if(name == null){
        cancelRequest();
        return;
      }
      System.out.println("Please enter a date for the task");
      String date = getString();
      if(date == null){
        cancelRequest();
        return;
      }
      System.out.println("Please enter status of the task");
      String status = getString();
      if(status == null){
        cancelRequest();
        return;
      }
      System.out.println("Please enter any description of the task");
      String description = getString();
      if(description == null){
        cancelRequest();
        return;
      }
      System.out.println("Please enter userID of the task attendant");
      int attendant = getInt();
      if(attendant < 0) return;
      System.out.println("Sending...");
      // Create task
      // ID is redefined by server
      Task task = new Task(0, name, date, status, description, attendant);
      // Save task
      try {
        dos.writeUTF("start");
        dos.flush();
        ObjectMarshaller.marshall(task, dos);
        dos.flush();
        System.out.println("Task sent");
      } catch (IOException e) {
        System.out.println("Error saving task: "+e);
      }
    }
    close();
  }
  
  private void cancelRequest(){
    try {
      dos.writeUTF("q");
      dos.flush();
    } catch (IOException e) {
      Log.error(e.getMessage());
    }
  }

  private void delete() {
    boolean ready = initialize();
    String request = "delete";
    // Check if server is ready for request
    if (ready && check(request)) {
      // Get taskID from user
      System.out.println("Write taskID of the task you want to delete");
      int in = getInt();
      if(in < 0){
        cancelRequest();
        return;
      }
      // Are you sure?
      System.out.println("Are you sure you want to delete? Y/N");
      String line = getString();
      if (line.equals("n")) {
        return;
      }
      // Delete task
      try {
        dos.writeUTF(""+in);
        dos.flush();
        System.out.println("Sent delete command");
      } catch (IOException e) {
        Log.error(e.getMessage());
      }
    }
    close();
  }

  private void put() {
    boolean ready = initialize();
    String request = "get";
    // Check if server is ready for request
    if (ready && check(request)) {
      // Get taskID from user
      System.out.println("Write the id of the task you wish to change");
      int id = getInt();
      if(id < 0){
        cancelRequest();
        return;
      }
      // Specify task
      try {
        dos.writeUTF("task:"+id);
        dos.flush();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      // Get task
      Task task = null;
      try {
        task = (Task) ObjectMarshaller.getUnmarshaller(Task.class).unmarshal(dis);
      } catch (JAXBException e) {
        Log.error(e.getMessage());
      }
      close();
      boolean ready2 = initialize();
      request = "put";
      if (ready2 && check(request)) {
        // Which field does user want to edit?
        boolean loop = true;
        while (loop) {
          System.out.println(task);
          System.out.println("Which field do you want to change? Write 0 to save");
          int in = getInt();
          if(in < 0){
            cancelRequest();
            return;
          }
          switch (in) {
          case 0:
            loop = false;
            break;
          case 1:
            System.out.println("New Task ID");
            task.setId(getInt());
            break;
          case 2:
            System.out.println("New Task name:");
            task.setName(getString());
            break;
          case 3:
            System.out.println("New Task date:");
            task.setDate(getString());
            break;
          case 4:
            System.out.println("New Task status:");
            task.setStatus(getString());
            break;
          case 5:
            System.out.println("New Task description:");
            task.setDescription(getString());
            break;
          case 6:
            System.out.println("New Task attendant:");
            task.setAttendantid(getInt());
            break;
          }
        }
        // Send task to server
        try {
          dos.writeUTF("start");
          dos.flush();
          ObjectMarshaller.marshall(task, dos);
          dos.flush();
          System.out.println("Task sent to server");
        } catch (IOException e) {
          System.out.println("Error putting task");
        }
      }
    }
    close();
  }

  private void get() {
    boolean ready = initialize();
    String request = "get";
    // Check if server is ready for request
    if (ready && check(request)) {
      // Get userID from user
      System.out.println("Type user:id to get tasks for user");
      System.out.println("  or task:id to get specific task");
      System.out.println("  or user:0  to get all tasks");
      String in = getString();
      String[] req = in.split(":");
      // Write userID to server
      try {
        dos.writeUTF(in);
        dos.flush();
      } catch (IOException e) {
        Log.error(e.getMessage());
      }
      if (req[0].equals("user")) {
        // Receive calendar with tasks
        ArrayList<Task> tasks = null;
        try {
          Calendar cal = (Calendar) ObjectMarshaller.getUnmarshaller(Calendar.class).unmarshal(is);
          tasks = cal.getTasks();
        } catch (JAXBException e) {
          Log.error(e.getMessage());
        }
        // Print
        if (tasks != null) {
          if (tasks.size() == 0)
            System.out.println("No tasks");
          else {
            for (Task task : tasks) {
              System.out.println("\n" + task);
            }
          }
        }
      } else if (req[0].equals("task")) {
        try {
          Task task = (Task) ObjectMarshaller.getUnmarshaller(Task.class).unmarshal(is);
          System.out.println(task);
        } catch (JAXBException e) {
          Log.error(e.getMessage());
        }
      }
    }
    close();
  }

  /**
   * Get string from user
   * 
   * @return string
   */
  private String getString() {
    String input = keyboard.next().toLowerCase().trim();
    // Did user want to cancel?
    if (input.equals("q"))
      return null;
    return input;
  }

  /**
   * Get integer from user
   * 
   * @return int
   */
  private int getInt() {
    boolean inputIsAccepted = false;
    int input = -1;
    do {
      String in = keyboard.next().toLowerCase().trim();
      try {
        input = Integer.parseInt(in);
        inputIsAccepted = true;
      } catch (NumberFormatException e) {
        // Did the user want to cancel?
        if (in.equals("q")) {
          inputIsAccepted = true;
          input = -1;
        }
        System.out.println("Invalid number. Please type a number or type Q to cancel");
      }
    } while(!inputIsAccepted);
    return input;
  }

  private void close() {
    // close the socket
    if (!socket.isClosed()) {
      try {
        socket.close();
      } catch (IOException e) {
        System.out.println("error message: " + e.getMessage());
      }
    }
  }

  private boolean check(String request) {
    String in = "";
    try {
      dos.writeUTF(request.trim().toLowerCase());
      dos.flush();

      in = dis.readUTF().trim().toLowerCase();
    } catch (IOException e) {
      Log.error(e.getMessage());
      return false;
    }
    if (in.equals(request))
      return true;
    else {
      Log.error(in);
      return false;
    }
  }

  private void stop() {
    System.out.println("Program will now exit");
    // close the socket
    close();
    System.exit(0);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    int serverPort = 7896;
    InetAddress ip = null; 
    try {
      ip = InetAddress.getByName("localhost");
    } catch (UnknownHostException e) {
      System.err.println("error message: " + e.getMessage());
    }

    new TaskManagerTCPClient(ip, serverPort);
  }
}
