package DomainModel.SoftwareArchitectureSpecificationEntity;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * This class defines the path element: ORFork
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/09/2016
 */

@Entity
@Table(name = "ORFORK")
@PrimaryKeyJoinColumn(name = "id")
public class ORFork extends Fork {

	ArrayList<Double> pathProbabilities = new ArrayList<Double>();
			
	public ORFork(String name) {
		super(name);
	}

	public ArrayList<Double> getPathProbabilities() {
		return pathProbabilities;
	}

	public void setPathProbabilities(ArrayList<Double> pathProbabilities) {
		this.pathProbabilities = pathProbabilities;
	}
	
	public ORFork() {
	}
	
}
