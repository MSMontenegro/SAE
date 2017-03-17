package DomainModel.SoftwareArchitectureSpecificationEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This class defines the path element: Responsibility
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/09/2016
 */

@Entity
@Table(name = "RESPONSABILITY")
public class Responsibility extends PathElement {
	
	private String idUCM;
	
	@OneToMany(targetEntity = SpecificationParameter.class)
	private Set<SpecificationParameter> specificationParameter = new HashSet<SpecificationParameter>();
	
	public Responsibility(String name) {
		super(name);
	}
	
	public Responsibility() {
	}

	public Set<SpecificationParameter> getSpecificationParameter() {
		return specificationParameter;
	}

	public void setSpecificationParameter(Set<SpecificationParameter> specificationParameter) {
		this.specificationParameter = specificationParameter;
	}

	public String getIdUCM() {
		return idUCM;
	}

	public void setIdUCM(String idUCM) {
		this.idUCM = idUCM;
	}
	
}
