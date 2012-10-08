package CopiedStuffFromHandInOne;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.xml.bind.JAXBException;

/**
 * Update a task in the task list
 * 
 * @author Claus
 * 
 */
public class RequestParserPUT extends RequestParser {
  public RequestParserPUT(Socket con, InetAddress source) throws IOException {
    super(con, source);
  }

  public void parseRequest(String request) throws IOException {
    try {
      Task updatedTask = (Task) ObjectMarshaller.getUnmarshaller(Task.class).unmarshal(is);
      TaskManagerTCPServer.INSTANCE.getCalendar().updateTask(updatedTask);
      TaskManagerTCPServer.log(source, "PUT: Updated task with id "+updatedTask.getId());
    } catch (JAXBException e) {
      TaskManagerTCPServer.log(source, "PUT: Task could not be updated");
    }
    
  }
}
