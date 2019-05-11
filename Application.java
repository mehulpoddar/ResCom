import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
public class Application
{
	private static PathSetup pathSetup;

	static ArrayList<String[]> row = new ArrayList<String[]>();
	static ArrayList<String[]> row_mul = new ArrayList<String[]>();
	static Application temp;
	static String class_path = pathSetup.cp;
	static String branch_path = pathSetup.bp;
	static String dest_path = pathSetup.dp;
	static String subcode="",fas="",drive = "E:\\";

	static String line="",USN="",details="",replace_lines="";
	static String grades[]={"S+","S","A","B","C","D","E","X","I","F"};
	static int no_of_students,a,b,sc_loc;
	static int count[],sgpaCount[],multiple,replace_check,s_loc,sgpaFlag;
	static float students_attempted,students_passed,pass_percentage;
	
	public static void main(String args[])
	{
		File f = new File(drive);
		if(!f.exists())
			drive = "D:";
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame frame=new MainFrame("ResCom");
				frame.setSize(800, 700);
				//frame.pack();
				frame.setVisible(true);
				frame.setResizable(false);
				frame.getContentPane().setBackground(Color.decode("#20b2aa"));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
	
	public void update()throws IOException
	{
		USN = "";
		details = "";
		count = new int[10];
		sgpaCount = new int[6];
		sgpaFlag = 0;
	}
	
	public static void class_read(String path)throws IOException
	{
		
		BufferedReader br = new BufferedReader(new FileReader(path));
		int loc = location("USN",path);
		try
		{
			while((line=br.readLine())!=null)
			{
				line=line.replace("\t", "        ");
				String temp=locrange(loc,line);
				if(temp.startsWith("1DS"))
				{
					USN = USN + temp + " ";
					if(replace_check == 1)
						replace_lines = replace_lines + line + "$";
				}
			}
		}
		finally
		{
			br.close();
		}
	}
	
	public static void result()throws IOException
	{
		int flag=0;
		int loc=location("USN",branch_path);
		BufferedReader br = new BufferedReader(new FileReader(branch_path));
		try
		{
			while((line=br.readLine())!=null)
			{
				flag=0;
				line=line.replace("\t", "        ");
				String temp=locrange(loc,line);
				StringTokenizer c_usn = new StringTokenizer(USN);
				no_of_students=c_usn.countTokens();
				check: for(int j=0;j<no_of_students;j++)
					{
						if(temp.equalsIgnoreCase(c_usn.nextToken()))
						{
							flag=1;
							break check;
						}
					}
						if(flag==1)
							compute_count(line,temp);
			}
		}
		finally
		{
			br.close();
		}
	}
	
	public static void compute_count(String p_line, String usn)throws IOException
	{
		String temp=locrange(sc_loc,p_line);
		update: for(int i=0;i<10;i++)
		{
			if(temp.equals(grades[i]))
			{
				count[i]++;
				break update;
			}
		}
		if(sgpaFlag == 0)
		{
			int sgpa = (int)Double.parseDouble(locrange(s_loc,p_line));
			switch(sgpa)
			{
				case 10:
				case 9: sgpaCount[0]++;
						break;
				case 8: sgpaCount[1]++;
						break;
				case 7: sgpaCount[2]++;
						break;
				case 6: sgpaCount[3]++;
						break;
				case 5: sgpaCount[4]++;
						break;
				default: sgpaCount[5]++;
					 	 break;
			}
		}
		if(temp.equals("X") || temp.equals("I") || temp.equals("F"))
		{
			int loc=location("NAME",branch_path);
			String name=locrange(loc,p_line);
			details=details+usn+"/"+temp+"/"+subcode+"/"+name+"/";
		}
	}
	
	public static int location(String find,String in)throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(in));
		try
		{
			outer: while((line=br.readLine())!=null)
			{
				line=line.replace("\t", "        ");
				StringTokenizer st = new StringTokenizer(line);
				int n=st.countTokens();
				for(int i=0;i<n;i++)
				{
					String temp=st.nextToken();
					if(temp.equalsIgnoreCase("USN"))
						break outer;
				}
			}
		}
		finally
		{
			br.close();
		}
		line=line.toUpperCase();
		find=find.toUpperCase();
		return line.indexOf(find);
	}
	
	public static String locrange(int loc, String line)
	{
		int len=line.length();
		if(loc>=0 && loc<len)
		{
			a=loc;
			while(a>0)
			{
				if(Character.isWhitespace(line.charAt(a-1)))
					break;
				else
					a--;
			}
			if((loc+7) < len)
				b=loc+7;
			else
				b=len-1;
			while(b<line.length()-1)
			{
				if(Character.isWhitespace(line.charAt(b+1)))
					break;
				else
					b++;
			}
			return line.substring(a,b+1).trim();
		}
		else
			return "";
	}
	
	public static void output()throws IOException
	{
		BufferedWriter br=new BufferedWriter(new FileWriter(dest_path,true));
		try
		{
			String temp[] = new String[15], temp_mul[] = new String[11],memory="";
			int ctr=0,f=0;
			
			temp[0] = fas;
			for(int i=0;i<10;i++)
				temp[i+5] = String.valueOf(count[i]);
			for(int i=0;i<6;i++)
				temp_mul[i+5] = String.valueOf(sgpaCount[i]);
			br.newLine();
			br.newLine();
			br.newLine();
			br.write(temp[0]);
			br.newLine();
			br.newLine();
			br.write("Student details of those who received 'X', 'I' and 'F' grades:");
			br.newLine();
			br.newLine();
			br.write("   USN    \tGrade   \tSubCode   \tName");
			br.newLine();
			br.write("   ___    \t_____   \t_______   \t____");
			br.newLine();
			details=details.trim();
			StringTokenizer st=new StringTokenizer(details,"/");
			int n=st.countTokens()/4;
			for(int i=0;i<n;i++)
			{
				String u,g,s,name;
				
				u = st.nextToken();
				g = st.nextToken();
				s = st.nextToken();
				name = st.nextToken();
				
				br.write(u+"\t  "+g+"\t\t"+s+"\t\t"+name);
				br.newLine();
				if(g.equalsIgnoreCase("X") || g.equalsIgnoreCase("F"))
				{
					if(f==0)
					{
						f=1;
						memory = memory + u + " ";
						ctr++;
					}
					else
					{
						int rep = 0;
						StringTokenizer mem = new StringTokenizer(memory);
						int nm = mem.countTokens();
						for(int k=0;k<nm;k++)
						{
							if(u.equalsIgnoreCase(mem.nextToken()))
							{
								rep = 1;
								break;
							}
						}
						if(rep == 0)
						{
							ctr++;
							memory = memory + u + " ";
						}
					}
				}
			}
			
			students_attempted = no_of_students-count[8];
			students_passed=no_of_students-ctr;
			pass_percentage=(students_passed/students_attempted)*100;
			
			temp[1] = String.valueOf(no_of_students);
			temp[2] = String.valueOf((int)students_attempted);
			temp[3] = String.valueOf((int)students_passed);
			temp[4] = String.valueOf(pass_percentage);
			
			temp_mul[1] = String.valueOf(no_of_students);
			temp_mul[2] = String.valueOf((int)students_attempted);
			temp_mul[3] = String.valueOf((int)students_passed);
			temp_mul[4] = String.valueOf(pass_percentage);
			
			row.add(temp);
			row_mul.add(temp_mul);
			new Output();
		}
		finally
		{
			br.close();
		}
	}
	
	public static void setup()throws IOException
	{
		BufferedWriter br=new BufferedWriter(new FileWriter(drive+"\\ResCom Setup.txt"));
		try
		{
			br.write(pathSetup.cp);
			br.newLine();
			br.write(pathSetup.bp);
			br.newLine();
			br.write(pathSetup.dp);
		}
		finally
		{
			br.close();
		}
	}
	
	public static void replace(String file1, String file2, String file3, String sub_temp)throws IOException
	{
		String line = "",usn = "";
		int loc=location("USN",file1);
		BufferedReader of = new BufferedReader(new FileReader(file1));
		BufferedWriter uf = new BufferedWriter(new FileWriter(file3,true));
		StringTokenizer nl = new StringTokenizer(replace_lines,"$");
		int n = nl.countTokens(); 
		String rl[] = new String[n];
		for(int i = 0; i<n; i++)
			rl[i] = nl.nextToken();
		
		try
		{
			while((line=of.readLine()) != null)
			{
				line = line.replace("\t", "        ");
				usn = locrange(loc,line);
				StringTokenizer nu = new StringTokenizer(USN);
				int flag = 0;
				int pos = 0;
				for(int i=0; i<n; i++)
				{
					if(usn.equalsIgnoreCase(nu.nextToken()))
					{
						flag = 1;
						pos = i;
						break;
					}
				}
				if(flag == 1)
				{
					StringTokenizer sc = new StringTokenizer(sub_temp,",");
					int nsc = sc.countTokens();
					for(int j=0;j<nsc;j++)
					{
						String sub = sc.nextToken().trim();
						int nloc = location(sub,file2);
						String ng = locrange(nloc, rl[pos]);
						int oloc = location(sub,file1);
						String og = locrange(oloc, line);
						
						if(!ng.equals(""))
						{
							int na = oloc;
							char ch = ' ';
							while(ch == ' ')
								ch = line.charAt(na++);
							na--;
							int nb = na;
							ch = 'a';
							while(ch != ' ')
							ch = line.charAt(++nb);
							line = line.substring(0,na)+ng+line.substring(nb);
						}
						System.out.print(line);
					}
				}
				uf.write(line);
				uf.newLine();
			}
		}
		finally
		{
			of.close();
			uf.close();
		}
	}
}
