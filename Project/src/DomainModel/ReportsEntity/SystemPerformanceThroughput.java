package DomainModel.ReportsEntity;

/**
 * 
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 */
public class SystemPerformanceThroughput {

	private double throughput;
	private double throughputE;
	private String system;
	private String run;
	
	public double getThroughputE() {
		return throughputE;
	}
	public void setThroughputE(double throughputE) {
		this.throughputE = throughputE;
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
