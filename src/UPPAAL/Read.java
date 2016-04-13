package UPPAAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.dom4j.Element;
public class Read {
		
	ArrayList<UppaalTemPlate> uppaalTemplates = new ArrayList<UppaalTemPlate>();
	ArrayList<UppaalLocation> locations = new ArrayList<UppaalLocation>();
	ArrayList<UppaalTransition> transitions = new ArrayList<UppaalTransition>();
			
	public ArrayList<UppaalTemPlate> getUppaalTemplates() {
		return uppaalTemplates;
	}
	
	public void load(Element root) throws Exception{
		
		ArrayList<Element> templateList = new ArrayList<Element>();
		
		
		templateList.addAll(root.elements("template"));
		
		
		for(Object oTemplate: templateList)
		{
			Element templateI = (Element)oTemplate;
			
			UppaalTemPlate template = new UppaalTemPlate();
			template.setName(templateI.element("name").getText());//设置template的名字
			
			locations = new ArrayList<UppaalLocation>();
			transitions = new ArrayList<UppaalTransition>();
			
			ArrayList<Element> locationList = new ArrayList<Element>();			
			locationList.addAll(templateI.elements("location"));//读取该template的locations		
			
			boolean first = true;
			for(Object oLocation: locationList)
			{
				Element locationI = (Element)oLocation;	
				
				UppaalLocation location = new UppaalLocation();
				
				location.setId(Integer.parseInt(locationI.attributeValue("id").substring(2)));
				location.setName(locationI.element("name").getText());
				
				if(first)
				{	
					first = false;
					location.setInit(true);	
					locations.add(location);
					continue;
				}
				location.setR1(Double.parseDouble(locationI.attributeValue("R1")));
				location.setR2(Double.parseDouble(locationI.attributeValue("R2")));
				location.setEnerge(Double.parseDouble(locationI.attributeValue("Energe")));
				
				locations.add(location);
			}
			template.setLocations(locations);
			
			ArrayList<Element> transitionList = new ArrayList<Element>();			
			transitionList.addAll(templateI.elements("transition"));
			for(Object oTransition: transitionList)
			{
				Element transitionI = (Element)oTransition;	
				
				UppaalTransition transition = new UppaalTransition();
				//System.out.println(transitionI.element("label").getText());
				transition.setNameText(transitionI.element("label").getText());
				transition.setT1(Double.parseDouble(transitionI.attributeValue("T1")));
				transition.setT2(Double.parseDouble(transitionI.attributeValue("T2")));				
				transition.setSourceId(Integer.parseInt(transitionI.element("source").attributeValue("ref").substring(2)));
				transition.setTargetId(Integer.parseInt(transitionI.element("target").attributeValue("ref").substring(2)));
					
				transitions.add(transition);
			}
			
			template.setTransitions(transitions);
			
			
			uppaalTemplates.add(template);
		}
		
	}
}
