package taskManagerGroupCommunication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.xml.bind.JAXBException;

/**
 * Add a task to the task list
 * 
 * @author BieberFever
 * @author Claus
 * 
 */
public class RequestParserPOST extends RequestParser {
  public RequestParserPOST(Socket con, InetAddress source) throws IOException {
    super(con, source);
  }

  public void parseRequest(String request) throws IOException {
    try {
      DeprecatedTask task = (DeprecatedTask) ObjectMarshaller.getUnmarshaller(DeprecatedTask.class).unmarshal(is);
      task.setRightId();
      TaskManagerTCPServer.INSTANCE.getCalendar().addTask(task);
      TaskManagerTCPServer.log(source, "POST: Added task with id "+task.getId()+" for user "+task.getAttendantid());
    } catch (JAXBException|NullPointerException e) {
      TaskManagerTCPServer.log(source, "POST: Error adding. Maybe attendantid is wrong?");
      e.printStackTrace();
    }
  }
}
