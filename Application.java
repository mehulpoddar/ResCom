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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.Package;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
	
	public static String[] splitStr(String line)
	{
			String temp[] = new String[40];
			for(int j=0;j<temp.length;j++)
				temp[j] = "";
			
			int count = 0, a = 0;
		
			char[] characters = (line.trim()).toCharArray();
			            
			for(int k=0; k<characters.length; k++)
			{
				if(!Character.isWhitespace(characters[k]))
					temp[a] = temp[a] + characters[k];	
				else
				{
					count++;
				
					if(count == 4)
					{
						count = 1;
						a++;
						temp[a] = " ";
					}
					else if((k+1) >= characters.length && count >= 3)
					{
						a++;
						temp[a] = " ";
					}
					else if(!Character.isWhitespace(characters[k+1]))
					{
						count = 0;
						a++;
					}
				}
			}
			
			int flag=1;

			for(int l=1;l<4;l++)
			{
				char ch = line.charAt(line.length()-l);
				if(!Character.isWhitespace(ch))
					flag=0;	
			}

			if(flag==1)
				temp[++a]=" ";
			
			String inter[]=new String[a+1];
			for(int k=0;k<=a;k++)
				inter[k]=temp[k];
			return inter;
	}
	
	public static void convertPDF(String path, String conPath) throws IOException
	{
		PDDocument doc = PDDocument.load(new File(path)); // put path to your input pdf file here
		String text =  new PDFTextStripper().getText(doc);
		System.out.println(text);
		   
		File file=new File(conPath);
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

		int starti=0,aftercol=0;
		String lines[] = text.split("\\r?\\n");

		   
		for(int i=0;i<lines.length;i++)
		{
		   	if(lines[i].toUpperCase().contains("USN"))
		   	{
		   		String words[]=lines[i].split("\\s+");
		   		int count = 0;
		   	
			   	for(int j=0;j<words.length;j++)
			   	{
	
				   	if(words[j].matches("[0-9]+") && words[j].length() >= 1) // headings which have only numbers will not be printed
				   		count++;
				   	
				   	else if(words[j].toUpperCase().contains("NAME"))
				   	{
				   		starti=j;
				   		pw.write(StringUtils.center(words[j],30)); 
				   		//System.out.print(StringUtils.center(words[j],30));
				   	}
				   	
				   	else
				   	{
				   		pw.write(StringUtils.center(words[j],15));
				   		//System.out.print(StringUtils.center(words[j],15));// printing headings other than usn,name...
				   	}
			   	
			   	}
			   	pw.println();
			   	aftercol = words.length - starti - count - 1;
			   	//System.out.println();
		   	
		   	}
		   	else if(lines[i].contains("1DS"))
		   	{ 
		   		int endi=0;
		   		String str="";
		   	
		        String temp[] = splitStr(lines[i]);
		        endi = temp.length - aftercol - 1;
		   	   
			   	for(int l=0;l<starti;l++)//data before name
			   	{
			   	   	pw.print(StringUtils.center(temp[l],15));
			   	   	//System.out.print(StringUtils.center(temp[l],15));
			   	}
			       
			   	for(int k=starti;k<=endi;k++)//name
			   		str = str+" "+temp[k];
			                
			   	pw.print(StringUtils.center(str, 30));
			   	//System.out.print(StringUtils.center(str, 30));
			   	
			   	for (int m=endi+1;m<temp.length;m++)//after columns
			   	{
			   		pw.write(StringUtils.center(temp[m], 15));
			   		//System.out.print(StringUtils.center(temp[m],15));
			   	}
			   	pw.println();
			   	//System.out.println();
		   	}
		   	else
		   	{ 
		   		pw.println(lines[i]);
		   		//System.out.println(lines[i]);
		   	}
		}
		pw.close();
	}


	public static void convertExcel(String path, String conPath) throws IOException
	{
		File file=new File(conPath);
		
		PrintWriter pw = new PrintWriter(file);
		
		if(path.contains(".xlsx"))
		{
		
			FileInputStream fis = new FileInputStream(path);
	
		     // Finds the workbook instance for XLSX file
		     XSSFWorkbook myWorkBook = new XSSFWorkbook(path);
		    
		     // Return first sheet from the XLSX workbook
		     XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		    
		     // Get iterator to all the rows in current sheet
		     Iterator<Row> rowIterator = mySheet.iterator();
		    
		     // Traversing over each row of XLSX file
		     while (rowIterator.hasNext()) {
		         Row row = rowIterator.next();
		
		         // For each row, iterate through each columns
		         Iterator<Cell> cellIterator = row.cellIterator();
		         while (cellIterator.hasNext()) {
		
		             Cell cell = cellIterator.next();
		
		             switch (cell.getCellType()) {
		             case Cell.CELL_TYPE_STRING:
		            	 if(cell.getStringCellValue().contentEquals("USN"))
		            	 {
		                 //System.out.print(cell.getStringCellValue() + "\t\t");
		                 pw.write(cell.getStringCellValue() + "\t\t");
		            	 }
		            	 else
		            	 {
		            	 //System.out.print(cell.getStringCellValue() + "\t");
		                 pw.write(cell.getStringCellValue() + "\t");
		            	 }
		                 break;
		             case Cell.CELL_TYPE_NUMERIC:
		                 //System.out.print(Math.round(cell.getNumericCellValue()) + "\t");
		                 pw.write(Math.round(cell.getNumericCellValue()) + "\t");
		                 break;
		             case Cell.CELL_TYPE_BOOLEAN:
		                 //System.out.print(cell.getBooleanCellValue() + "\t");
		                 pw.write(cell.getBooleanCellValue() + "\t");
		                 break;
		             default :
		          
		             }
		        
		         }  //System.out.print("\n");
		         pw.println();
		     }pw.close(); 
	    }
		else
		{
			FileInputStream fis2 = new FileInputStream(path);
	
			  //Get the workbook instance for XLS file 
			  HSSFWorkbook workbook = new HSSFWorkbook(fis2);
	
			  //Get first sheet from the workbook
			  HSSFSheet sheet = workbook.getSheetAt(0);
	
			  //Iterate through each rows from first sheet
			   Iterator<Row> rowIterator = sheet.iterator();
			   while(rowIterator.hasNext()) {
			     Row row = rowIterator.next();
	
			     //For each row, iterate through each columns
			    Iterator<Cell> cellIterator = row.cellIterator();
			    while(cellIterator.hasNext()) {
	
			        Cell cell = cellIterator.next();
	
			        switch(cell.getCellType()) {
			            case Cell.CELL_TYPE_BOOLEAN:
			                //System.out.print(cell.getBooleanCellValue() + "\t");
			                pw.write(cell.getBooleanCellValue() + "\t");
			                break;
			            case Cell.CELL_TYPE_NUMERIC:
			                //System.out.print(Math.round(cell.getNumericCellValue()) + "\t");
			                pw.write(Math.round(cell.getNumericCellValue()) + "\t");
			                break;
			            case Cell.CELL_TYPE_STRING:
			            	if(cell.getStringCellValue().contentEquals("USN"))
			            	{
			                //System.out.print(cell.getStringCellValue() + "\t\t");
			                pw.write(cell.getStringCellValue() + "\t\t");
			            	}
			            	else
			            	{
			            		//System.out.print(cell.getStringCellValue() + "\t");
			            		pw.write(cell.getStringCellValue() + "\t");
			            	}
			                break;
			        }
			    }
			    //System.out.println("");
			    pw.println();
			}
			pw.close();
		}
	}
	
	public static String convertFile(String path, int pdf) throws IOException
	{
		String conPath = path + "_converted.txt";
		
		File f = new File(conPath);
		if(!f.exists())
		{
			if(pdf == 1)
				convertPDF(path+".pdf",conPath);
			else
			{
				File tempf = new File(path+".xls");
				if(tempf.exists())
					convertExcel(path+".xls",conPath);
				else
					convertExcel(path+".xlsx",conPath);
			}
		}
		
		return conPath;
	}
	
	public static void class_read(String path)throws IOException
	{
		String conPath = "";
		if(replace_check == 1)
			conPath = convertFile(path,1);
		else
			conPath = convertFile(path,0);
		
		BufferedReader br = new BufferedReader(new FileReader(conPath));
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
		BufferedReader br = new BufferedReader(new FileReader(branch_path+"_converted.txt"));
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
			System.out.println("Hey");
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
			String s = locrange(s_loc,p_line);
			if(!s.equals(""))
			{
				int sgpa = (int)Double.parseDouble(s);
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
		String conPath = convertFile(in,1);
		BufferedReader br = new BufferedReader(new FileReader(conPath));
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
		String conPath1 = convertFile(file1,1);

		int loc=location("USN",file1);
		
		BufferedReader of = new BufferedReader(new FileReader(conPath1));
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
