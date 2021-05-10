package Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Greedy {
	
	
	public static boolean isCut(Link link, double line, boolean vert)
	{
		boolean flag = false;
		int x1 = link.getP1().getX();
		int y1 = link.getP1().getY();
		int x2 = link.getP2().getX();
		int y2 = link.getP2().getY();
		
		if(vert)
		{
			if((line>x1 && line <x2) || (line<x1 && line>x2))
			{
				flag =true;
			}
		}
		else
		{
			if((line>y1 && line <y2) || (line<y1 && line>y2))
			{
				flag =true;
			}
		}
		
		return flag;
	}
	
	public static int getCuts(double line, double currLine, ArrayList<Link> links, boolean vert)
	{
		int countCuts = 0;

		for(Link link : links)
		{
			if(isCut(link, line, vert))
			{
				countCuts++;
			}
		}

		return countCuts;
	}
	
	public static void main(String[] args) throws Exception {
		//String fileName = args[0];
		File file = new File("instance02.txt");
		String fileName = file.getName();
		String fileno = fileName.replaceAll("\\D+","");

		String currline;
		int n = 0;
		BufferedReader br = new BufferedReader(new FileReader(file));
		currline = br.readLine();
		//get firstline from file which gives total points
		n = Integer.parseInt(currline);
		
		ArrayList<String> solution = new ArrayList<>();
		ArrayList<Point> points = new ArrayList<Point>();
		
		// bounds for "graph"
		double minX =0, minY =0, maxX = 0, maxY =0;
		//readfile line by line, add points to list
		while((currline = br.readLine())!=null)
		{
			String[] temp= currline.trim().split("\\s+");
			int x = Integer.parseInt(temp[0]);
			int y = Integer.parseInt(temp[1]);
			points.add(new Point(x,y));	
			if(x > maxX)
				maxX =x;
			if(x < minX)
				minX = x;
			if(y > maxY)
				maxY = y;
			if(y < minY)
				minY = y;	
		}	 
		br.close(); 
		
		// establish all links between each point in graph
		ArrayList<Link> links = new ArrayList<Link>();
		for(int i = 0; i < points.size(); i++)
		{
			for( int j = i+1; j < points.size(); j++)
			{
				links.add(new Link(points.get(i), points.get(j)));
			}
		}
		
		double line;
		boolean orientation = true; //true means verticle, false means horizontal
		
		while((links.size()) > 0)
		{
			double currVLine =0, currHLine = 0, currLine =0;
			int hMax = 0, vMax = 0;
			orientation = false;
			double h1 = minY + .5;
			double v1 = minX + .5;
			
			while(true)
			{
				if(h1 > maxY)
				{
					break;
				}
				int hLineCuts = getCuts(h1, currLine,links, false);
				if(hMax< hLineCuts)
				{
					hMax = hLineCuts;
					currHLine = h1;
				}
				h1 += 1;
			}
			
			
			while(true)
			{
				if(v1 > maxX)
					break;
				int vLineCuts = getCuts(v1, currLine,links, true);
				if(vMax< vLineCuts)
				{
					vMax = vLineCuts;
					currVLine = v1;
				}
				
				v1+=1;
			}
			
			line = currHLine;
			
			if( hMax < vMax)
			{
				orientation = true;
				line = currVLine;
			}
			
			if(orientation)
				solution.add("v " + line);
			else
				solution.add("h " + line);
			
			ArrayList<Link> toRemove = new ArrayList<Link>();
			for(Link link: links)
			{
				if(isCut(link, line, orientation))
					toRemove.add(link);
			}
			for(Link link : toRemove)
			{
				links.remove(link);
			}
			toRemove.clear(); 
			
		}

		for(String sol : solution)
		{
			System.out.println(sol);
		}

		
		//create output file according to store output.
		FileWriter outputfile = new FileWriter("greedy_solution"+fileno+".txt");
		//call function to select vertical and horizontal line
		outputfile.write(solution.size()+"\n");
		for(int i=0; i<solution.size();i++)
		{
			outputfile.write(solution.get(i)+"\n");
		}
		outputfile.close();
		
	}

}
