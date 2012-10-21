package taskManagerGroupCommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

public abstract class RequestParser extends Thread {
  protected String request;
  protected InetAddress source;

  protected Socket con;
  protected DataOutputStream out;
  protected DataInputStream dis;
  protected InputStream is;

  public RequestParser(Socket con, InetAddress source) throws IOException {
    this.con = con;
    this.source = source;
    out = getOutputStream(con);
    is = con.getInputStream();
    dis = new DataInputStream(is);
  }

  public static DataOutputStream getOutputStream(Socket con) throws IOException {
    return new DataOutputStream(con.getOutputStream());
  }

  public String getRequest(Socket con) throws IOException {
    return dis.readUTF();
  }

  public static void writeUTF(DataOutputStream out, String message) throws IOException {
    out.writeUTF(message);
    out.flush();
  }
  
  public static void returnError(Socket con, InetAddress client) throws IOException {
    RequestParser.writeUTF(RequestParser.getOutputStream(con), "Command not found");
  }
  
  public static String getClassName(String command) {
    return "handinone.RequestParser"+command.toUpperCase();
  }

  public void run() {
    try {
      request = getRequest(con);
      if (request.equals("q")) throw new IllegalArgumentException("Not understood, must be quit signal");
      if (request != null) {
        parseRequest(request);
        out.flush();
        out.close();
      }
    } catch (IOException e) {
      System.err.println(e);
    }
    TaskManagerTCPServer.INSTANCE.getCalendar().marshallCalendar(TaskManagerTCPServer.calendarfile);
  }

  public abstract void parseRequest(String request) throws IOException;
}
