package functions;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import dbUtil.DBUtil;

public class ToDoFunctions {

	Scanner sc = null;
	Statement st = null;
	ResultSet rs = null;

	public void addItem(Connection c, String command) throws SQLException {
		try {
			if (c == null || c.isClosed())
				c = DBUtil.getConnection();
			String item = command.substring(9);
			String sql = " INSERT INTO TODO_LIST (TODO_TEXT) " + " VALUES('"
					+ item + "')";
			st = c.createStatement();
			int rowCount = st.executeUpdate(sql);
			if (rowCount > 0)
				System.out.println("TODO Item updated !");

		} catch (SQLException e) {
			System.out.println("SQLException in addItem");
			e.printStackTrace();
		} finally {
			st.close();
			c.close();
		}

	}

	public void showList(Connection c) {
		try {
			if (c == null || c.isClosed())
				c = DBUtil.getConnection();

			String sql = "SELECT * FROM TODO_LIST";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			System.out.println(" The Current Todo List : \n");
			while (rs.next()) {
				int todoId = rs.getInt(1);
				String todoItem = rs.getString(2);
				String isDone = rs.getString(3);
				System.out.print(todoId + ".");
				if (isDone.equalsIgnoreCase("Y"))
					strikeThrough(todoItem);
				else
					System.out.print(todoItem +"\n");
				//System.out.print(" -> " + isDone +"\n");
			}
			System.out.println("");

		} catch (SQLException e) {
			System.out.println("SQLException in showList");
			e.printStackTrace();
		}

	}

	private void strikeThrough(String todoItem) {
		AnsiConsole.systemInstall();
		Ansi ansi = new Ansi();
		ansi.a(Ansi.Attribute.STRIKETHROUGH_ON);
		ansi.a(todoItem);
		ansi.reset();
		System.out.print(ansi+"\n");

	}

	public void markComplete(Connection c, String command) {
		try {
			if (c == null || c.isClosed())
				c = DBUtil.getConnection();

			int todoId = Integer.parseInt(command.substring(10));
			String sql = " UPDATE TODO_LIST SET ISDONE ='Y' "
					+ " WHERE TODO_ID = " + todoId;
			st = c.createStatement();
			st.executeUpdate(sql);
			System.out.println(" Item # " + todoId + " marked completed");
		} catch (SQLException e) {
			System.out.println("SQLException in markComplete");
			e.printStackTrace();
		}

	}

	public void help() {
		System.out.println("List of commands : ");
		System.out.println("todo list : lists all the items in the list ");
		System.out.println("todo add <your_item_description> : Adds this item to the list");
		System.out.println("todo done <item#> : Marks the item number mentioned as complete");

	}

}
