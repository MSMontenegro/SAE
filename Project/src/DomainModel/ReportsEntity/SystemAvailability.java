package DomainModel.ReportsEntity;

/**
 * 
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 */
public class SystemAvailability {

	private double availability;
	private double noAvailability;
	private double availabilityE;
	private double noAvailabilityE;
	private String system;
	private String run;
	
	public double getAvailability() {
		return availability;
	}
	public void setAvailability(double availability) {
		this.availability = availability;
	}
	public double getNoAvailability() {
		return noAvailability;
	}
	public void setNoAvailability(double noAvailability) {
		this.noAvailability = noAvailability;
	}
	public double getAvailabilityE() {
		return availabilityE;
	}
	public void setAvailabilityE(double availabilityE) {
		this.availabilityE = availabilityE;
	}
	public double getNoAvailabilityE() {
		return noAvailabilityE;
	}
	public void setNoAvailabilityE(double noAvailabilityE) {
		this.noAvailabilityE = noAvailabilityE;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getRun() {
		return run;
	}
	public void setRun(String run) {
		this.run = run;
	}
	
}
