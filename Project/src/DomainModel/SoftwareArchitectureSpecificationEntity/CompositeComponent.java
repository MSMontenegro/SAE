package DomainModel.SoftwareArchitectureSpecificationEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * This class defines the architecture element: composite component
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 06/09/2016
 */

@Entity
@Table(name = "COMPOSITECOMPONENT")
@PrimaryKeyJoinColumn(name = "id")
public class CompositeComponent extends ArchitectureElement {
	
	@OneToMany(targetEntity = ArchitectureElement.class, cascade = CascadeType.ALL)
	private Set<ArchitectureElement> internalComponent = new HashSet<ArchitectureElement>();

	public CompositeComponent(String name) {
		super(name);
	}

	public CompositeComponent() {
		super();
	}
}

