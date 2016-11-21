package bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("location")
public class Location extends Entity{
	@XStreamAlias("id")
	private String id;
	@XStreamAlias("name")
	private String name;
	@XStreamAlias("timeDuration")
	private String timeDuration;
	@XStreamAlias("init")
	private boolean init;
	@XStreamAlias("final")
	private boolean finl;
	
	
	@XStreamAlias("Energe")
	private String Energe;
	@XStreamAlias("R1")
	private String R1;
	@XStreamAlias("R2")
	private String R2;
	@XStreamAlias("T1")
	private String T1;
	@XStreamAlias("T2")
	private String T2;
	
	public double getT1() {
		if (T1 == null) {
			return 0;
		}
		return Double.valueOf(T1);
	}
	public void setT1(String t1) {
		T1 = t1;
	}
	public double getT2() {
		if (T2 == null) {
			return 0;
		}
		return Double.valueOf(T2);
	}
	

	public double getEnerge() {
		if (Energe == null) {
			return 0;
		}
		return Double.valueOf(Energe);
	}
	public double getR1() {
		if (R1 == null) {
			return 0;
		}
		return Double.valueOf(R1);
	}
	public double getR2() {
		if (R2 == null) {
			return 0;
		}
		return Double.valueOf(R2);
	}
	
	
	public void setEnerge(String energe) {
		Energe = energe;
	}
	public void setR1(String r1) {
		R1 = r1;
	}
	public void setR2(String r2) {
		R2 = r2;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTimeDuration() {
		return timeDuration;
	}
	public void setTimeDuration(String timeDuration) {
		this.timeDuration = timeDuration;
	}
	public boolean isInit() {
		return init;
	}
	public void setInit(boolean init) {
		this.init = init;
	}
	public boolean isFinl() {
		return finl;
	}
	public void setFinl(boolean finl) {
		this.finl = finl;
	}
	
}
