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

import com.jun.util.Display;

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
		XML2UppaalUtil xUtil = new XML2UppaalUtil(new File("EnergeForXStream.xml"));
		mAutomata = XML2UppaalUtil.getAutomata();
		
	    
	    ArrayList<Template> templateList = new ArrayList<>();
	    templateList = mAutomata.getTemplateList();
	    
	    Iterator <Template>  templateIterator = templateList.iterator();
	   
	    Display.println("请输入 最大能源Energe：");
	    double maxEnerge = cin.nextDouble();
	    
	    Display.println("请输入 最大资源R1 R2：");
 	    double maxR1 = cin.nextDouble();
 	    double maxR2 = cin.nextDouble();
 	    
 	    // 通过id查找index
 	    HashMap<String, Integer> locationIndexById = indexOfLocations();
 	    
	    while(templateIterator.hasNext())	//遍历template
	    {
	    	Template templateI = templateIterator.next();
	    	map = new int [templateI.getLocationList().size()][templateI.getLocationList().size()];
	    	remain = new double [templateI.getLocationList().size()];
	    	f = new int [templateI.getLocationList().size()];
	    	ArrayList <Transition> transitionList = templateI.getTransitionList();
	    	
	    	for(Object oTransition : transitionList)//遍历transition 来构建map 邻接矩阵 并去掉回路map[i][j]满足i<j
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
	    	Display.println("================================正在验证顺序图================================");
	    	Display.println("===> 顺序图名为：" + templateI.getName()+ "\n");
	    	
	    	Display.println("-------------------------能耗验证-------------------------");
	    	DFSForEnerge(0,maxEnerge,maxEnerge,s,templateI.getLocationList());
	    	if(EnergeOK)
	    		Display.println("满足能源损耗条件\n");
	    	else
	    		Display.println("不满足能源损耗条件\n");
	    	
	    	for(int i=0;i<remain.length;i++)
	    		Display.println("到达状态"+i+"最小能耗为:"+df.format(maxEnerge-remain[i])+"\n");
	    	
	    	//然后找出R1 R2 超标的	    	
	    	Display.println("-------------------------资源验证-------------------------");
	    	
	    	ArrayList <Location> locationList = templateI.getLocationList();
	    	boolean legal = true;
	    	int i =0;
	    	for(Object oLocation : locationList)//遍历transition 来构建map 邻接矩阵 并去掉回路map[i][j]满足i<j
	    	{
	    		Location locationI = (Location) oLocation;
	    		if(locationI.getR1() > maxR1 || locationI.getR2() > maxR2 )
	    		{	
	    			legal = false;
	    			Display.println("locationID = "+locationI.getId() + "超出资源限制！");
	    			for(int j=0;j<map.length;j++)
	    				map[j][i] = 0;
	    		} else {
	    			Display.println("locaion:" + locationI.getName() + "满足给定资源验证条件\n");
	    		}
	    		i++;
	    	}
	    	
	    	if(!legal)
	    	{
	    		Display.println("不满足给定资源约束\n");
	    		Display.println("请输入要测试的起始点和终止点id(以-1结束)：");
	    		while(cin.hasNext())
	    		{
	    			
	    			int start = cin.nextInt();
	    			if(start == -1)
	    				break;
	    			int end = cin.nextInt();
	    			
	    			
	    			Stack<Integer> stack = new Stack<Integer>();
					if(!DFS(start,end,stack))
						Display.println("没有该路径");
					
					Display.println("请输入要测试的起始点和终止点id(以-1结束)：");
	    		}
	    	}
	    	else
	    	{
	    		Display.println("满足给定资源约束\n");
	    	}
	    }
	   
	    
	    Display.println("================================顺序图验证结束================================");
	    
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
			
			Display.println(stack);
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
		if(t1Energe > remain[i] || f[i] == 0)//f记录是否是初始
		{
			remain[i] = t1Energe;
			f[i] =1;
		}
		
		if(t2Energe < 0 && t2Energe+locations.get(i).getEnerge()*locations.get(i).getT2()>=0)
		{	
			
			Display.println("超出能耗:"+df.format(t2Energe*(-1)).toString() );
			Display.println("路径：");
			Display.println(stack);
			Display.println("\n");
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
