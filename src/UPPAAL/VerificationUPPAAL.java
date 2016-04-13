package UPPAAL;

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

import UPPAAL.Read;


public class VerificationUPPAAL {

	public static int [][] map ;
	public static Scanner cin = new Scanner(System.in);
    public static Stack<Integer> s = new Stack<Integer>();
    public static double [] remain;
    public static int [] f;
    public static boolean EnergeOK = true;
    public static DecimalFormat df=new DecimalFormat("0.00");  
    
	public static void main(String[] args) throws Exception 
	{
		SAXReader reader=new SAXReader();//��ȡ������
	    Document dom= reader.read("UPPAAL.xml");//����XML��ȡ���������ĵ���dom����
	    Element root=dom.getRootElement();//��ȡ���ڵ�
	    
	    Read uppaal=new Read();
	    uppaal.load(root);
	    
	    ArrayList <UppaalTemPlate> templateList = new  ArrayList <UppaalTemPlate>();
	    templateList = uppaal.getUppaalTemplates();
	    
	    Iterator <UppaalTemPlate>  templateIterator = templateList.iterator();
	   
	    System.out.print("������ �����ԴEnerge��");
	    double maxEnerge = cin.nextDouble();
	    
	    System.out.print("������ �����ԴR1 R2��");
 	    double maxR1 = cin.nextDouble();
 	    double maxR2 = cin.nextDouble();
 	    
	    while(templateIterator.hasNext())//����template
	    {
	    	System.out.println("\n=====================================================");
	    	UppaalTemPlate templateI = templateIterator.next();
	    	map = new int [templateI.getLocations().size()][templateI.getLocations().size()];
	    	remain = new double [templateI.getLocations().size()];
	    	f = new int [templateI.getLocations().size()];
	    	ArrayList <UppaalTransition> transitionList = templateI.getTransitions();
	    	
	    	for(Object oTransition : transitionList)//����transition ������map �ڽӾ��� ��ȥ����·map[i][j]����i<j
	    	{
	    		UppaalTransition transitionI = (UppaalTransition)oTransition;
	    		if(transitionI.getSourceId() < transitionI.getTargetId())
	    		{	
	    			map[transitionI.getSourceId()][transitionI.getTargetId()] = transitionI.getTargetId();
		    		templateI.getLocations().get(transitionI.getTargetId()).setT1(transitionI.getT1());
		    		templateI.getLocations().get(transitionI.getTargetId()).setT2(transitionI.getT2());
	    		}
	    	}
	    	
	    	EnergeOK = true;
	    	
	    	s.clear();
	    	System.out.println(templateI.getName()+":");
	    	DFSForEnerge(0,maxEnerge,maxEnerge,s,templateI.getLocations());
	    	if(EnergeOK)
	    		System.out.println("**������Դ�������**\n");
	    	else
	    		System.out.println("**��������Դ�������**\n");
	    	
	    	for(int i=0;i<remain.length;i++)
	    		System.out.println("����״̬"+i+"��С�ܺ�Ϊ:"+df.format(maxEnerge-remain[i]));
	    	
	    	System.out.println("----------------------------------");
	    	//Ȼ���ҳ�R1 R2 �����	    	
	    	
	    	ArrayList <UppaalLocation> locationList = templateI.getLocations();
	    	boolean legal = true;
	    	int i =0;
	    	for(Object oLocation : locationList)//����transition ������map �ڽӾ��� ��ȥ����·map[i][j]����i<j
	    	{
	    		UppaalLocation locationI = (UppaalLocation) oLocation;
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
	private static void DFSForEnerge(int i,double t1Energe, double t2Energe, Stack<Integer> stack, ArrayList<UppaalLocation> locations)
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
