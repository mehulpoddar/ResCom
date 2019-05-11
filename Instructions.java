import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;

public class Instructions extends JPanel {
	
	JTextArea instructions = new JTextArea();
	String space = "              ";
	
		public Instructions()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					Dimension size = getPreferredSize();
					size.width = 270;
					size.height = 650;
					setMaximumSize(size);
					setMinimumSize(size);
					setPreferredSize(size);
					setBackground(Color.decode("#20b2aa"));
					Border b = BorderFactory.createLineBorder(Color.BLACK,1,true);
					setBorder(BorderFactory.createTitledBorder(b, "Information"));
					instructions.setBackground(Color.decode("#fafad2"));
					instructions.setEditable(false);
					
					setLayout(new BorderLayout());
					add(instructions, BorderLayout.CENTER);
					
					instructions.setText("  How to use:\n"
							+ "  1.Use File Conversion Links from Read Me\n"
							+ "  File to Convert all PDF and Excel files to .txt.\n"
							+ "  You can use 'Update Result File' to update\n"
							+ "  files after Make-up or Fastrack Exams,etc.\n\n"
							+ "  2.Paths of the Input and Output files\n"
							+ "  have to be given. You have 2 options:\n"
							+ "  You can enter paths of wherever your\n"
							+ "  files are saved and submit it.\n"
							+ "  Eg: D:\\ResCom\\Files\\ - Exactly in this format.\n"
							+ "  For C: Drive, use allowed folders.\n"
							+ "  Next time when you use the software\n"
							+ "  you can 'Use Paths from previous Run.'\n"
							+ "  - You can Create a Folder named\n"
							+ "  'ResCom' in E: drive(if E: not available use D:),\n"
							+ "   save your files there and 'Use Default Paths'.\n\n"
							+ "  3.Click 'Perform Result Analysis', enter all\n"
							+ "  details as instructed in the Read Me File.\n\n"
							+ "  4.You can run again for different class\n"
							+ "  or branch files or subject codes, and it will\n"
							+ "  all be stacked up in tabular form.");
					instructions.append("\n\n"+space+space+"ResCom 2017\n\n"
							+ "  Software from:\n"
							+ "  Department of Computer Science & Engg.,\n"
							+ "  Dayananda Sagar College of Engineering.\n\n"
							+ "  Designed and Developed by:\n"
							+ " - Dr. D R Ramesh Babu,\n"
							+ "   Vice Principal, Dept. of CSE, DSCE\n"
							+ " - Prof. Monika P,\n"
							+ "   Assistant Professor, Dept. of CSE, DSCE\n"
							+ " - Mehul Poddar\n"
							+ "   Student of III Sem, 'B' Section\n"
							+ "   Dept. of CSE, DSCE\n");
				}
			});
		}
}

