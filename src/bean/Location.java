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
	
	@XStreamAlias("T1")
	private Double T1;
	@XStreamAlias("T2")
	private Double T2;
	@XStreamAlias("Energe")
	private Double Energe;
	@XStreamAlias("R1")
	private Double R1;
	@XStreamAlias("R2")
	private Double R2;
	
	public Double getT1() {
		if (T1 == null) {
			return (double) 0;
		}
		return T1;
	}
	public void setT1(Double t1) {
		T1 = t1;
	}
	public Double getT2() {
		if (T2 == null) {
			return (double) 0;
		}
		return T2;
	}
	public void setT2(Double t2) {
		T2 = t2;
	}
	public Double getEnerge() {
		if (Energe == null) {
			return (double) 0;
		}
		return Energe;
	}
	public void setEnerge(Double energe) {
		Energe = energe;
	}
	public Double getR1() {
		if (R1 == null) {
			return (double) 0;
		}
		return R1;
	}
	public void setR1(Double r1) {
		R1 = r1;
	}
	public Double getR2() {
		if (R2 == null) {
			return (double) 0;
		}
		return R2;
	}
	public void setR2(Double r2) {
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
