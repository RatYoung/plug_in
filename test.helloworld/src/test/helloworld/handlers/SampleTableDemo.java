package test.helloworld.handlers;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class SampleTableDemo {
	public static ArrayList<String> file_name;
	public static Component MyTableTest() throws FileNotFoundException, IOException {
		file_name = null;
		String[] columNames = {"No", "Score", "FileName", "View"};
		file_name = TakeFileName.readfile("/Users/mac/Desktop/java_project/test.helloworld/REQ");
		Object[][] scores = {{"1", "3", file_name.get(0), "0"}, {"2", "7", file_name.get(1), "0"}, {"3", "5", file_name.get(2), "0"}};
		JTable table = new JTable(scores, columNames);
		table.setSize(10, 25);
		return table;
	}
}