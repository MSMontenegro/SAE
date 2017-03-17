package BusinessLogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import DataManager.HibernateManager;
import DataManager.HibernateUtil;
import DomainModel.AnalysisEntity.Metric;
import DomainModel.AnalysisEntity.QualityRequirement;
import DomainModel.AnalysisEntity.Unit;
import DomainModel.SoftwareArchitectureEvaluationEntity.Indicator;
import DomainModel.SoftwareArchitectureEvaluationEntity.ResponsabilityIndicator;
import DomainModel.SoftwareArchitectureEvaluationEntity.Run;
import DomainModel.SoftwareArchitectureEvaluationEntity.Simulator;
import DomainModel.SoftwareArchitectureEvaluationEntity.SystemIndicator;
import DomainModel.SoftwareArchitectureSpecificationEntity.Architecture;
import DomainModel.SoftwareArchitectureSpecificationEntity.Responsibility;
import DomainModel.SoftwareArchitectureSpecificationEntity.SpecificationParameter;
import Main.TransformerSimulator;

/**
 * This class is responsible for the management package: Software Architecture
 * Specification
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 * @version: 23/08/2016
 */
public class SoftwareArchitectureEvaluationManager extends HibernateManager {

	/**
	 * Attributes
	 */
	private DomainModel.AnalysisEntity.System system;
	public static final String SEPARATOR = ";";
	public static final String QUOTE = "\"";
	private static final String PATH = Platform.getInstallLocation().getURL().getPath() + "plugins/SAE";
	private TransformerSimulator pluginTS;
	private Simulator simulator;
	private Architecture architecture;
	private Run run;
	private SystemIndicator typeIndicator;
	private static SoftwareArchitectureEvaluationManager manager;

	private SoftwareArchitectureEvaluationManager() {
		super();
	}

	/**
	 * Get SystemConfigurationManager. Applied Singleton pattern
	 */
	public static SoftwareArchitectureEvaluationManager getManager() {
		if (manager == null) {
			synchronized (SoftwareArchitectureEvaluationManager.class) {
				manager = new SoftwareArchitectureEvaluationManager();
			}
		}
		return manager;
	}

	public static void setManager(SoftwareArchitectureEvaluationManager manager) {
		SoftwareArchitectureEvaluationManager.manager = manager;
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

	public TransformerSimulator getPluginTS() {
		if (pluginTS == null) {
			pluginTS = new TransformerSimulator();
		}
		return pluginTS;
	}

	public void setPluginTS(TransformerSimulator pluginTS) {
		this.pluginTS = pluginTS;
	}

	public Simulator getSimulator() {
		return simulator;
	}

	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}

	public Architecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(Architecture architecture) {
		this.architecture = architecture;
	}

	public Run getRun() {
		return run;
	}

	public void setRun(Run run) {
		this.run = run;
	}

	public SystemIndicator getTypeIndicator() {
		return typeIndicator;
	}

	public void setTypeIndicator(SystemIndicator typeIndicator) {
		this.typeIndicator = typeIndicator;
	}

	public Set<Architecture> getArchitectures() {
		return this.getSystem().getArchitectures();
	}

	public Set<QualityRequirement> getQualityRequirements() {
		return this.getSystem().getQualityRequirements();
	}

	/**
	 * 
	 * @return ComboBoxModel with system names whose state==true and
	 *         architectures!=empty
	 * 
	 */
	public DomainModel.AnalysisEntity.System[] getComboModelSystemWithArchitecture() {
		ArrayList<DomainModel.AnalysisEntity.System> systems = new ArrayList<DomainModel.AnalysisEntity.System>();
		for (DomainModel.AnalysisEntity.System auxTipo : this.listSystem()) {
			if (auxTipo.getArchitectures().isEmpty() == false) {
				systems.add(auxTipo);
			}
		}
		DomainModel.AnalysisEntity.System[] arraySystem = new DomainModel.AnalysisEntity.System[systems.size()];
		systems.toArray(arraySystem);
		return arraySystem;
	}

	/**
	 * 
	 * @return List<System> with the system names whose state==true
	 */
	private List<DomainModel.AnalysisEntity.System> listSystem() {
		return this.listClass(DomainModel.AnalysisEntity.System.class, "systemName", true);
	}

	/**
	 * 
	 * @return ComboBoxModel with units
	 * 
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
	 * 
	 * @return List<Unit> with the unit names
	 */
	private List<Unit> listUnit() {
		return this.listClass(Unit.class, "name");
	}

	public boolean existSystemTrueWithArchitecture() {
		for (DomainModel.AnalysisEntity.System auxTipo : this.listSystem()) {
			if (auxTipo.getArchitectures().isEmpty() == false) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return True if there are systems whose state==true,
	 *         qualityRequirement!=empty and qualityRequirement.state==true,
	 *         else return false
	 * 
	 */
	public boolean existSystemTrueWithQualityRequirementTrue() {
		for (DomainModel.AnalysisEntity.System auxTipo : this.listSystem()) {
			if (auxTipo.getQualityRequirements().isEmpty() == false) {
				Iterator it = auxTipo.getQualityRequirements().iterator();
				while (it.hasNext()) {
					QualityRequirement q = (QualityRequirement) it.next();
					if (q.isState()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void createSimulator(Unit punit) {
		simulator = new Simulator(punit);
		typeIndicator = new SystemIndicator(this.getSystem().getSystemName());
		this.saveObject(typeIndicator);
	}

	private void createRun(Double psimulationTime, Table ptable) {
		run = new Run(GregorianCalendar.getInstance().getTime(), psimulationTime);
		this.getSimulator().getRuns().add(run);
		this.saveObject(this.getSimulator());

		TableItem[] items = ptable.getItems();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			if (item.getChecked()) {
				this.getSimulator().getRequirements().add((QualityRequirement) item.getData());
			}
		}

		this.getArchitecture().setSimulator(this.getSimulator());
		this.updateObject(this.getSystem());
	}

	/**
	 * Convert result simulation to object
	 * 
	 * @param ppath
	 * @throws IOException
	 */
	private void convertCSVToTable(String ppath) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(ppath));
			String line = br.readLine();
			while (null != line) {
				String[] fields = line.split(SEPARATOR);
				fields = removeTrailingQuotes(fields);
				loadDataInBd(fields, this.getTypeIndicator());
				line = br.readLine();
			}

		} catch (Exception e) {
			System.err.println("Error! " + e.getMessage());
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("Error closing file !! " + e.getMessage());
				}
			}
		}
	}

	private static String[] removeTrailingQuotes(String[] fields) {
		String result[] = new String[fields.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = fields[i].replaceAll("^" + QUOTE, "").replaceAll(QUOTE + "$", "");
		}
		return result;
	}

	private void loadDataInBd(String[] pfields, SystemIndicator type) {
		if (pfields[1].equals("system")) {
			loadSystemIndicator(pfields, type);
		} else {
			loadResponsabilityIndicator(pfields);
		}
	}

	private Responsibility[] getResponsibilities() {
		ArrayList<Responsibility> responsibilities = new ArrayList<Responsibility>();
		Iterator it2 = this.getArchitecture().getPaths().iterator();
		DomainModel.SoftwareArchitectureSpecificationEntity.Path path = (DomainModel.SoftwareArchitectureSpecificationEntity.Path) it2
				.next();
		Iterator it3 = path.getPathElements().iterator();
		while (it3.hasNext()) {
			DomainModel.SoftwareArchitectureSpecificationEntity.PathElement element = (DomainModel.SoftwareArchitectureSpecificationEntity.PathElement) it3
					.next();
			if (element.isResponsibility()) {
				Responsibility auxTipo = (Responsibility) element;
				responsibilities.add(auxTipo);
			}
		}
		Responsibility[] arrayResponsibility = new Responsibility[responsibilities.size()];
		responsibilities.toArray(arrayResponsibility);
		return arrayResponsibility;
	}

	private void loadSystemIndicator(String[] pfields, SystemIndicator ptype) {
		Indicator ind = new Indicator();
		ind.setType(ptype);
		ind.setValue(Double.parseDouble(pfields[3]));
		switch (pfields[2]) {
		case "SA":
			ind.setUnit(this.getUnitIndicator());
			ind.setMetric((Metric) this.listMetric("System Availability Time").get(0));
			break;
		case "SNA":
			ind.setUnit(this.getUnitIndicator());
			ind.setMetric((Metric) this.listMetric("System No-Availability Time").get(0));
			break;
		case "ST":
			ind.setUnit(this.getUnitIndicator());
			ind.setMetric((Metric) this.listMetric("System Throughput").get(0));
			break;
		case "STT":
			ind.setUnit(this.getUnitIndicator());
			ind.setMetric((Metric) this.listMetric("System Turnaround Time").get(0));
			break;
		case "SF":
			ind.setMetric((Metric) this.listMetric("System Failures").get(0));
			break;
		}
		this.getRun().getIndicators().add(ind);
		this.updateObject(this.getSystem());
	}

	private Unit getUnitIndicator() {
		Responsibility[] r = this.getResponsibilities();
		Iterator it = r[0].getSpecificationParameter().iterator();
		if (it.hasNext()) {
			SpecificationParameter sp = (SpecificationParameter) it.next();
			return sp.getUnit();
		}
		return null;
	}

	private void loadResponsabilityIndicator(String[] pfields) {
		Indicator ind = new Indicator();
		ResponsabilityIndicator type = new ResponsabilityIndicator(pfields[0]);
		type.setName(pfields[0]);
		type.setResponsibility(this.getResponsability(pfields[0]));
		this.saveObject(type);
		ind.setType(type);
		ind.setValue(Double.parseDouble(pfields[3]));

		switch (pfields[2]) {
		case "RDT":
			ind.setUnit(this.getUnitIndicator());
			ind.setMetric((Metric) this.listMetric("Responsibility Downtime").get(0));
			break;
		case "RRT":
			ind.setUnit(this.getUnitIndicator());
			ind.setMetric((Metric) this.listMetric("Responsibility Recovery Time").get(0));
			break;
		case "RTT":
			ind.setUnit(this.getUnitIndicator());
			ind.setMetric((Metric) this.listMetric("Responsibility Turnaround Time").get(0));
			break;
		case "RF":
			ind.setMetric((Metric) this.listMetric("Responsibility Failures").get(0));
			break;
		}
		this.getRun().getIndicators().add(ind);
		this.updateObject(this.getSystem());
	}

	private List listMetric(String pmetric) {
		Criteria crit = getSession().createCriteria(Metric.class).add(Restrictions.eq("name", pmetric));
		return crit.list();
	}

	private double convertValueAcordingToUnit(double pvalue, Unit punit) {
		double pvalueConvert = 0;
		if (punit == this.getUnitIndicator()) {
			pvalueConvert = pvalue;
		} else {
			if (this.getUnitIndicator().getName().equals("Minutes")) {
				if (punit.getName().equals("Seconds")) {
					pvalueConvert = pvalue / 60;
				} else if (punit.getName().equals("Hours")) {
					pvalueConvert = pvalue * 60;
				} else if (punit.getName().equals("Milliseconds")) {
					pvalueConvert = (pvalue / 60) / 1000;
				} else if (punit.getName().equals("Days")) {
					pvalueConvert = pvalue * 1440;
				} else if (punit.getName().equals("Weeks")) {
					pvalueConvert = pvalue * 10080;
				} else if (punit.getName().equals("Months")) {
					pvalueConvert = pvalue * 40320;
				}
			} else if (this.getUnitIndicator().getName().equals("Seconds")) {
				if (punit.getName().equals("Minutes")) {
					pvalueConvert = pvalue * 60;
				} else if (punit.getName().equals("Hours")) {
					pvalueConvert = pvalue * 3600;
				} else if (punit.getName().equals("Milliseconds")) {
					pvalueConvert = pvalue / 1000;
				} else if (punit.getName().equals("Days")) {
					pvalueConvert = pvalue * 86400;
				} else if (punit.getName().equals("Weeks")) {
					pvalueConvert = pvalue * 604800;
				} else if (punit.getName().equals("Months")) {
					pvalueConvert = pvalue * 40320 * 60;
				}
			} else if (this.getUnitIndicator().getName().equals("Hours")) {
				if (punit.getName().equals("Minutes")) {
					pvalueConvert = pvalue / 60;
				} else if (punit.getName().equals("Seconds")) {
					pvalueConvert = pvalue / 3600;
				} else if (punit.getName().equals("Milliseconds")) {
					pvalueConvert = (pvalue / 3600) / 1000;
				} else if (punit.getName().equals("Days")) {
					pvalueConvert = pvalue * 24;
				} else if (punit.getName().equals("Weeks")) {
					pvalueConvert = pvalue * 168;
				} else if (punit.getName().equals("Months")) {
					pvalueConvert = pvalue * 672;
				}
			} else if (this.getUnitIndicator().getName().equals("Milliseconds")) {
				if (punit.getName().equals("Minutes")) {
					pvalueConvert = pvalue * 60000;
				} else if (punit.getName().equals("Seconds")) {
					pvalueConvert = pvalue * 1000;
				} else if (punit.getName().equals("Hours")) {
					pvalueConvert = pvalue * 3600000;
				} else if (punit.getName().equals("Days")) {
					pvalueConvert = pvalue * 86400000;
				} else if (punit.getName().equals("Weeks")) {
					pvalueConvert = pvalue * 604800000;
				} else if (punit.getName().equals("Months")) {
					BigDecimal mil1 = new BigDecimal(pvalue);
					BigDecimal mil2 = new BigDecimal("2419200000");
					pvalueConvert = (mil1.multiply(mil2)).doubleValue();
				}
			} else if (this.getUnitIndicator().getName().equals("Days")) {
				if (punit.getName().equals("Seconds")) {
					pvalueConvert = pvalue / 86400;
				} else if (punit.getName().equals("Hours")) {
					pvalueConvert = pvalue / 24;
				} else if (punit.getName().equals("Milliseconds")) {
					pvalueConvert = pvalue / 86400000;
				} else if (punit.getName().equals("Minutes")) {
					pvalueConvert = pvalue / 1440;
				} else if (punit.getName().equals("Weeks")) {
					pvalueConvert = pvalue * 7;
				} else if (punit.getName().equals("Months")) {
					pvalueConvert = pvalue * 28;
				}
			} else if (this.getUnitIndicator().getName().equals("Weeks")) {
				if (punit.getName().equals("Seconds")) {
					pvalueConvert = pvalue / 604800;
				} else if (punit.getName().equals("Hours")) {
					pvalueConvert = pvalue / 168;
				} else if (punit.getName().equals("Milliseconds")) {
					pvalueConvert = pvalue / 604800000;
				} else if (punit.getName().equals("Days")) {
					pvalueConvert = pvalue / 7;
				} else if (punit.getName().equals("Minutes")) {
					pvalueConvert = pvalue / 10080;
				} else if (punit.getName().equals("Months")) {
					pvalueConvert = pvalue * 4;
				}
			} else if (this.getUnitIndicator().getName().equals("Months")) {
				if (punit.getName().equals("Seconds")) {
					pvalueConvert = pvalue / 2419200;
				} else if (punit.getName().equals("Hours")) {
					pvalueConvert = pvalue * 672;
				} else if (punit.getName().equals("Milliseconds")) {
					BigDecimal num1 = new BigDecimal(pvalue);
					BigDecimal num2 = new BigDecimal("2419200000");
					pvalueConvert = (num1.divide(num2)).doubleValue();
				} else if (punit.getName().equals("Days")) {
					pvalueConvert = pvalue / 28;
				} else if (punit.getName().equals("Weeks")) {
					pvalueConvert = pvalue / 4;
				} else if (punit.getName().equals("Minutes")) {
					pvalueConvert = pvalue * 40320;
				}
			}
		}
		return pvalueConvert;
	}

	public Responsibility getResponsability(String pname) {
		for (Responsibility dp : this.getResponsibilities()) {
			if (dp.getIdUCM().equals(pname)) {
				return dp;
			}
		}
		return null;
	}

	/*public String chequerUCM(String path) {
		return this.getPluginTS().callChequerUCM(path);
	}*/

	private String transformer(String inputPath) {
		return this.getPluginTS().callTransformer(inputPath, PATH.substring(1, PATH.length()));
	}

	private Boolean simulator(double observe_t, Unit punit) {
		double psimulationTime = this.convertValueAcordingToUnit(observe_t, punit);
		return this.getPluginTS().callSimulator(PATH, psimulationTime);
	}

	/**
	 * Delete files generate by plugin TS
	 */
	private void deleteFiles() {
		// Delete jar
		File jarFile = new File(PATH + "/simulator.jar");
		jarFile.delete();

		// Delete csv
		for (int i = 1; i < 11; i++) {
			File RunDirectory = new File(PATH + "/Run/Run" + i);

			String[] fileList;
			fileList = RunDirectory.list();
			for (int j = 0; j < fileList.length; j++) {
				File csvFile = new File(RunDirectory, fileList[j]);
				csvFile.delete();
			}
		}

		// Delete SystemTemp
		File Directory = new File(PATH + "/Simulator/src/SimEnvironment/SAModel/SystemTemp");

		String[] fileList;
		fileList = Directory.list();
		for (int i = 0; i < fileList.length; i++) {
			File file = new File(Directory, fileList[i]);
			file.delete();
		}

		// Delete test.class and simenvironment.class
		File testFile = new File(PATH + "/Simulator/src/Test.class");
		testFile.delete();

		File simEnvironmentFile = new File(PATH + "/Simulator/src/SimEnvironment/SimEnvironment.class");
		simEnvironmentFile.delete();

	}

	/**
	 * Return if connection with database is success
	 * 
	 * @return
	 */
	public Boolean isConnection() {
		if (HibernateUtil.getSession().isOpen()) {
			HibernateUtil.getSession().close();
		}
		HibernateUtil.initialize(this.getDb());
		HibernateUtil hu = new HibernateUtil();
		return hu.isConnection();
	}

	/**
	 * Evaluate architecture
	 * 
	 * @param UCMpath
	 * @param chequerUCMResult
	 * @param simulationTime
	 * @param unit
	 * @param system
	 * @param simulationTimeS
	 * @param table
	 * @return
	 * @throws IOException
	 */
	public int evaluate(String UCMpath, double simulationTime, Unit unit, DomainModel.AnalysisEntity.System system,
			String simulationTimeS, Table table) throws IOException {
		try {

			TransformerSimulator pluginTS = new TransformerSimulator();

			String resultTransformer = this.transformer(UCMpath);
			
			if (resultTransformer.equals("")) {

				if (this.simulator(simulationTime, unit)) {
					this.setSystem(system);

					this.createSimulator(unit);
					for (int i = 1; i <= 10; i++) {
						String num = Integer.toString(i);
						this.createRun(simulationTime, table);
						this.convertCSVToTable(Platform.getInstallLocation().getURL().getPath() + "plugins/SAE/Run/Run"
								+ num + "/performance.csv");
						this.convertCSVToTable(Platform.getInstallLocation().getURL().getPath() + "plugins/SAE/Run/Run"
								+ num + "/availability.csv");
						this.convertCSVToTable(Platform.getInstallLocation().getURL().getPath() + "plugins/SAE/Run/Run"
								+ num + "/reliability.csv");
					}
					this.deleteFiles();
					return 0;
				} else {
					return 1;
				}
			} else if (resultTransformer.equals("FileNotFoundException")){
				return 3;
			} else{
				return 2;
			}
		} catch (IOException e) {
			return 4;
		}
	}

}
