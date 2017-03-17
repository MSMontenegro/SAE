package DomainModel.ReportsEntity;

/**
 * 
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 */
public class SystemReliability {

	private double fails;
	private double failsE;
	private String system;
	private String run;
	
	public double getFails() {
		return fails;
	}
	public void setFails(double fails) {
		this.fails = fails;
	}
	public double getFailsE() {
		return failsE;
	}
	public void setFailsE(double failsE) {
		this.failsE = failsE;
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
