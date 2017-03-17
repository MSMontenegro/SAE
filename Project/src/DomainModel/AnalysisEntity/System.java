package DomainModel.AnalysisEntity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import DomainModel.SoftwareArchitectureSpecificationEntity.Architecture;


/**
 * This class defines the system of a project
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/11/2015
 */

@Entity
@Table(name = "SYSTEM")
public class System implements Comparable {

	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String systemName;

	private String projectName;

	@Temporal(javax.persistence.TemporalType.DATE)
	private Date projectFinishDate;

	@Temporal(javax.persistence.TemporalType.DATE)
	private Date projectStartDate;

	@OneToMany(targetEntity = QualityRequirement.class, cascade = CascadeType.ALL)
	private Set<QualityRequirement> qualityRequirements = new HashSet<QualityRequirement>();

	// VER QUE PUEDE SER CERO
	@OneToMany(targetEntity = Architecture.class, cascade = CascadeType.ALL)
	private Set<Architecture> architectures = new HashSet<Architecture>();

	private boolean state;

	// Builders
	public System() {

	}

	public System(String psystemName, String pprojectName, Date pprojectStartDate, Date pprojectFinishDate,
			boolean pstate) {
		super();
		this.projectFinishDate = pprojectFinishDate;
		this.projectStartDate = pprojectStartDate;
		this.projectName = pprojectName;
		this.systemName = psystemName;
		this.state = pstate;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int pid) {
		this.id = pid;
	}

	public Date getProjectFinishDate() {
		return projectFinishDate;
	}

	public void setProjectFinishDate(Date pprojectFinishDate) {
		this.projectFinishDate = pprojectFinishDate;
	}

	public Date getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(Date pprojectStartDate) {
		this.projectStartDate = pprojectStartDate;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String pprojectName) {
		this.projectName = pprojectName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String psystemName) {
		this.systemName = psystemName;
	}

	public Set<QualityRequirement> getQualityRequirements() {
		return qualityRequirements;
	}

	public void setQualityRequirements(Set<QualityRequirement> pqualityRequirements) {
		this.qualityRequirements = pqualityRequirements;
	}

	public Set<Architecture> getArchitectures() {
		return architectures;
	}

	public void setArchitectures(Set<Architecture> architectures) {
		this.architectures = architectures;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean pstate) {
		this.state = pstate;
	}

	@Override
	public String toString() {
		return this.getSystemName();
	}

	// compareTo
	public int compareTo(Object p) {
		System t = (System) p;
		return this.toString().compareTo(t.toString());
	}

	/**
	 * Method removes a system logically, by setting their status to false
	 * 
	 * @param doesn't apply
	 * @return void
	 */
	public void remove() {
		this.setState(false);
	}

}
