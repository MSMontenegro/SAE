package DomainModel.SoftwareArchitectureSpecificationEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import DomainModel.SoftwareArchitectureEvaluationEntity.Simulator;

/**
 * This class defines an architecture view for a specific system
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/09/2016
 */

@Entity
@Table(name = "ARCHITECTURE")
public class Architecture {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	protected int id;

	@OneToMany(targetEntity = Path.class)
	private Set<Path> paths = new HashSet<Path>();

	@OneToMany(targetEntity = ArchitectureElement.class, cascade = CascadeType.REMOVE)
	private Set<ArchitectureElement> architectureElements = new HashSet<ArchitectureElement>();

	@OneToOne(targetEntity = Simulator.class, cascade = CascadeType.REMOVE)
	private Simulator simulator;

	private String pathUCM;

	public Architecture() {

	}

	public Architecture(String ppathUCM) {
		this.pathUCM = ppathUCM;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Path> getPaths() {
		return paths;
	}

	public void setPaths(Set<Path> paths) {
		this.paths = paths;
	}

	public Set<ArchitectureElement> getArchitectureElements() {
		return architectureElements;
	}

	public void setArchitectureElements(Set<ArchitectureElement> architectureElements) {
		this.architectureElements = architectureElements;
	}

	public Simulator getSimulator() {
		return simulator;
	}

	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}

	public String getPathUCM() {
		return pathUCM;
	}

	public void setPathUCM(String pathUCM) {
		this.pathUCM = pathUCM;
	}

}
