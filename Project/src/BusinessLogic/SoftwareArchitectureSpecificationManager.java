package BusinessLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import DataManager.HibernateManager;
import DomainModel.AnalysisEntity.Metric;
import DomainModel.AnalysisEntity.Unit;
import DomainModel.SoftwareArchitectureSpecificationEntity.ANDFork;
import DomainModel.SoftwareArchitectureSpecificationEntity.ANDJoin;
import DomainModel.SoftwareArchitectureSpecificationEntity.Architecture;
import DomainModel.SoftwareArchitectureSpecificationEntity.ArchitectureElement;
import DomainModel.SoftwareArchitectureSpecificationEntity.CompositeComponent;
import DomainModel.SoftwareArchitectureSpecificationEntity.EndPoint;
import DomainModel.SoftwareArchitectureSpecificationEntity.ORFork;
import DomainModel.SoftwareArchitectureSpecificationEntity.ORJoin;
import DomainModel.SoftwareArchitectureSpecificationEntity.Path;
import DomainModel.SoftwareArchitectureSpecificationEntity.PathElement;
import DomainModel.SoftwareArchitectureSpecificationEntity.Responsibility;
import DomainModel.SoftwareArchitectureSpecificationEntity.SimpleComponent;
import DomainModel.SoftwareArchitectureSpecificationEntity.SpecificationParameter;
import DomainModel.SoftwareArchitectureSpecificationEntity.StartPoint;
import Main.TransformerSimulator;

/**
 * This class is responsible for the management package: Software Architecture
 * Specification
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @version: 23/08/2016
 */
public class SoftwareArchitectureSpecificationManager extends HibernateManager implements TreeSelectionListener {

	/**
	 * Attributes
	 */
	private DomainModel.AnalysisEntity.System system;
	private Unit unit;
	Document doc;
	private DefaultTreeModel model;
	private JTree tree;
	private static SoftwareArchitectureSpecificationManager manager;
	private TransformerSimulator pluginTS;
	private Architecture architecture;

	private SoftwareArchitectureSpecificationManager() {
		super();
	}

	/**
	 * Get SystemConfigurationManager. Applied Singleton pattern
	 */
	public static SoftwareArchitectureSpecificationManager getManager() {
		if (manager == null) {
			synchronized (SoftwareArchitectureSpecificationManager.class) {
				manager = new SoftwareArchitectureSpecificationManager();
			}
		}
		return manager;
	}

	public static void setManager(SoftwareArchitectureSpecificationManager manager) {
		SoftwareArchitectureSpecificationManager.manager = manager;
	}

	/**
	 * Getters and Setters
	 */
	public void setSystem(DomainModel.AnalysisEntity.System psystem) {
		this.system = psystem;
	}

	public DomainModel.AnalysisEntity.System getSystem() {
		return this.system;
	}
	
	

	public Architecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}


	public TransformerSimulator getPluginTS() {
		if (pluginTS == null) {
			pluginTS = new TransformerSimulator();
		}
		return pluginTS;
	}

	public void setPluginTS(TransformerSimulator pluginTS) {
		this.pluginTS = pluginTS;
	}
	
	/**
	 * 
	 * @return True if there are systems whose state==true, else return false
	 * 
	 */
	public boolean existSystemTrue() {
		if (listSystem().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * get combos
	 */

	/**
	 * 
	 * @return ComboBoxModel with system names whose state==true
	 */
	public DomainModel.AnalysisEntity.System[] getComboModelSystem() { // NOPMD
																		// by
																		// Usuario-Pc
																		// on
																		// 10/06/16
																		// 21:42
		ArrayList<DomainModel.AnalysisEntity.System> systems = new ArrayList<DomainModel.AnalysisEntity.System>();
		for (DomainModel.AnalysisEntity.System auxTipo : this.listSystem()) {
			systems.add(auxTipo);
		}
		DomainModel.AnalysisEntity.System[] arraySystem = new DomainModel.AnalysisEntity.System[systems.size()];
		systems.toArray(arraySystem);
		return arraySystem;
	}

	/**
	 * 
	 * @return ComboBoxModel with unit names
	 */
	public Unit[] getComboModelUnit() {
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (Unit auxTipo : this.listUnit()) {
			if (auxTipo.getName().equals("Request/Hour") || auxTipo.getName().equals("Request/Day")
					|| auxTipo.getName().equals("Request/Week") || auxTipo.getName().equals("Request/Month")) {
			} else {
				units.add(auxTipo);
			}
		}
		Unit[] arrayUnit = new Unit[units.size()];
		units.toArray(arrayUnit);
		return arrayUnit;
	}

	/**
	 * lists
	 */

	/**
	 * 
	 * @return List<System> with the system names whose state==true
	 */
	private List<DomainModel.AnalysisEntity.System> listSystem() {
		return this.listClass(DomainModel.AnalysisEntity.System.class, "systemName", true);
	}

	/**
	 * 
	 * @return List<Unit> with the units names
	 */
	private List<Unit> listUnit() {
		return this.listClass(Unit.class, "name");
	}

	/**
	 * 
	 * @param pmetric
	 * @return
	 */
	private List listMetric(String pmetric) {
		Criteria crit = getSession().createCriteria(Metric.class).add(Restrictions.eq("name", pmetric));
		return crit.list();
	}

	/**
	 * db operation
	 */

	public Boolean updateSystem() {
		return this.updateObject(this.getSystem());
	}

	public Set<Architecture> getArchitectures() {
		return this.getSystem().getArchitectures();
	}

	/**
	 * db method to create architecture
	 */
	public void createArchitecture(Architecture parch) {
		try {

			StartPoint startPoint = null;
			Set<ArchitectureElement> archElements = new HashSet<ArchitectureElement>();
			Set<PathElement> pathElements = new HashSet<PathElement>();
			Set<Responsibility> responsibilities = new HashSet<Responsibility>();
			CompositeComponent child;

			// read xml file
			File fXmlFile = new File(parch.getPathUCM());

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			Boolean isRootNodeASimpleComponent = true;

			// Search item with matching tag
			NodeList contRefsList = doc.getElementsByTagName("contRefs");
			ArchitectureElement rootNode = null;

			DefaultMutableTreeNode parentN = null;
			// Search parent of tree (root)
			for (int i = 0; i < contRefsList.getLength(); i++) {
				if (contRefsList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eContRef = (Element) contRefsList.item(i);
					String parent = eContRef.getAttribute("parent");
					if (parent.equals("")) {
						String id = eContRef.getAttribute("id");
						if (isASimpleComponent(eContRef)) {
							rootNode = new SimpleComponent(this.translateNameComponent(id));
							parentN = new DefaultMutableTreeNode(rootNode);
						} else {
							rootNode = new CompositeComponent(this.translateNameComponent(id));
							parentN = new DefaultMutableTreeNode(rootNode);
							isRootNodeASimpleComponent = false;
						}
					}
				}
			}
			archElements.add(rootNode);
			// define model where nodes will be added
			model = new DefaultTreeModel(parentN);

			// add model to tree
			tree = new JTree(model);

			// define event
			tree.getSelectionModel().addTreeSelectionListener(this);

			// each parent creato to children, recursively
			creationComponent(parentN, isRootNodeASimpleComponent, archElements, pathElements);

			creationStartPoint(startPoint, pathElements);
			for (PathElement dp : pathElements) {
				saveObject(dp);
			}
			Set<Path> paths = new HashSet<Path>();
			Path path = new Path();
			path.setPathElements(pathElements);
			saveObject(path);
			paths.add(path);
			parch.setPaths(paths);
			for (ArchitectureElement dp : archElements) {
				saveObject(dp);
			}
			parch.setArchitectureElements(archElements);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Return name of component with id (value)
	 * 
	 * @param id
	 * @return
	 */
	private String translateNameComponent(String id) {
		NodeList componentsList = doc.getElementsByTagName("components");

		for (int i = 0; i < componentsList.getLength(); i++) {

			Node component = componentsList.item(i);

			if (component.getNodeType() == Node.ELEMENT_NODE) {
				Element eComponent = (Element) component;
				if (eComponent.getAttribute("contRefs").equals(id)) {
					return eComponent.getAttribute("name");
				}
			}
		}
		return "";
	}

	/**
	 * Return name of responsibility with id (value)
	 * 
	 * @param value
	 * @return
	 */
	private String translateNameSimpleElement(String value) {
		NodeList responsibilitiesNode = doc.getElementsByTagName("responsibilities");

		for (int i = 0; i < responsibilitiesNode.getLength(); i++) {

			Node responsibility = responsibilitiesNode.item(i);

			if (responsibility.getNodeType() == Node.ELEMENT_NODE) {
				Element eResponsibility = (Element) responsibility;
				if (eResponsibility.getAttribute("respRefs").equals(value)) {
					String asdfdsa = eResponsibility.getAttribute("name");
					return eResponsibility.getAttribute("name");
				}
			}
		}

		NodeList nodesList = doc.getElementsByTagName("nodes");
		for (int i = 0; i < nodesList.getLength(); i++) {

			Node nNode = nodesList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String typeNode = eElement.getAttribute("xsi:type");
				if (eElement.getAttribute("id").equals(value) && !typeNode.equals("ucm.map:EndPoint")
						&& !typeNode.equals("ucm.map:StartPoint")) {
					String asdf = eElement.getAttribute("name");
					return eElement.getAttribute("name");
				}
			}
		}

		return null;
	}

	/**
	 * Create components
	 * 
	 * @param parentN
	 * @param pisRootNodeASimpleComponent
	 * @param archElements
	 * @param pathElements
	 */
	private void creationComponent(DefaultMutableTreeNode parentN, boolean pisRootNodeASimpleComponent,
			Set<ArchitectureElement> archElements, Set<PathElement> pathElements) {
		NodeList contRefsList = doc.getElementsByTagName("contRefs");
		int index = 0;
		ArchitectureElement dataParent;
		Boolean isRootNodeASimpleComponent = true;

		if (pisRootNodeASimpleComponent) {
			dataParent = (SimpleComponent) parentN.getUserObject();
		} else {
			dataParent = (CompositeComponent) parentN.getUserObject();
		}
		for (int j = 0; j < contRefsList.getLength(); j++) {
			Node contRef = contRefsList.item(j);
			if (contRef.getNodeType() == Node.ELEMENT_NODE) {
				Element eContRef = (Element) contRef;
				// search root of subtree
				if (this.translateNameComponent(eContRef.getAttribute("id")).equals(dataParent.getName())) {
					String children = eContRef.getAttribute("children");
					if (!children.equals("")) {
						// create array with children
						String[] childArray = children.split(" ");
						for (int i = 0; i < childArray.length; i++) {

							Element eChild = getNodeOfComponent(childArray[i]);

							String id = eChild.getAttribute("id");
							ArchitectureElement child;
							if (isASimpleComponent(eChild)) {
								child = new SimpleComponent(this.translateNameComponent(id));
							} else {
								child = new CompositeComponent(this.translateNameComponent(id));
								isRootNodeASimpleComponent = false;
							}
							archElements.add(child);
							DefaultMutableTreeNode hijoN = new DefaultMutableTreeNode(child);
							model.insertNodeInto(hijoN, parentN, index);
							index++;
							// call recursively, if have children to create them
							creationComponent(hijoN, isRootNodeASimpleComponent, archElements, pathElements);
						}
					}

					// search his responsibilities
					String nodes = eContRef.getAttribute("nodes");
					if (!nodes.equals("")) {
						String[] node = nodes.split(" ");
						for (int k = 0; k < node.length; k++) {
							// Each resopnsibility
							this.creationSimpleElement(node[k], parentN, index, pathElements);
						}
					}
				}
			}
		}
	}

	/**
	 * Create responsibilities
	 * 
	 * @param idNode
	 * @param parentNode
	 * @param index
	 * @param pathElements
	 * @return
	 */
	private int creationSimpleElement(String idNode, DefaultMutableTreeNode parentNode, int index,
			Set<PathElement> pathElements) {
		NodeList responsibilitiesList = doc.getElementsByTagName("responsibilities");
		NodeList nodesList = doc.getElementsByTagName("nodes");

		Element eNode = this.getNodeOfResponsability(idNode, doc);

		for (int i = 0; i < responsibilitiesList.getLength(); i++) {
			Node respNode = responsibilitiesList.item(i);

			if (respNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eResponsability = (Element) respNode;

				if (eResponsability.getAttribute("respRefs").equals(idNode)) {

					Responsibility child = new Responsibility(this.translateNameSimpleElement(idNode));
					child.setIdUCM(idNode);
					saveObject(child);

					NodeList metadatasList = eResponsability.getElementsByTagName("metadata");

					// search metadata
					for (int k = 0; k < metadatasList.getLength(); k++) {
						Node node = metadatasList.item(k);
						Element metadata = (Element) node;
						String metadataName = metadata.getAttribute("name");

						if (metadataName.equals("MeanExecutionTime")) {
							SpecificationParameter sp = new SpecificationParameter();
							sp.setMetric((Metric) this.listMetric("Mean Execution Time").get(0));
							sp.setValue(Double.parseDouble(metadata.getAttribute("value")));
							sp.setUnit(this.getUnit());
							saveObject(sp);
							child.getSpecificationParameter().add(sp);
						} else if (metadataName.equals("MeanDowntime")) {
							SpecificationParameter sp = new SpecificationParameter();
							sp.setMetric((Metric) this.listMetric("Mean Downtime").get(0));
							sp.setValue(Double.parseDouble(metadata.getAttribute("value")));
							sp.setUnit(this.getUnit());
							saveObject(sp);
							child.getSpecificationParameter().add(sp);
						} else if (metadataName.equals("MeanRecoveryTime")) {
							SpecificationParameter sp = new SpecificationParameter();
							sp.setMetric((Metric) this.listMetric("Mean Recovery Time").get(0));
							sp.setValue(Double.parseDouble(metadata.getAttribute("value")));
							sp.setUnit(this.getUnit());
							saveObject(sp);
							child.getSpecificationParameter().add(sp);
						} else if (metadataName.equals("MeanTimeBFail")) {
							SpecificationParameter sp = new SpecificationParameter();
							sp.setMetric((Metric) this.listMetric("Mean Time B Fail").get(0));
							sp.setValue(Double.parseDouble(metadata.getAttribute("value")));
							sp.setUnit(this.getUnit());
							saveObject(sp);
							child.getSpecificationParameter().add(sp);
						}

					}
					updateObject(child);
					pathElements.add(child);

					DefaultMutableTreeNode hijoN = new DefaultMutableTreeNode(child);

					model.insertNodeInto(hijoN, parentNode, index);
					index++;

					return 0;

				}
			}

		}

		for (int i = 0; i < nodesList.getLength(); i++) {
			Node node = nodesList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eCurrentNode = (Element) node;
				String nodeType = eCurrentNode.getAttribute("xsi:type");
				if (eCurrentNode.getAttribute("id").equals(idNode) && !nodeType.equals("ucm.map:StartPoint")) {
					if (nodeType.equals("ucm.map:EndPoint")) {
						EndPoint child = new EndPoint(this.translateNameSimpleElement(idNode));
						pathElements.add(child);
					} else if (nodeType.equals("ucm.map:OrFork")) {
						ORFork child = new ORFork(this.translateNameSimpleElement(idNode));
						ArrayList<Double> pathProbabilities = new ArrayList<Double>();

						NodeList metadatasList = eCurrentNode.getElementsByTagName("metadata");
						// search metadata
						for (int k = 0; k < metadatasList.getLength(); k++) {
							Node metadataNode = metadatasList.item(k);
							Element metadata = (Element) metadataNode;

							pathProbabilities.add(Double.parseDouble(metadata.getAttribute("value")));
						}
						child.setPathProbabilities(pathProbabilities);
						pathElements.add(child);

						DefaultMutableTreeNode hijoN = new DefaultMutableTreeNode(child);

						model.insertNodeInto(hijoN, parentNode, index);
						index++;
					} else if (nodeType.equals("ucm.map:OrJoin")) {
						ORJoin child = new ORJoin(this.translateNameSimpleElement(idNode));
						pathElements.add(child);

						DefaultMutableTreeNode hijoN = new DefaultMutableTreeNode(child);

						model.insertNodeInto(hijoN, parentNode, index);
						index++;
					} else if (nodeType.equals("ucm.map:AndFork")) {
						ANDFork child = new ANDFork(this.translateNameSimpleElement(idNode));
						pathElements.add(child);

						DefaultMutableTreeNode hijoN = new DefaultMutableTreeNode(child);

						model.insertNodeInto(hijoN, parentNode, index);
						index++;
					} else if (nodeType.equals("ucm.map:AndJoin")) {
						ANDJoin child = new ANDJoin(this.translateNameSimpleElement(idNode));
						pathElements.add(child);

						DefaultMutableTreeNode hijoN = new DefaultMutableTreeNode(child);

						model.insertNodeInto(hijoN, parentNode, index);
						index++;
					}

				}
			}

		}

		return 0;

	}

	/**
	 * Create start point
	 * 
	 * @param startPoint
	 * @param pathElements
	 */
	private void creationStartPoint(StartPoint startPoint, Set<PathElement> pathElements) {

		NodeList nodesList = doc.getElementsByTagName("nodes");

		for (int i = 0; i < nodesList.getLength(); i++) {
			Node node = nodesList.item(i);

			Element eNode = (Element) node;

			if (eNode.getAttribute("xsi:type").equals("ucm.map:StartPoint")) {
				NodeList metadatasList = eNode.getElementsByTagName("metadata");

				Node currentNode = metadatasList.item(0);
				Element metadata = (Element) currentNode;

				startPoint = new StartPoint("start point", Double.parseDouble(metadata.getAttribute("value")));

			}

		}

		pathElements.add(startPoint);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
	}

	/**
	 * Return successor of a element
	 * 
	 * @param idSource
	 * @return
	 */
	private ArrayList<String> getSuccessor(String idSource) {
		ArrayList<String> successor = new ArrayList<String>();
		NodeList listConnections = doc.getElementsByTagName("connections");

		for (int i = 0; i < listConnections.getLength(); i++) {

			Node connection = listConnections.item(i);
			Element eConnection = (Element) connection;

			if (idSource.equals(eConnection.getAttribute("source"))) {
				successor.add(translateNameSimpleElement(eConnection.getAttribute("target")));
			}

		}

		return successor;
	}

	/**
	 * Return predecessor of a element
	 * 
	 * @param idTarget
	 * @return
	 */
	private ArrayList<String> getPredecessor(String idTarget) {
		ArrayList<String> predecessor = new ArrayList<String>();
		NodeList listConnections = doc.getElementsByTagName("connections");

		for (int i = 0; i < listConnections.getLength(); i++) {

			Node node = listConnections.item(i);
			Element eNode = (Element) node;

			if (idTarget.equals(eNode.getAttribute("target"))) {
				predecessor.add(translateNameSimpleElement(eNode.getAttribute("source")));
			}
		}

		return predecessor;
	}

	private Element getNodeOfResponsability(String idNode, Document doc) {
		NodeList nodesList = doc.getElementsByTagName("nodes");

		for (int i = 0; i < nodesList.getLength(); i++) {

			Node node = nodesList.item(i);
			Element eNode = (Element) node;

			String id = eNode.getAttribute("id");
			if (id.equals(idNode)) {
				return eNode;
			}

		}
		return null;
	}

	/**
	 * Return node in tree with id='id'
	 * 
	 * @param id
	 * @return
	 */
	private Element getNodeOfComponent(String id) {
		NodeList contRefsList = doc.getElementsByTagName("contRefs");

		for (int i = 0; i < contRefsList.getLength(); i++) {

			Node contRef = contRefsList.item(i);
			Element eContRef = (Element) contRef;

			if (id.equals(eContRef.getAttribute("id"))) {
				return eContRef;
			}
		}

		return null;
	}

	/**
	 * Return if memberNode is child of tree
	 * 
	 * @param rootNode
	 * @param memberNode
	 * @return
	 */
	private boolean isInTree(DefaultMutableTreeNode rootNode, DefaultMutableTreeNode memberNode) {
		java.util.Enumeration e = rootNode.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) e.nextElement();
			if (memberNode == currentNode) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return leafs (responsibility, andjoin, andfork, orjoin and orfork) of
	 * tree
	 * 
	 * @param node
	 * @return
	 */
	private ArrayList<DefaultMutableTreeNode> getLeafs(DefaultMutableTreeNode node) {
		Boolean terminationCondition = true;

		ArrayList<DefaultMutableTreeNode> leafList = new ArrayList<DefaultMutableTreeNode>();

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		java.util.Enumeration e = root.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) e.nextElement();
			if (currentNode.isLeaf()) {
				DefaultMutableTreeNode parent = currentNode;

				while (terminationCondition) {
					if (parent != root) {
						if (node.getLevel() != parent.getLevel()) {
							parent = (DefaultMutableTreeNode) parent.getParent();
						} else {
							terminationCondition = false;
						}
					} else {
						terminationCondition = false;
					}
				}

				if (node == parent) {
					leafList.add(currentNode);
				}
				terminationCondition = true;
			}
		}
		return leafList;

	}

	/**
	 * Return if is a simple component
	 * 
	 * @param pelem
	 * @return
	 */
	private boolean isASimpleComponent(Element pelem) {
		if (pelem.getAttribute("children").equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public String chequerUCM(String path) {
		return this.getPluginTS().callChequerUCM(path);
	}

	public String getPath() {
		return this.getArchitecture().getPathUCM().substring(0, this.getArchitecture().getPathUCM().lastIndexOf("\\"));
	}
	
	public String getName() {
		return this.getArchitecture().getPathUCM().substring(this.getArchitecture().getPathUCM().lastIndexOf("\\") + 1);
	}
	
	public Boolean deleteArchitecture() {
		return this.deleteObject(this.getArchitecture());
		//return this.updateSystem();
	}

	public Boolean removeArchitecture() {
		this.deleteAsociation();
		this.updateSystem();
		return this.deleteArchitecture();
	}
	
	/**
	 * Delete to current system, association with selected architecture
	 */
	private void deleteAsociation() {
		Iterator it = this.getArchitectures().iterator();
		while (it.hasNext()) {
			Architecture arc = (Architecture) it.next();

			boolean isNotDelete = false;
			if (arc.equals(this.getArchitecture())) {
					it.remove();
			}

		}
	}
}
