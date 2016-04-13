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
		SAXReader reader=new SAXReader();//获取解析器
	    Document dom= reader.read("UPPAAL.xml");//解析XML获取代表整个文档的dom对象
	    Element root=dom.getRootElement();//获取根节点
	    
	    Read uppaal=new Read();
	    uppaal.load(root);
	    
	    ArrayList <UppaalTemPlate> templateList = new  ArrayList <UppaalTemPlate>();
	    templateList = uppaal.getUppaalTemplates();
	    
	    Iterator <UppaalTemPlate>  templateIterator = templateList.iterator();
	   
	    System.out.print("请输入 最大能源Energe：");
	    double maxEnerge = cin.nextDouble();
	    
	    System.out.print("请输入 最大资源R1 R2：");
 	    double maxR1 = cin.nextDouble();
 	    double maxR2 = cin.nextDouble();
 	    
	    while(templateIterator.hasNext())//遍历template
	    {
	    	System.out.println("\n=====================================================");
	    	UppaalTemPlate templateI = templateIterator.next();
	    	map = new int [templateI.getLocations().size()][templateI.getLocations().size()];
	    	remain = new double [templateI.getLocations().size()];
	    	f = new int [templateI.getLocations().size()];
	    	ArrayList <UppaalTransition> transitionList = templateI.getTransitions();
	    	
	    	for(Object oTransition : transitionList)//遍历transition 来构建map 邻接矩阵 并去掉回路map[i][j]满足i<j
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
	    		System.out.println("**满足能源损耗条件**\n");
	    	else
	    		System.out.println("**不满足能源损耗条件**\n");
	    	
	    	for(int i=0;i<remain.length;i++)
	    		System.out.println("到达状态"+i+"最小能耗为:"+df.format(maxEnerge-remain[i]));
	    	
	    	System.out.println("----------------------------------");
	    	//然后找出R1 R2 超标的	    	
	    	
	    	ArrayList <UppaalLocation> locationList = templateI.getLocations();
	    	boolean legal = true;
	    	int i =0;
	    	for(Object oLocation : locationList)//遍历transition 来构建map 邻接矩阵 并去掉回路map[i][j]满足i<j
	    	{
	    		UppaalLocation locationI = (UppaalLocation) oLocation;
	    		if(locationI.getR1() > maxR1 || locationI.getR2() > maxR2 )
	    		{	
	    			legal = false;
	    			System.out.println("locationID = "+locationI.getId() + "超出资源限制！");
	    			for(int j=0;j<map.length;j++)
	    				map[j][i] = 0;
	    		}
	    		i++;
	    	}
	    	
	    	if(!legal)
	    	{
	    		System.out.println("**不满足给定资源约束**");
	    		System.out.print("请输入要测试的起始点和终止点id(以-1结束)：");
	    		while(cin.hasNext())
	    		{
	    			
	    			int start = cin.nextInt();
	    			if(start == -1)
	    				break;
	    			int end = cin.nextInt();
	    			
	    			
	    			Stack<Integer> stack = new Stack<Integer>();
					if(!DFS(start,end,stack))
						System.out.println("没有该路径");
					
					System.out.print("请输入要测试的起始点和终止点id(以-1结束)：");
	    		}
	    	}
	    	else
	    	{
	    		System.out.println("**满足给定资源约束**");
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
		if(t1Energe > remain[i] || f[i] == 0)//f记录是否是初始
		{
			remain[i] = t1Energe;
			f[i] =1;
		}
		
		if(t2Energe < 0 && t2Energe+locations.get(i).getEnerge()*locations.get(i).getT2()>=0)
		{	
			
			System.out.print("超出能耗:"+df.format(t2Energe*(-1)).toString() +"--路径：");
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
