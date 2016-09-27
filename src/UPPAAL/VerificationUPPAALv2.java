package UPPAAL;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import util.*;
import bean.Automata;
import bean.Location;
import bean.Template;
import bean.Transition;


public class VerificationUPPAALv2 {

	public static int [][] map ;
	public static Scanner cin = new Scanner(System.in);
    public static Stack<Integer> s = new Stack<Integer>();
    public static double [] remain;
    public static int [] f;
    public static boolean EnergeOK = true;
    public static DecimalFormat df=new DecimalFormat("0.00");  
    private static Automata mAutomata;
	public static void main(String[] args) throws Exception 
	{
		XML2UppaalUtil xUtil = new XML2UppaalUtil(new File("UAVForXStream.xml"));
		mAutomata = XML2UppaalUtil.getAutomata();
		
	    
	    ArrayList<Template> templateList = new ArrayList<>();
	    templateList = mAutomata.getTemplateList();
	    
	    Iterator <Template>  templateIterator = templateList.iterator();
	   
	    System.out.print("������ �����ԴEnerge��");
	    double maxEnerge = cin.nextDouble();
	    
	    System.out.print("������ �����ԴR1 R2��");
 	    double maxR1 = cin.nextDouble();
 	    double maxR2 = cin.nextDouble();
 	    
 	    // ͨ��id����index
 	    HashMap<String, Integer> locationIndexById = indexOfLocations();
 	    
	    while(templateIterator.hasNext())	//����template
	    {
	    	System.out.println("\n=====================================================");
	    	Template templateI = templateIterator.next();
	    	map = new int [templateI.getLocationList().size()][templateI.getLocationList().size()];
	    	remain = new double [templateI.getLocationList().size()];
	    	f = new int [templateI.getLocationList().size()];
	    	ArrayList <Transition> transitionList = templateI.getTransitionList();
	    	
	    	for(Object oTransition : transitionList)//����transition ������map �ڽӾ��� ��ȥ����·map[i][j]����i<j
	    	{
	    		Transition transitionI = (Transition)oTransition;
	    		int sourceId = locationIndexById.get(transitionI.getSource());
    			int targetId = locationIndexById.get(transitionI.getTarget());
    			
	    		if(sourceId < targetId)
	    		{	
	    			ArrayList<Location> locations = templateI.getLocationList();
	    			Location targetLocation = locations.get(locationIndexById.get(transitionI.getTarget()));
	    			map[sourceId][targetId] = locationIndexById.get(transitionI.getTarget());
		    		
	    		}
	    	}
	    	
	    	EnergeOK = true;
	    	
	    	s.clear();
	    	System.out.println(templateI.getName()+":");
	    	DFSForEnerge(0,maxEnerge,maxEnerge,s,templateI.getLocationList());
	    	if(EnergeOK)
	    		System.out.println("**������Դ�������**\n");
	    	else
	    		System.out.println("**��������Դ�������**\n");
	    	
	    	for(int i=0;i<remain.length;i++)
	    		System.out.println("����״̬"+i+"��С�ܺ�Ϊ:"+df.format(maxEnerge-remain[i]));
	    	
	    	System.out.println("----------------------------------");
	    	//Ȼ���ҳ�R1 R2 �����	    	
	    	
	    	ArrayList <Location> locationList = templateI.getLocationList();
	    	boolean legal = true;
	    	int i =0;
	    	for(Object oLocation : locationList)//����transition ������map �ڽӾ��� ��ȥ����·map[i][j]����i<j
	    	{
	    		Location locationI = (Location) oLocation;
	    		if(locationI.getR1() > maxR1 || locationI.getR2() > maxR2 )
	    		{	
	    			legal = false;
	    			System.out.println("locationID = "+locationI.getId() + "������Դ���ƣ�");
	    			for(int j=0;j<map.length;j++)
	    				map[j][i] = 0;
	    		}
	    		i++;
	    	}
	    	
	    	if(!legal)
	    	{
	    		System.out.println("**�����������ԴԼ��**");
	    		System.out.print("������Ҫ���Ե���ʼ�����ֹ��id(��-1����)��");
	    		while(cin.hasNext())
	    		{
	    			
	    			int start = cin.nextInt();
	    			if(start == -1)
	    				break;
	    			int end = cin.nextInt();
	    			
	    			
	    			Stack<Integer> stack = new Stack<Integer>();
					if(!DFS(start,end,stack))
						System.out.println("û�и�·��");
					
					System.out.print("������Ҫ���Ե���ʼ�����ֹ��id(��-1����)��");
	    		}
	    	}
	    	else
	    	{
	    		System.out.println("**���������ԴԼ��**");
	    	}
	    }
	   
	    
	    
	    
	}
	private static HashMap<String, Integer> indexOfLocations() {
		HashMap<String, Integer> map = new HashMap<>();
		int index = 0;
		for(Template template : mAutomata.getTemplateList()) {
			ArrayList<Location> locations = template.getLocationList();
			for (Location location : locations) {
				map.put(location.getId(), index++);
			}
		}
		
		return map;
	}
	private static boolean DFS(int start, int end, Stack<Integer> stack) {
		stack.push(start);
		if(start == end)
		{	
			if(stack.size() == 1)
				return false;
			
			System.out.println(stack);
			return true;
		}
		for(int j=start+1;j<map.length;j++)
		{
			if(map[start][j]!=0 && DFS(j,end,stack))
			{						
					return true;
			}
		}
		stack.pop();
		return false;
	}
	private static void DFSForEnerge(int i,double t1Energe, double t2Energe, Stack<Integer> stack, ArrayList<Location> locations)
	{
		stack.push(i);
		t2Energe-=locations.get(i).getEnerge()*locations.get(i).getT2();
		t1Energe-=locations.get(i).getEnerge()*locations.get(i).getT1();
		if(t1Energe > remain[i] || f[i] == 0)//f��¼�Ƿ��ǳ�ʼ
		{
			remain[i] = t1Energe;
			f[i] =1;
		}
		
		if(t2Energe < 0 && t2Energe+locations.get(i).getEnerge()*locations.get(i).getT2()>=0)
		{	
			
			System.out.print("�����ܺ�:"+df.format(t2Energe*(-1)).toString() +"--·����");
			System.out.println(stack);
			
			EnergeOK = false;
							
		}
			
				
		
		
		for(int j = i+1; j<map.length;j++)
		{
			if(map[i][j]!=0)
			{				
				DFSForEnerge(j,t1Energe,t2Energe,stack,locations);				
			}
		}
		stack.pop();	
		return ;
	}
	
}