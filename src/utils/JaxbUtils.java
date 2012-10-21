package utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import taskManagerGroupCommunication.Task;
import taskManagerGroupCommunication.TaskList;


/**
 * Util class for converting xml to Task objects and vice versa
 * @author BieberFever
 *
 */
public class JaxbUtils {
		
	/**
	 * Creates a Task from a given xml-string. Returns null if an error is encountered.
	 * @param xml The xml to unmarshall
	 * @return The Task given from the xml
	 */
	public static Task xmlToTask(String xml) {
		try {
			//Create marshaller context
			JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
			//Convert XML string to inputstream
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			//Unmarshall task from the inputstream
			Task task = (Task) jaxbContext.createUnmarshaller().unmarshal(is);
			return task;
		} catch (JAXBException ex) {
			System.err.println(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * Creates a TaskList from a given xml-string. Returns null if an error is encountered.
	 * @param xml The xml to unmarshall
	 * @return The TaskList given from the xml
	 */
	public static TaskList xmlToTaskList(String xml) {
		try {
			//Create marshaller context
			JAXBContext jaxbContext = JAXBContext.newInstance(TaskList.class);
			//Convert XML string to inputstream
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			//Unmarshall task from the inputstream
			TaskList taskList = (TaskList) jaxbContext.createUnmarshaller().unmarshal(is);
			return taskList;
		} catch (JAXBException ex) {
			System.err.println(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * Creates a XML string from a given Task. Returns null if an error is encountered.
	 * @param task The task to marshall
	 * @return The xml given from the Task
	 */
	public static String taskToXml(Task task) {
		try {
			//Create marshaller context
			JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
			//Create an Stringwriter to marshall into
			StringWriter writer = new StringWriter();
			//Marshall into the writer
			jaxbContext.createMarshaller().marshal(task, writer);
			return writer.toString();
		} catch (JAXBException ex) {
			System.err.println(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * Creates a XML string from a given TaskList. Returns null if an error is encountered.
	 * @param taskList The TaskList to marshall
	 * @return The xml given from the TaskList
	 */
	public static String taskListToXml(TaskList taskList) {
		try {
			//Create marshaller context
			JAXBContext jaxbContext = JAXBContext.newInstance(TaskList.class);
			//Create an Stringwriter to marshall into
			StringWriter writer = new StringWriter();
			//Marshall into the writer
			jaxbContext.createMarshaller().marshal(taskList, writer);
			return writer.toString();
		} catch (JAXBException ex) {
			System.err.println(ex.getMessage());
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		
//		//Testing stuff
//		String testXMLTask = 
//			      "<task id=\"TaskTest\" " +
//			      		"name=\"test task 01\" " +
//			      		"date=\"24-09-2012\" " +
//			      		"status=\"pending\">" +
//			      "<description>Test task in XML</description>" +
//			      "<attendants>TestAttendee</attendants>" +
//			      "</task>";
//		
//		System.out.println(xmlToTask(testXMLTask).description);
//		
//	}
}
