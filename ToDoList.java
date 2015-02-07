import java.sql.*;
import java.util.Scanner;

import dbUtil.DBUtil;
import functions.ToDoFunctions;

public class ToDoList {

	public static void main(String[] args) {
		System.out.println("ToDoList Application");
		Scanner sc = new Scanner(System.in);
		int choice = 0;
		String command = null;
		ToDoFunctions tdf = new ToDoFunctions();
		Connection c = null;
		Statement st = null;

		try {
			c = DBUtil.getConnection();
			st = c.createStatement();
			String sql = " CREATE TABLE IF NOT EXISTS TODO_LIST ("
					+ " TODO_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ " TODO_TEXT TEXT NOT NULL,"
					+ " ISDONE TEXT NOT NULL DEFAULT N) ";
			st.executeUpdate(sql);
			System.out.println("*******************************");
			System.out.println("Type 'help' for list of commands. 'exit' to exit the application");

			while (choice <= 5) {
				System.out.println(" Please enter a command : ");
				command = sc.nextLine();
				if (command.startsWith("todo list"))
					choice = 2;
				else if (command.startsWith("todo add"))
					choice = 1;
				else if (command.startsWith("todo done"))
					choice = 3;
				else if (command.startsWith("help"))
					choice = 4;
				else if (command.startsWith("exit"))
					choice = 5;
				else
					choice = 0;
				switch (choice) {
				case 1:
					tdf.addItem(c, command);
					break;
				case 2:
					tdf.showList(c);
					break;
				case 3:
					tdf.markComplete(c, command);
					break;
				case 4:
					tdf.help();
					break;
				case 5:
					System.exit(0);
				default:
					System.out.println("Enter a valid command ==>");
				}
			}

		} catch (SQLException e) {
			System.err.println("*SQL Exception Occurred*");
			e.printStackTrace();
		} finally {
			try {
				st.close();
				c.close();
			} catch (SQLException e) {
				System.out
						.println("Exception occurred while closing JDBC Resources");
				e.printStackTrace();
			}
		}
	}
}
