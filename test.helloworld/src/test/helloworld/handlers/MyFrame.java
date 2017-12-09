package test.helloworld.handlers;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MyFrame extends JFrame{
	public static void main(String args[]) {
		new MyFrame();
	}
	public MyFrame() {
		JFrame jf = new JFrame("test");
		Container container = jf.getContentPane();
		container.setLayout(null);
		JLabel jl = new JLabel("这是一个JFrame窗体");
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(jl);
		JButton bl = new JButton("弹出对话框");
		bl.setBounds(10, 10, 100, 21);
		bl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MyJDialog(MyFrame.this).setVisible(true);
			}
		});
		container.add(bl);
		container.setBackground(Color.white);
		jf.setVisible(true);
		jf.setSize(1000, 750);
		jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
