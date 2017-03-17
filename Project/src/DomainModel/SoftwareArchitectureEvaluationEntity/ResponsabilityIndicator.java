package DomainModel.SoftwareArchitectureEvaluationEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import DomainModel.SoftwareArchitectureEvaluationEntity.IndicatorType;
import DomainModel.SoftwareArchitectureSpecificationEntity.Responsibility;

/**
 * This class defines join path element
 *  
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 */
@Entity
@Table(name = "RESPONSABILIDADINDICATOR")
public class ResponsabilityIndicator extends IndicatorType {

	public ResponsabilityIndicator(String pname) {
		super(pname);
	}

	@ManyToOne(targetEntity = Responsibility.class, cascade = CascadeType.ALL)
	private Responsibility responsibility;

	public ResponsabilityIndicator() {
		super();
	}

	public Responsibility getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(Responsibility responsibility) {
		this.responsibility = responsibility;
	}

}
