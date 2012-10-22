package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.protocols.BARRIER;
import org.jgroups.protocols.FD_ALL;
import org.jgroups.protocols.FD_SOCK;
import org.jgroups.protocols.FRAG2;
import org.jgroups.protocols.MERGE2;
import org.jgroups.protocols.MFC;
import org.jgroups.protocols.PING;
import org.jgroups.protocols.UDP;
import org.jgroups.protocols.UFC;
import org.jgroups.protocols.UNICAST2;
import org.jgroups.protocols.VERIFY_SUSPECT;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.protocols.pbcast.STATE_TRANSFER;
import org.jgroups.stack.ProtocolStack;
import org.jgroups.util.Util;

import taskManagerGroupCommunication.Task;
import taskManagerGroupCommunication.TaskList;


/**
 * A helper class which provides an abstraction over the JGroup functionality
 * for assignment #3 in BMDS-2012
 * @author BieberFever
 *
 */
public class JGroupHelper {
	
	public static void main(String[] args) {
		//Small test example. Run concurrent instances of this application to test it.
		JGroupHelper j = new JGroupHelper("BieberFeverGroup", "127.0.0.1", 51924);
		Task t = new Task(Integer.toString(new Random().nextInt(9999)), "Rick Astley", "TODAY", "Completed", "This is a Rick Roll", "The BieberFever team" );
		j.addTask(t);
	}
	
	private JChannel channel;
	private final TaskList taskListState = new TaskList();
	
	/**
	 * Creates a helper on the local machine
	 * 
	 * @param groupIdentifier A String that identifies the group to connect to
	 */
	public JGroupHelper(String groupIdentifier) {
		try {
			channel = new JChannel();
			channel.setReceiver(new JGroupEventHandler());
			channel.connect(groupIdentifier, null, 0);
			channel.getState(null, 0);
		}
		catch (Exception ex) {
			//Runtime exception with info for use in debugging
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * Creates a helper on the given IP and port.
	 * For this assignment use a local destination:
	 * IP: 127.0.0.1
	 * Port: 51924
	 * 
	 * @param groupIdentifier A String that identifies the group to connect to 
	 * @param ipAddress The IP of the destination
	 * @param port The port of the destination
	 */
	public JGroupHelper(String groupIdentifier, String ipAddress,int port) {
		try {
			channel = RaosChannelHelper.getNewChannel(ipAddress, port);
			channel.setReceiver(new JGroupEventHandler());
			channel.connect(groupIdentifier);
			channel.getState(null, 0);
		}
		catch (Exception ex) {
			//Runtime exception with info for use in debugging
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * Sends a task to every member of the JGroup
	 * @param task The task to send
	 */
	public void addTask(Task task) {
		Message msg=new Message(null, null, task);
		try {
			channel.send(msg);
		}
		catch (Exception ex) {
			//Runtime exception with info for use in debugging
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * Sends a TaskList to every member of the JGroup
	 * @param taskList The TaskList to send
	 */
	public void addTaskList(TaskList taskList) {
		Message msg=new Message(null, null, taskList);
		try {
			channel.send(msg);
		}
		catch (Exception ex) {
			//Runtime exception with info for use in debugging
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * Returns every task in the JGroups current state
	 * @return Every task in the JGroups current state
	 */
	public TaskList getTasks() {
		return taskListState;
	}
	
	/**
	 * Closes the underlying JGroup structures
	 */
	public void close() {
		channel.close();
	}
	
	/**
	 * Provides the receiver functionality for this assignment
	 * @author BieberFever
	 */
	private class JGroupEventHandler extends ReceiverAdapter {
		/**
		 * Called implicitly when a new member joins the group
		 */
		@Override
		public void viewAccepted(View joiner) {
		}
		
		/**
		 * Called implicitly upon receiving a message from the group
		 * This implementation expects either a Task or a TaskList in the message
		 */
		@Override
		public void receive(Message msg) {
		    //If a Task was received
		    if (msg.getObject() instanceof Task) {
		    	Task t = (Task) msg.getObject();
		    	System.out.println("> New task received. ID: " + t.id);
		    	synchronized(taskListState) {
		    		//add task if it isn't already in the state
	    			if (!taskListState.getList().contains(t)) taskListState.getList().add(t); 
		        }
		    }
		    //If a TaskList was received
		    else if (msg.getObject() instanceof TaskList) {
		    	synchronized(taskListState) {
		    		for(Task t : ((TaskList)msg.getObject()).getList()) {
		    			//add every task which isn't already in the state
		    			if (!taskListState.getList().contains(t)) taskListState.getList().add(t);
		    		}
		        }
		    } //Should never reach this, but added in case of future versions
		    else throw new IllegalArgumentException("> The receiver only supports Task and TaskList objects");
		   
		}
		
		/**
		 * Called implicitly on the group's coordinator as part of the JChannel's getState() method.
		 * Returns the current state of the JGroup in the given OutputStream
		 */
		@Override
		public void getState(OutputStream output) throws Exception {
			synchronized(taskListState) {
	            Util.objectToStream(taskListState, new DataOutputStream(output));
	        }
	    }
		
		/**
		 * Called implicitly on the synchronizing member as part of the JChannel's getState() method.
		 * Synchronizes the state based on the given InputStream
		 */
		@Override
		public void setState(InputStream input)throws Exception {
			TaskList t = (TaskList)Util.objectFromStream(new DataInputStream(input));
	        synchronized(taskListState) {
	            taskListState.getList().addAll(t.getList());
	        }
	    }
	}
		
	
	/**
	 * Provided by Rao, based on from http://www.jgroups.org/manual-3.x/html/user-channel.html#CreatingAChannel
	 * @author Rao
	 */
	private static class RaosChannelHelper {

	    // Usage: pass the ipaddress and a port number to create channel.
	    // For example you can pass  values: "127.0.0.1" and 51924 to create a JChannel on your local computer.
	    //  
	    // 
	    public static JChannel getNewChannel(String ipAddress,int port) throws UnknownHostException, Exception {
	        // The following code taken from an example on programmatic creation  from JGroups documentation. 
	        // Therefore refer to http://www.jgroups.org/manual-3.x/html/user-channel.html#CreatingAChannel page
	        // for documentation and explanation of the following code fragment. 
	        
	        JChannel ch = new JChannel(false);
	        ProtocolStack stack = new ProtocolStack();
	        ch.setProtocolStack(stack);
	        
	        UDP udp = new UDP();
	        udp.setBindAddress(InetAddress.getByName(ipAddress));
	        udp.setMulticastPort(port);
	        
	        stack.addProtocol(udp)
	                .addProtocol(new PING())
	                .addProtocol(new MERGE2())
	                .addProtocol(new FD_SOCK())
	                .addProtocol(new FD_ALL()
	                .setValue("timeout", 12000)
	                .setValue("interval", 3000))
	                .addProtocol(new VERIFY_SUSPECT())
	                .addProtocol(new BARRIER())
	                .addProtocol(new NAKACK())
	                .addProtocol(new UNICAST2())
	                .addProtocol(new STABLE())
	                .addProtocol(new GMS())
	                .addProtocol(new UFC())
	                .addProtocol(new MFC())
	                .addProtocol(new FRAG2())
	                .addProtocol(new STATE_TRANSFER());
	        
	        stack.init();
	        return ch;
	    }
	}
}
