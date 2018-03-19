package test.helloworld.handlers;

import java.awt.Container;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

class MyJDialog extends JDialog{
	public MyJDialog(JScrollPaneTest frame, String message) {
		super(frame, "Message", true);
		Container container = getContentPane();
		JLabel jl = new JLabel(message);
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(jl);
		setBounds(220, 120, 200, 100);
	}
}