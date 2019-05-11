import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class Output extends JDialog {
	
	private static Application app = new Application();
	public Output()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				//JLabel intro = new JLabel("Generated using ResCom, Department of Computer Science and Engg.");
				JPanel panel=new JPanel(new BorderLayout());
				Border b = BorderFactory.createLineBorder(Color.BLACK,1,true);
				panel.setBorder(BorderFactory.createTitledBorder(b, bold("Generated using ResCom"), TitledBorder.CENTER, TitledBorder.TOP, new Font("pickwick",Font.PLAIN,16), Color.BLACK));
				String headings1[] = {bold("Details"),bold("Total No. of Students"),bold("Students Attempted"),bold("No. of Students Passed"),bold("Pass Percentage")};
				String headings2a[] = {bold("No. Failed"), bold("9 - 10"), bold("8 - 8.9"), bold("7 - 7.9"), bold("6 - 6.9"), bold("5 - 5.9"), bold("below 5")};
				String headings2b[] = {bold("S+"),bold("S"),bold("A"),bold("B"),bold("C"),bold("D"),bold("E"),bold("X"),bold("I"),bold("F")};
				String data1[][],data2a[][],data2b[][];
				double chart_count[] = new double[10];
				double chart_count_mul[] = new double[7];
				double multiple_count[] = new double[2];
				DefaultPieDataset dataset = new DefaultPieDataset();
				
				String temp[], temp_mul[];
				int m = app.row.size(), k;
				data1 = new String[m][5];
				data2a = new String[m][7];
				data2b = new String[m][10];
				for(int i=0;i<m;i++)
				{
					temp = app.row.get(i);
					temp_mul = app.row_mul.get(i);
					
					for(int j=0;j<5;j++)
						data1[i][j] = bold(temp[j]);
					
					data2a[i][0] = String.valueOf(Integer.parseInt(temp[1]) - Integer.parseInt(temp[3]));
					chart_count_mul[0] = chart_count_mul[0] + Double.parseDouble(data2a[i][0]);
					
					k = 0;
					for(int j=5;j<15;j++)
					{
						data2b[i][k] = bold(temp[j]);
						chart_count[k] = chart_count[k] + Double.parseDouble(temp[j]);
						if(k < 6)
						{
							data2a[i][k+1] = bold(temp_mul[j]);
							chart_count_mul[k+1] = chart_count_mul[k+1] + Double.parseDouble(temp_mul[j]);
						}
						
						if(app.multiple != 1)
							dataset.setValue(app.grades[k], new Double(chart_count[k]));
						k++;
					}
					multiple_count[0] = multiple_count[0] + (Double.parseDouble(temp[2])); // Total Attempted
					multiple_count[1] = multiple_count[1] + Double.parseDouble(temp[3]); // Total Passed
				}
				if(app.multiple == 1)
				{
					double per = (multiple_count[1] / multiple_count[0]) * 100;
					dataset.setValue("Number of students Failed", new Double(multiple_count[0] - multiple_count[1]));
					dataset.setValue(
							"Percentage of Students Passed (" + (int)(multiple_count[1])
							+ "/" + (int)(multiple_count[0]) + ")", new Double(per));
				}
				
				//panel.add(new JScrollPane(intro), BorderLayout.NORTH);
				
				JTable jt = new JTable(data1,headings1);
				jt.setEnabled(false);
				jt.setBounds(300,400,20,30);
				jt.setBackground(Color.YELLOW);
				panel.add(new JScrollPane(jt), BorderLayout.CENTER);
				
				JTable jt2;
				if(app.multiple == 1)
					jt2 = new JTable(data2a,headings2a);
				else
					jt2 = new JTable(data2b,headings2b);
				jt2.setEnabled(false);
				jt2.setBounds(300,400,20,30);
				jt2.setBackground(Color.CYAN);
				panel.add(new JScrollPane(jt2), BorderLayout.EAST);
				
				JScrollPane sp = new JScrollPane(panel);
				JDialog d = new JDialog();
				d.setLayout(new BorderLayout());
				d.add(sp, BorderLayout.NORTH);
				
				d.setSize(1200, 700);
				d.setVisible(true);
				d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				JPanel panel2=new JPanel(new BorderLayout());
				Dimension size = getPreferredSize();
				size.width = 1150;
				size.height = 300;
				panel2.setPreferredSize(size);
				//panel2.add(new JScrollPane(intro), BorderLayout.NORTH);
				
			    JFreeChart chart = ChartFactory.createPieChart(      
			            "Generated using ResCom",   // chart title 
			            dataset,          // data    
			            true,             // include legend   
			            true, 			// Use tooltips
			            false);			// Configure chart to generate URLs?
				
			    ChartPanel cp = new ChartPanel(chart);
			    cp.setPreferredSize(size);
			    panel2.add(cp);
			    d.add(panel2, BorderLayout.SOUTH);
			    
				saveImage(panel,chart);
			}
		});
	}
	void saveImage(JPanel p, JFreeChart c)
	{
		BufferedImage img = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		p.paint(g2);
		try
		{
			String new_path = app.dest_path;
			int l = new_path.length();
			new_path = new_path.substring(0,l-16);
			ImageIO.write(img, "PNG", new File(new_path+"Table.png"));
			ChartUtilities.saveChartAsPNG(new File(new_path+"Pie Chart.png"), c, 640, 480); //width,height
		}
		catch(Exception e){}
	}
	String bold(String temp)
	{
		temp = "<html><b>"+temp+"</b></html>";
		return temp;
	}
}
