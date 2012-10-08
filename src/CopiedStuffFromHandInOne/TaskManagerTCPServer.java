package CopiedStuffFromHandInOne;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.xml.bind.JAXBException;

/**
 * @author BieberFever
 * @author Claus
 * 
 */
public enum TaskManagerTCPServer {
  INSTANCE;
  
  private Calendar calendar = new Calendar();
  public static File calendarfile = new File("calendar.xml");

  /**
   * @author BieberFever
   * @param args
   */
  public static void main(String[] args) {
    try {
      TaskManagerTCPServer.INSTANCE.calendar = Calendar.loadCalendar(calendarfile);
      TaskManagerTCPServer.INSTANCE.run(7896);
    } catch (JAXBException|IOException e) {
      System.out.println("Could not load or create calendar file: "+e);
    }
  }
  
  public Calendar getCalendar() {
    return calendar;
  }

  public void run(int port) {
    ServerSocket ss = null;
    try {
      ss = new ServerSocket(port);
    } catch (IOException e) {
      System.err.println("Could not start server: " + e);
      System.exit(-1);
    }
    System.out.println("FileServer accepting connections on port " + port);

    while (true) {
      try {
        // Listens for request. It stays on this line until a request is made to
        // the server
        Socket con = ss.accept();
        InetAddress client = ss.getInetAddress();
        DataInputStream dis = new DataInputStream(con.getInputStream());
        String request = dis.readUTF();
        try {
          //The next lines use Javas reflection to create the right object from the request
          //Find the class with the name corresponding to the request
          @SuppressWarnings({ "unchecked" })
          Class<RequestParser> classDefinition = (Class<RequestParser>) Class.forName(RequestParser.getClassName(request));
          //Set up a variable containing the types that the constructor should take
          @SuppressWarnings("rawtypes")
          Class[] constructorArgumentTypes = new Class[] {Socket.class, InetAddress.class};
          //Set up a variable containing the actual arguments the constructor should take
          Object[] constructorArguments = new Object[] {con, client};
          //Find the right constructor from the types it should take
          Constructor<RequestParser> constructor = classDefinition.getConstructor(constructorArgumentTypes);
          //Create an instance using the constructor, taking the arguments from the variable above
          RequestParser p = constructor.newInstance(constructorArguments);
          //Start the thread object
          p.start();
          //Respond that everything went well to the client
          DataOutputStream out = RequestParser.getOutputStream(con);
          RequestParser.writeUTF(out, request);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          e.printStackTrace();
          RequestParser.returnError(con, client);
        }
      } catch (IOException e) {
        System.err.println(e);
      }
    }
  }

  public static void log(InetAddress client, String msg) {
    System.err.println(new Date() + ": from " + client + " - " + msg);
  }
}
