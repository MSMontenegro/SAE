package DomainModel.ReportsEntity;


/**
 * 
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 */
public class ResponsibilityAvailability {
	private double downtime;
	private double recoveryTime;
	private String responsibility;
	
	public double getDowntime() {
		return downtime;
	}
	public void setDowntime(double downtime) {
		this.downtime = downtime;
	}
	public double getRecoveryTime() {
		return recoveryTime;
	}
	public void setRecoveryTime(double recoveryTime) {
		this.recoveryTime = recoveryTime;
	}
	public String getResponsibility() {
		return responsibility;
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	
}
