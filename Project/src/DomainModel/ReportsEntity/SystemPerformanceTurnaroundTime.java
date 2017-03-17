package DomainModel.ReportsEntity;

/**
 * 
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 */
public class SystemPerformanceTurnaroundTime {

	private double turnaroundTime;
	private double turnaroundTimeE;
	private String system;
	private String run;
	
	public double getTurnaroundTime() {
		return turnaroundTime;
	}
	public void setTurnaroundTime(double turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}
	public double getTurnaroundTimeE() {
		return turnaroundTimeE;
	}
	public void setTurnaroundTimeE(double turnaroundTimeE) {
		this.turnaroundTimeE = turnaroundTimeE;
	}
	public String getRun() {
		return run;
	}
	public void setRun(String run) {
		this.run = run;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}	
	
}
