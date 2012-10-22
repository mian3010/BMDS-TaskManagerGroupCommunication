package taskManagerGroupCommunication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.JGroupHelper;

public class TaskClient {
	private static Scanner sc;
	private static boolean quit = false;
	private static JGroupHelper groupHelper;
	private static TaskList sabotage;
	
	public static void main(String[] args) {
		/******************************
		 * SECRET SABOTAGE CREATION!! *
		 ******************************/
		List<Task> tlist = new ArrayList<>();
		tlist.add(
				new Task(
						"1337",
						"L337 T45K",
						"RIGHT NOW",
						"YOU'RE GOING",
						"It's a party for people who all think you're a noob."
						+"Seriously. Everyone here hates you.",
						"You."
				)
		);
		tlist.add(
				new Task(
						"42",
						"The Question Will Be Revealed",
						"2012-42-42",
						"Cool",
						"So, we all know the answer to the great question about life, the universe, "+
						"and everything. What we don't know is the question. This is where you get it.",
						"You."
				)
		);
		sabotage = new TaskList(tlist);
		/******************************
		 * SECRET SABOTAGE CREATION!! *
		 ******************************/
		
		System.out.println("WELCOME TO THE TASK MACHINE, BRO");
		System.out.println("HOLD ON WHILE I FIX YOU A CONNECTION...");
		groupHelper = new JGroupHelper("BROCHANNEL","127.0.0.1",51924);
		System.out.println("OK, SO THAT'S GOOD. NOW LET ME WAIT FOR YOUR INPUT, BRO");
		sc = new Scanner(System.in);
		System.out.println("OK, SO NOW WE'RE READY FOR YOU, 'KAY? SO HERE'S WHAT I WANT YOU TO DO...");
		System.out.println(" - SO YOU CAN GET ALL TASKS BY TYPING IN 'GET', THAT WILL PRINT 'EM");
		System.out.println(" - THEN YOU CAN, LIKE, ADD A NEW TASK... I'LL HELP YOU THRU IT JUST TYPE 'ADD'");
		System.out.println(" - OR YOU CAN ADD AN ENTIRE LIST, JUST GO 'ADDLIST'");
		System.out.println(" - ... IF YOU WANNA LEAVE, JUST DO THE 'QUIT'");
		System.out.println("... I'M WAITING");
		System.out.println();
		while(!quit) {
			menuInteract();
		}
		groupHelper.close();
	}
	
	public static void menuInteract() {
		System.out.println("GET|ADD|ADDLIST|QUIT");
		
		boolean illiterate = true;
		while(illiterate) {
			String s = sc.nextLine();
			switch(s.toUpperCase()) {
				case("GET"):
					illiterate = false;
					getTasks();
					break;
				case("ADD"):
					illiterate = false;
					addTask();
					break;
				case("ADDLIST"):
					illiterate = false;
					addTaskList();
					break;
				case("QUIT"):
					illiterate = false;
					quit = true;
					System.out.println("YEAH, YOU BETTER RUN!");
					break;
				default:
					System.out.println("WHAT ARE YOU SOME ILLITERATE PERSON OR SOMETHING?");
					System.out.println("TRY AGAIN, BRO");
					break;
			}
		}
	}
	
	public static void getTasks() {
		System.out.println();
		System.out.println("OK SO YOU WANNA GET THEM TASKS? OK.");
		TaskList list = groupHelper.getTasks();
		if(list.getList().size() > 0) {
			System.out.println("ID\t| NAME\t| STATUS");
			System.out.println("----------------------------------------->");
			for(Task t : list.getList()) {
				System.out.println(t.id + "\t| "+t.name+"\t| "+t.status);
			}
		}
		else {
			System.out.println("NO TASKS, BRO");
		}
		System.out.println("WE COOL?");
		System.out.println();
	}
	
	public static void addTask() {
		System.out.println();
		System.out.println("OK, ADDING TASKS... HERE'S HOW IT GOES. I NAME AN ATTRIBUTE, YOU NAME THE VALUE, OK?");
		System.out.print("ID:\t");
		String id = sc.nextLine();
		System.out.print("NAME:\t");
		String name = sc.nextLine();
		System.out.print("DATE (YYYY-MM-DD):\t");
		String date = sc.nextLine();
		System.out.print("STATUS:\t");
		String status = sc.nextLine();
		System.out.print("DESCRIPTION:\t");
		String description = sc.nextLine();
		System.out.print("ATTENDANTS:\t");
		String attendants = sc.nextLine();
		Task newTask = new Task(id,name,date,status,description,attendants);
		groupHelper.addTask(newTask);
		System.out.println("LOL I ADDED IT !!!!111!!1!1!11111111one");
		System.out.println();
	}
	
	public static void addTaskList() {
		System.out.println();
		System.out.println("OH IM SORRY. DID YOU THINK I WOULD LET YOU ADD A LIST?");
		System.out.println("OH NO, WE COULDN'T TRUST YOU WITH THIS. I MADE A LIST, THOUGH.");
		System.out.println("THIS LIST IS GOING STRAIGHT TO YOUR GROUP! MWAHAHAHA");
		System.out.println("CONSIDER YOURSELF SABOTAGED!");
		groupHelper.addTaskList(sabotage);
		System.out.println();
	}
}
