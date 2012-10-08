package CopiedStuffFromHandInOne;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Delete an item from the task list
 * 
 * @author BieberFever
 * @author Claus
 * 
 */
public class RequestParserDELETE extends RequestParser {
  public RequestParserDELETE(Socket con, InetAddress source) throws IOException {
    super(con, source);
  }

  public void parseRequest(String request) throws IOException {
    int id = Integer.parseInt(request);
    if(id == 0) return; // Illegal ID. Client didn't want to delete
    int userId = TaskManagerTCPServer.INSTANCE.getCalendar().removeTask(id);
    if (userId > 0) TaskManagerTCPServer.log(source, "DELETE: Removed task with id "+id+" from user "+userId);
    else TaskManagerTCPServer.log(source, "DELETE: Error removing task");
  }
}
