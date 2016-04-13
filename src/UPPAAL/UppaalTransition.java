package UPPAAL;

public class UppaalTransition 
{
	int sourceId;
	int targetId;
	
	String nameText;
	
	double T1;
	double T2;
	
	
	public double getT1() {
		return T1;
	}
	public void setT1(double t1) {
		T1 = t1;
	}
	public double getT2() {
		return T2;
	}
	public void setT2(double t2) {
		T2 = t2;
	}
	public int getSourceId() 
	{
		return sourceId;
	}
	public void setSourceId(int sourceId) 
	{
		this.sourceId = sourceId;
	}
	public int getTargetId() 
	{
		return targetId;
	}
	public void setTargetId(int targetId) 
	{
		this.targetId = targetId;
	}
	
	public String getNameText() 
	{
		return nameText;
	}
	public void setNameText(String nameText) 
	{
		this.nameText = nameText;
	}
	
}