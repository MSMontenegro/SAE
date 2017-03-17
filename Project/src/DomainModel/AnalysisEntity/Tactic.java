package DomainModel.AnalysisEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class defines a tactic
 * 
 * @author: Micaela Montenegro. mail: mica.montenegro.sistemas@gmail.com
 * @version: 19/09/2016
 */

@Entity
@Table(name = "TACTIC")
public class Tactic implements Comparable{
	// Attributes
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

	private String name;

	@ManyToOne(targetEntity = TacticType.class)
	private TacticType type;

	public Tactic(String pname, TacticType ptype) {
		this.name = pname;
		this.type = ptype;
	}

	public Tactic() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TacticType getType() {
		return type;
	}

	public void setType(TacticType type) {
		this.type = type;
	}
	
	// ComparaTo
	public int compareTo(Object p) {
		Tactic t = (Tactic) p;
		return this.toString().compareTo(t.toString());
	}
}
