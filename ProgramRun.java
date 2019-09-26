import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class ProgramRun extends JPanel {
	
	Application app = new Application();
	PathSetup pathSetup = new PathSetup();
	
	public static JLabel status,class_heading,branch_heading,dest_heading,subcode_heading,fas_heading;
	public static JTextField class_in,branch_in,dest_in,subcode_in,fas_in;
	public static JButton gen,con,back;
	public static String choice;
	public static int track,i=1;
	
		public ProgramRun()
		{
			Dimension size = getPreferredSize();
			size.width = 500;
			size.height = 460;
			setMaximumSize(size);
			setMinimumSize(size);
			setPreferredSize(size);
			setBackground(Color.decode("#B0e0e6"));
			Border b = BorderFactory.createLineBorder(Color.BLACK,1,true);
			setBorder(BorderFactory.createTitledBorder(b, "Program Run"));
			
			status = new JLabel("Welcom to ResCom!");
			fas_heading = new JLabel("Details (Branch name or Faculty Name & Sec):");
			fas_in = new JTextField(20);
			class_heading = new JLabel("Section's Student List file name: ");
			class_in = new JTextField(15);
			branch_heading = new JLabel("Branch-wise Result file names (separated by ','): ");
			branch_in = new JTextField(25);
			dest_heading = new JLabel("Name of the Output file you want: ");
			dest_in = new JTextField(15);
			subcode_heading = new JLabel("Desired Subject Codes (separated by ','): ");
			subcode_in = new JTextField(25);
			gen = new JButton("Perform Result Analysis");
			con = new JButton("Update Result File");
			back = new JButton("Back");
			
			back.setVisible(false);
			fas_heading.setVisible(false);
			fas_in.setVisible(false);
			class_heading.setVisible(false);
			class_in.setVisible(false);
			branch_heading.setVisible(false);
			branch_in.setVisible(false);
			dest_heading.setVisible(false);
			dest_in.setVisible(false);
			subcode_heading.setVisible(false);
			subcode_in.setVisible(false);
			
			setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			
			//Initial Row///
			gc.weighty = 5;
			
			gc.gridy = 0;
			add(status, gc);
			
			//First Row///
			
			gc.anchor = GridBagConstraints.PAGE_END;
			gc.gridy = 1;
			add(fas_heading, gc);
			
			gc.anchor = GridBagConstraints.CENTER;
			gc.gridy = 2;
			add(fas_in, gc);
			
			//First Row///
			
			gc.anchor = GridBagConstraints.PAGE_END;
			gc.gridy = 3;
			add(class_heading, gc);
			
			gc.anchor = GridBagConstraints.CENTER;
			gc.gridy = 4;
			add(class_in, gc);
			
			//Second Row///
			
			gc.anchor = GridBagConstraints.PAGE_END;
			gc.gridy = 5;
			add(branch_heading, gc);
			
			gc.anchor = GridBagConstraints.CENTER;
			gc.gridy = 6;
			add(branch_in, gc);
			
			//Third Row///
			
			gc.anchor = GridBagConstraints.PAGE_END;
			gc.gridy = 7;
			add(dest_heading, gc);
			
			gc.anchor = GridBagConstraints.CENTER;
			gc.gridy = 8;
			add(dest_in, gc);
			
			//Fourth Row///
			
			gc.anchor = GridBagConstraints.PAGE_END;
			gc.gridy = 9;
			add(subcode_heading, gc);
			
			gc.anchor = GridBagConstraints.CENTER;
			gc.gridy = 10;
			add(subcode_in, gc);
			
			//Final Buttons///
			gc.weighty = 5;
			
			gc.anchor = GridBagConstraints.NORTH;
			gc.gridy = 11;
			add(con, gc);
			
			gc.anchor = GridBagConstraints.NORTH;
			gc.gridy = 12;
			add(gen, gc);
			
			gc.anchor = GridBagConstraints.NORTH;
			gc.gridy = 13;
			add(back, gc);
			
			gen.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					try
					{
						if(track == 0)
						{
							track = 1;
							status.setText("Enter the following details:");
							gen.setText("Generate Output");
							
							fas_heading.setText("Details (Branch name or Faculty Name & Sec):");
							subcode_heading.setText("Desired Subject Codes (separated by ','): ");
							class_heading.setText("Section's Student List file name: ");
							
							con.setVisible(false);
							back.setVisible(true);
							fas_heading.setVisible(true);
							fas_in.setVisible(true);
							class_heading.setVisible(true);
							class_in.setVisible(true);
							branch_heading.setVisible(true);
							branch_in.setVisible(true);
							dest_heading.setVisible(true);
							dest_in.setVisible(true);
							subcode_heading.setVisible(true);
							subcode_in.setVisible(true);
							
						}
						else if(track == 1)
						{
							app.fas = fas_in.getText();
							
							String sub_temp = subcode_in.getText();
							
							app.dest_path = pathSetup.dp + dest_in.getText() + " - X,I,F Grades.txt";
							
							String temp = class_in.getText().trim();
							if(temp.equals("-"))
								app.class_path = pathSetup.bp + branch_in.getText();
							else
								app.class_path = pathSetup.cp + temp;
							app.class_read(app.class_path);
							
							StringTokenizer branch = new StringTokenizer(branch_in.getText(),",");
							int nb = branch.countTokens();
							for(int i=0; i<nb; i++)
							{
								app.branch_path = pathSetup.bp + branch.nextToken().trim();
								StringTokenizer sc = new StringTokenizer(sub_temp,",");
								int nsc = sc.countTokens();
								if(nsc > 1)
									app.multiple = 1;
								else
									app.multiple = 0;
								app.s_loc = app.location("SGPA",app.branch_path);
								for(int j=0;j<nsc;j++)
								{
									app.subcode = sc.nextToken().trim();
									app.sc_loc = app.location(app.subcode, app.branch_path);
									app.result();
									app.sgpaFlag = 1;
								}
							}
							app.output();
							
							status.setText("Your Output File has been generated.");
							gen.setText("Perform Result Analysis Again");
							app.update();
							
							track = 0;
							fas_heading.setVisible(false);
							fas_in.setVisible(false);
							class_heading.setVisible(false);
							class_in.setVisible(false);
							branch_heading.setVisible(false);
							branch_in.setVisible(false);
							dest_heading.setVisible(false);
							dest_in.setVisible(false);
							subcode_heading.setVisible(false);
							subcode_in.setVisible(false);
							con.setVisible(true);
							back.setVisible(false);
						}
					}
					catch(Exception e)
					{
					}
				}
				
			});
			
			con.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					try
					{
						if(track == 0)
						{
							track = 1;
							fas_in.setText("");
							class_in.setText("");
							subcode_in.setText("");
							status.setText("Let both Files be present in Branch Path.");
							con.setText("Update File");
							
							fas_heading.setText("Old Result File name: ");
							class_heading.setText("New Result File name: ");
							subcode_heading.setText("Subject Codes to be updated(separated by ','): ");
							
							back.setVisible(true);
							fas_heading.setVisible(true);
							fas_in.setVisible(true);
							class_heading.setVisible(true);
							class_in.setVisible(true);
							subcode_heading.setVisible(true);
							subcode_in.setVisible(true);
							gen.setVisible(false);
							
						}
						else if(track == 1)
						{
							String temp1 = pathSetup.bp + fas_in.getText();
							String temp2 = pathSetup.bp + class_in.getText();
							String temp3 = pathSetup.dp + fas_in.getText() + " - Updated.txt";
							String sub_temp = subcode_in.getText();
							
							app.replace_check = 1;
							app.class_read(temp2);
							app.replace(temp1, temp2, temp3, sub_temp);
							app.replace_check = 0;
							app.replace_lines = "";
							app.USN = "";
							
							status.setText("An Updated Result File has been generated in Output Path.");
							con.setText("Update Another Result File");
							
							track = 0;
							fas_heading.setVisible(false);
							fas_in.setVisible(false);
							class_heading.setVisible(false);
							class_in.setVisible(false);
							subcode_heading.setVisible(false);
							subcode_in.setVisible(false);
							gen.setVisible(true);
							back.setVisible(false);
						}
					}
					catch(Exception e)
					{
					}
				}
				
			});
			
			back.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					track = 0;
					
					status.setText("Welcom to ResCom!");
					gen.setText("Perform Result Analysis");
					con.setText("Update Result File");
					back.setVisible(false);
					con.setVisible(true);
					gen.setVisible(true);
					
					fas_heading.setVisible(false);
					fas_in.setVisible(false);
					class_heading.setVisible(false);
					class_in.setVisible(false);
					branch_heading.setVisible(false);
					branch_in.setVisible(false);
					dest_heading.setVisible(false);
					dest_in.setVisible(false);
					subcode_heading.setVisible(false);
					subcode_in.setVisible(false);
				}
			});
		}
}
