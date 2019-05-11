import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class PathSetup extends JPanel {
	
	Application app = new Application();
	
	public static String cp,bp,dp;

	public PathSetup()
	{
		
		Dimension size = getPreferredSize();
		size.width = 520;
		size.height = 200;
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		setBackground(Color.decode("#20b2aa"));
		Border b = BorderFactory.createLineBorder(Color.BLACK,1,true);
		setBorder(BorderFactory.createTitledBorder(b, "Path Setup"));
		
		JLabel heading = new JLabel("Enter FOLDER Paths of:");
		JLabel class_heading = new JLabel("Class File: ");
		JLabel branch_heading = new JLabel("Branch File: ");
		JLabel dest_heading = new JLabel("Output File: ");
		
		JTextField class_path = new JTextField(25);
		JTextField branch_path = new JTextField(25);
		JTextField dest_path = new JTextField(25);
		
		JButton path_read = new JButton("Use Paths from previous Run");
		JButton submit = new JButton("Submit your Paths");
		JButton def = new JButton("Use Default Paths");
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		//First Row///
		gc.weighty = 2;
		gc.anchor = GridBagConstraints.LINE_START;
		
		gc.gridx = 0;
		gc.gridy = 0;
		add(heading, gc);
		
		//Second Row///
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 0;
		gc.gridy = 1;
		add(class_heading, gc);
		
		gc.gridwidth = 2;
		gc.gridx = 1;
		gc.gridy = 1;
		add(class_path, gc);
		
		//Third Row///
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 0;
		gc.gridy = 2;
		add(branch_heading, gc);
		
		gc.gridwidth = 2;
		gc.gridx = 1;
		gc.gridy = 2;
		add(branch_path, gc);
				
		//Fourth Row///
				
		gc.anchor = GridBagConstraints.LINE_START;
		
		gc.gridx = 0;
		gc.gridy = 3;
		add(dest_heading, gc);
		
		gc.gridwidth = 2;
		gc.gridx = 1;
		gc.gridy = 3;
		add(dest_path, gc);
		
		//Final Row
		gc.gridwidth = 1;
		gc.anchor = GridBagConstraints.CENTER;
		
		gc.gridx = 0;
		gc.gridy = 5;
		add(submit, gc);
		
		gc.gridx = 1;
		gc.gridy = 5;
		add(def, gc);
		
		gc.gridx = 2;
		gc.gridy = 5;
		add(path_read, gc);
		
		submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try
				{
					cp = class_path.getText();
					bp = branch_path.getText();
					dp = dest_path.getText();
				
					cp = cp.replace("\\", "\\\\");
					bp = bp.replace("\\", "\\\\");
					dp = dp.replace("\\", "\\\\");
				
					app.update();
					app.setup();
				}
				catch(Exception e)
				{
				}
			}
			
		});
		
		path_read.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try
				{
					BufferedReader br = new BufferedReader(new FileReader(app.drive+"\\ResCom Setup.txt"));
					cp = br.readLine();
					bp = br.readLine();
					dp = br.readLine();
					
					class_path.setText(cp.replace("\\\\", "\\"));
					branch_path.setText(bp.replace("\\\\", "\\"));
					dest_path.setText(dp.replace("\\\\", "\\"));
					
					br.close();
					app.update();
				}
				catch(Exception e)
				{
				}
			}
			
		});
		
		def.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try
				{
					cp = app.drive+"\\ResCom\\";
					bp = app.drive+"\\ResCom\\";
					dp = app.drive+"\\ResCom\\";
					
					class_path.setText(cp.replace("\\\\", "\\"));
					branch_path.setText(bp.replace("\\\\", "\\"));
					dest_path.setText(dp.replace("\\\\", "\\"));
				
					app.update();
				}
				catch(Exception e)
				{
				}
			}
			
		});
	}
}
