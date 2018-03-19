package test.helloworld.handlers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
		container.setLayout(new BorderLayout());
		JLabel jl = new JLabel("这是一个JFrame窗体");
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(BorderLayout.CENTER, jl);
		String border[] = {BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.EAST, BorderLayout.WEST};
		for(int i = 0; i < 3; i++) {
			JButton bl = new JButton("弹出对话框" + (i+1));
			bl.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//new MyJDialog(MyFrame.this).setVisible(true);
				}
			});
			container.add(border[i], bl);
		}
		JButton bl = new JButton("弹出对话框" + 4);
		bl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new JScrollPaneTest().setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		container.add(border[3], bl);
		container.setBackground(Color.white);
		jf.setVisible(true);
		jf.setSize(500, 350);
		jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
