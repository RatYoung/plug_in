package test.helloworld.handlers;

import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JScrollPaneTest extends JFrame{
	public static void main(String args[]) throws IOException {
		new JScrollPaneTest();
	}
	
	public JScrollPaneTest() throws IOException {
		Container container = getContentPane();
		container.setLayout(new GridLayout(3, 2, 10, 10));
		
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		JLabel jl1 = new JLabel("Differing Code Elements");
		JTextArea ta1 = new JTextArea(10, 30);
		JScrollPane sp1 = new JScrollPane(ta1);
		p1.add(BorderLayout.NORTH, jl1);
		p1.add(BorderLayout.SOUTH, sp1);
		container.add(p1);
		
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		JLabel jl2 = new JLabel("Requirement Elements");
		/*JTextArea ta2 = new JTextArea(10, 30);
		JScrollPane sp2 = new JScrollPane(ta2);
		p2.add(BorderLayout.NORTH, jl2);
		p2.add(BorderLayout.SOUTH, sp2);
		container.add(p2);*/
		p2.add(BorderLayout.NORTH, jl2);
		p2.add(SampleTableDemo.MyTableTest());
		//TakeFileName.readfile("/Users/mac/Desktop/java_project/test.helloworld/REQ");
		container.add(p2);
		
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		JLabel jl3 = new JLabel("Recommand Method");
		JTextArea ta3 = new JTextArea(10, 30);
		JScrollPane sp3 = new JScrollPane(ta3);
		p3.add(BorderLayout.NORTH, jl3);
		p3.add(BorderLayout.SOUTH, sp3);
		container.add(p3);
		
		JPanel p4 = new JPanel();
		p4.setLayout(new BorderLayout());
		JLabel jl4 = new JLabel("Requirements Text");
		JTextArea ta4 = new JTextArea(10, 30);
		//读取文件内容并显示在JTextArea中
		BufferedReader bre = null;
		try {
			String file = "/Users/mac/Desktop/java_project/test.helloworld/test.txt";
			bre = new BufferedReader(new FileReader(file));
			String str;
			while((str = bre.readLine()) != null) {
				ta4.append(str);
			}
			bre.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		JScrollPane sp4 = new JScrollPane(ta4);
		p4.add(BorderLayout.NORTH, jl4);
		p4.add(BorderLayout.SOUTH, sp4);
		container.add(p4);
		
		JPanel p5 = new JPanel();
		p5.setLayout(new BorderLayout());
		JLabel jl5 = new JLabel("Update Info");
		JTextArea ta5 = new JTextArea(10, 30);
		JScrollPane sp5 = new JScrollPane(ta5);
		p5.add(BorderLayout.NORTH, jl5);
		p5.add(BorderLayout.SOUTH, sp5);
		container.add(p5);
		
		JPanel p6 = new JPanel();
		p6.setLayout(new BorderLayout());
		JButton jb = new JButton("Save");
		//设置按钮点击反应，更新文件内容
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//int content = length(ta5.getText());
				if(!(ta5.getText().equals(""))) {
					//System.out.println(content+ " " + "1");
					new MyJDialog(JScrollPaneTest.this, "日志更新成功").setVisible(true);
				}
				else {
					new MyJDialog(JScrollPaneTest.this, "更新信息不能为空").setVisible(true);
				}
			}
		});
		p6.add(BorderLayout.SOUTH, jb);
		container.add(p6);
		
		setTitle("带滚动条的文字编译器");
		setSize(800, 650);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}
