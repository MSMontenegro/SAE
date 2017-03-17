package DomainModel.ReportsEntity;

/**
 * 
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 */
public class SystemPerformance {

	private double throughput;
	private double throughputE;
	private double turnaroundTime;
	private double turnaroundTimeE;
	private String system;
	private String run;
	
	public double getThroughputE() {
		return throughputE;
	}
	public void setThroughputE(double throughputE) {
		this.throughputE = throughputE;
	}
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
	public double getThroughput() {
		return throughput;
	}
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}	
	
}
