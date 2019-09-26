import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
	
private static PathSetup pathSetup;
private static ProgramRun programRun;
private static Instructions instructions;
	
	public MainFrame(String title)
	{
		super(title);
		
		JPanel panel=new JPanel(new BorderLayout());
		panel.setBackground(Color.decode("#20b2aa"));
		//Layout Manager
		setLayout(new BorderLayout());
		
		//Swing Component Creation
		instructions = new Instructions();
		pathSetup = new PathSetup();
		programRun = new ProgramRun();
		
		
		//Adding Component to Content Pane
		Container c = getContentPane();
		
		//instructions.setAlignmentX(c.RIGHT_ALIGNMENT);
		//instructions.setAlignmentY(c.TOP_ALIGNMENT);
		
		//pathSetup.setAlignmentX(c.LEFT_ALIGNMENT);
		//pathSetup.setAlignmentY(c.TOP_ALIGNMENT);
		
		//programRun.setAlignmentX(c.LEFT_ALIGNMENT);
		//programRun.setAlignmentY(c.BOTTOM_ALIGNMENT);
		
		//setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
		c.add(instructions, BorderLayout.WEST);
		panel.add(pathSetup, BorderLayout.NORTH);
		panel.add(programRun, BorderLayout.SOUTH);
		c.add(panel, BorderLayout.EAST);
	}
}
