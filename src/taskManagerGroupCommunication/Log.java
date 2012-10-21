package taskManagerGroupCommunication;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Write log file
 * 
 * @author BieberFever
 * @author Claus
 * 
 */
public class Log {
  private static File log;
  private static PrintWriter print;

  /**
   * Write to log-file
   * 
   * @param s
   *          String to log
   */
  public static void log(String s) {
    if (!isInitialized())
      initialize();
    print.println(new Date() + ": " + s);
    print.flush();
  }

  /**
   * Write to log and write error to console
   * 
   * @param s
   *          error
   */
  public static void error(String s) {
    System.err.println("Error: " + s);
    log(s);
  }

  /**
   * Initialize log-writer
   */
  private static void initialize() {
    try {
      log = new File("./log.txt");
      if (!log.exists()) log.createNewFile();
      print = new PrintWriter(log);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   * @return is initialized
   */
  private static boolean isInitialized() {
    return (log != null && print != null);
  }
}
