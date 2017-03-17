package BusinessLogic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import DataManager.HibernateManager;
import DomainModel.AnalysisEntity.QualityAttribute;
import DomainModel.AnalysisEntity.QualityRequirement;
import DomainModel.AnalysisEntity.Tactic;
import DomainModel.AnalysisEntity.Unit;
import DomainModel.ReportsEntity.ResponsibilityAvailability;
import DomainModel.ReportsEntity.ResponsibilityPerformance;
import DomainModel.ReportsEntity.ResponsibilityReliability;
import DomainModel.ReportsEntity.SystemAvailability;
import DomainModel.ReportsEntity.SystemPerformance;
import DomainModel.ReportsEntity.SystemPerformanceThroughput;
import DomainModel.ReportsEntity.SystemPerformanceTurnaroundTime;
import DomainModel.ReportsEntity.SystemReliability;
import DomainModel.ReportsEntity.ireport;
import DomainModel.SoftwareArchitectureEvaluationEntity.Indicator;
import DomainModel.SoftwareArchitectureEvaluationEntity.ResponsabilityIndicator;
import DomainModel.SoftwareArchitectureEvaluationEntity.Run;
import DomainModel.SoftwareArchitectureEvaluationEntity.SystemIndicator;
import DomainModel.SoftwareArchitectureSpecificationEntity.Architecture;

/**
 * This class is responsible for the management package: Report
 * 
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 * @version: 07/09/2016
 */
public class ReportManager extends HibernateManager {
	/**
	 * Attributes
	 */
	private DomainModel.AnalysisEntity.System system;
	private Architecture architecture;
	private QualityRequirement qualityRequirement;
	private QualityAttribute qualityAttribute;
	private static ReportManager manager;

	private ReportManager() {
		super();
	}

	/**
	 * Get SystemConfigurationManager. Applied Singleton pattern
	 */
	public static ReportManager getManager() {
		if (manager == null) {
			synchronized (ReportManager.class) {
				manager = new ReportManager();
			}
		}
		return manager;
	}

	public static void setManager(ReportManager manager) {
		ReportManager.manager = manager;
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

	public QualityRequirement getQualityRequirement() {
		return qualityRequirement;
	}

	public void setQualityRequirement(QualityRequirement qualityRequirement) {
		this.qualityRequirement = qualityRequirement;
	}

	public QualityAttribute getQualityAttribute() {
		return qualityAttribute;
	}

	public void setQualityAttribute(QualityAttribute qualityAttribute) {
		this.qualityAttribute = qualityAttribute;
	}

	public Set<Architecture> getArchitectures() {
		return this.getSystem().getArchitectures();
	}

	/**
	 * 
	 * @return ComboBoxModel with system names whose state==true and with
	 *         simulations
	 */
	public DomainModel.AnalysisEntity.System[] getComboModelSystemWithSimulations() {
		ArrayList<DomainModel.AnalysisEntity.System> systems = new ArrayList<DomainModel.AnalysisEntity.System>();
		for (DomainModel.AnalysisEntity.System auxSystem : this.listSystem()) {
			Iterator it = auxSystem.getArchitectures().iterator();
			if (it.hasNext()) {
				Architecture q = (Architecture) it.next();
				if (q.getSimulator() != null) {
					systems.add(auxSystem);
				}
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
	 * Return average turnaround time by responsibility
	 * 
	 * @return
	 */

	public List<ResponsibilityPerformance> listResponsibilityPerformance() {
		List<ResponsibilityPerformance> list = new ArrayList<ResponsibilityPerformance>();

		Architecture f = this.getArchitecture();
		int i = 0;
		for (Iterator<Run> its = f.getSimulator().getRuns().iterator(); its.hasNext();) {
			Run r = its.next();
			for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
				String name;
				Indicator ind = iti.next();
				if (ind.getType() instanceof ResponsabilityIndicator) {
					ResponsabilityIndicator respIndicator = (ResponsabilityIndicator) ind.getType();
					name = respIndicator.getResponsibility().getName();
				} else {
					name = ind.getType().getName();
				}
				if (ind.getMetric().getName().equals("Responsibility Turnaround Time")) {
					if (i == 0) {
						ResponsibilityPerformance item = new ResponsibilityPerformance();
						item.setResponsibilityTT(name);
						item.setTurnaroundTime(
								this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()) / 10);
						list.add(item);
					} else {
						Iterator ite = list.iterator();
						while (ite.hasNext()) {
							ResponsibilityPerformance q = (ResponsibilityPerformance) ite.next();
							if (q.getResponsibilityTT().equals(name)) {
								q.setTurnaroundTime(q.getTurnaroundTime()
										+ (this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit())
												/ 10));
							}
						}
					}
				}
			}
			i++;
		}

		return list;
	}

	/**
	 * Return average fails by responsibility
	 * 
	 * @return
	 */
	public List<ResponsibilityReliability> listResponsibilityReliability() {

		List<ResponsibilityReliability> listResponsibility = new ArrayList<ResponsibilityReliability>();
		List<Integer> runsByResponsibility = new ArrayList<Integer>();

		Architecture f = this.getArchitecture();

		for (Iterator<Run> its = f.getSimulator().getRuns().iterator(); its.hasNext();) {
			Run r = its.next();
			for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
				String name;
				Indicator ind = iti.next();
				if (ind.getType() instanceof ResponsabilityIndicator) {
					ResponsabilityIndicator respIndicator = (ResponsabilityIndicator) ind.getType();
					name = respIndicator.getResponsibility().getName();
				} else {
					name = ind.getType().getName();
				}
				if (ind.getMetric().getName().equals("Responsibility Failures")) {
					int index = 0;
					Boolean exist = false;
					Iterator ite = listResponsibility.iterator();
					while (ite.hasNext()) {
						ResponsibilityReliability q = (ResponsibilityReliability) ite.next();
						if (q.getResponsibilityF().equals(name)) {
							exist = true;
							q.setFails(q.getFails() + (ind.getValue()));
							runsByResponsibility.set(index, runsByResponsibility.get(index) + 1);
						}
						index++;
					}
					if (!exist) {
						ResponsibilityReliability item = new ResponsibilityReliability();
						item.setResponsibilityF(name);
						item.setFails(ind.getValue());
						runsByResponsibility.add(1);
						listResponsibility.add(item);
					}
				}
			}
		}

		int index = 0;
		Iterator ite = listResponsibility.iterator();
		while (ite.hasNext()) {
			ResponsibilityReliability q = (ResponsibilityReliability) ite.next();
			q.setFails(q.getFails() / runsByResponsibility.get(index));
			index++;
		}
		return listResponsibility;
	}

	/**
	 * Return average downtime and average recovery time by responsibility
	 * 
	 * @return
	 */
	public List<ResponsibilityAvailability> listResponsibilityAvailability() {
		// TODO preguntar a vero si mismo que downtime es igual a la cantidad de
		// corridas para el otro
		List<ResponsibilityAvailability> listResponsibility = new ArrayList<ResponsibilityAvailability>();
		List<Integer> runsByResponsibility = new ArrayList<Integer>();

		Architecture f = this.getArchitecture();
		int i = 0;
		for (Iterator<Run> its = f.getSimulator().getRuns().iterator(); its.hasNext();) {
			Run r = its.next();
			for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
				String name;
				Indicator ind = iti.next();
				if (ind.getType() instanceof ResponsabilityIndicator) {
					ResponsabilityIndicator respIndicator = (ResponsabilityIndicator) ind.getType();
					name = respIndicator.getResponsibility().getName();
				} else {
					name = ind.getType().getName();
				}
				if (ind.getMetric().getName().equals("Responsibility Downtime")) {
					int index = 0;
					Boolean exist = false;
					Iterator ite = listResponsibility.iterator();
					while (ite.hasNext()) {
						ResponsibilityAvailability q = (ResponsibilityAvailability) ite.next();
						if (q.getResponsibility().equals(name)) {
							exist = true;
							q.setDowntime(q.getDowntime()
									+ this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
							runsByResponsibility.set(index, runsByResponsibility.get(index) + 1);
						}
						index++;
					}
					if (!exist) {
						ResponsibilityAvailability item = new ResponsibilityAvailability();
						item.setResponsibility(name);
						item.setDowntime(this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
						listResponsibility.add(item);
						runsByResponsibility.add(1);
						exist = false;
					}
				}
				if (ind.getMetric().getName().equals("Responsibility Recovery Time")) {
					int index = 0;
					Iterator ite = listResponsibility.iterator();
					Boolean exist = false;
					while (ite.hasNext()) {
						ResponsibilityAvailability q = (ResponsibilityAvailability) ite.next();
						if (q.getResponsibility().equals(name)) {
							exist = true;
							q.setRecoveryTime(q.getRecoveryTime()
									+ this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
						}
						index++;
					}
					if (!exist) {
						ResponsibilityAvailability item = new ResponsibilityAvailability();
						item.setResponsibility(name);
						item.setRecoveryTime(this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
						runsByResponsibility.add(1);
					}

				}
			}

		}

		int index = 0;
		Iterator ite = listResponsibility.iterator();
		while (ite.hasNext()) {
			ResponsibilityAvailability q = (ResponsibilityAvailability) ite.next();
			q.setDowntime(q.getDowntime() / runsByResponsibility.get(index));
			q.setRecoveryTime(q.getRecoveryTime() / runsByResponsibility.get(index));
			index++;
		}

		return listResponsibility;
	}

	/**
	 * Return System Availability Time and System No-Availability Time by run
	 * 
	 * @return
	 */
	public List<SystemAvailability> listSystemAvailability() {

		List<SystemAvailability> list = new ArrayList<SystemAvailability>();

		Architecture f = this.getArchitecture();
		int runNum = 1;
		for (Iterator<Run> its = f.getSimulator().getRuns().iterator(); its.hasNext();) {
			boolean isIndicator = false;
			Run r = its.next();
			boolean band = true;
			for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
				Indicator ind = iti.next();
				if (ind.getType() instanceof SystemIndicator && band) {
					SystemAvailability item = new SystemAvailability();
					item.setAvailabilityE(this.getAvailabilityTimeRequirement());
					item.setNoAvailabilityE(this.getNoAvailabilityTimeRequirement());
					item.setSystem(ind.getType().getName());
					item.setRun(Integer.toString(runNum));
					list.add(item);
					isIndicator = true;
					band = false;
				}
				if (ind.getMetric().getName().equals("System Availability Time")) {
					SystemAvailability q = list.get(runNum - 1);
					q.setAvailability(this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
				}
				if (ind.getMetric().getName().equals("System No-Availability Time")) {
					SystemAvailability q = list.get(runNum - 1);
					q.setNoAvailability(this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
				}
			}

			if (isIndicator) {
				runNum++;
			}
		}
		return list;
	}

	/**
	 * Return System Throughput and System Turnaround Time by run
	 * 
	 * @return
	 */
	public List<SystemPerformance> listSystemPerformance() {

		List<SystemPerformance> list = new ArrayList<SystemPerformance>();

		Architecture f = this.getArchitecture();
		int runNum = 1;
		for (Iterator<Run> its = f.getSimulator().getRuns().iterator(); its.hasNext();) {
			boolean isIndicator = false;
			Run r = its.next();
			boolean band = true;

			for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
				Indicator ind = iti.next();
				if (ind.getType() instanceof SystemIndicator && band) {
					SystemPerformance item = new SystemPerformance();
					item.setThroughputE(this.getThroughputRequirement());
					item.setTurnaroundTimeE(this.getTurnaroundTimeRequirement());
					item.setSystem(ind.getType().getName());
					item.setRun(Integer.toString(runNum));
					isIndicator = true;
					list.add(item);
					band = false;
				}
				if (ind.getMetric().getName().equals("System Throughput")) {
					SystemPerformance q = list.get(runNum - 1);
					q.setThroughput(this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
				}
				if (ind.getMetric().getName().equals("System Turnaround Time")) {
					SystemPerformance q = list.get(runNum - 1);
					q.setTurnaroundTime(this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
				}
			}
			if (isIndicator) {
				runNum++;
			}
		}
		return list;
	}

	/**
	 * Return System Turnaround Time by run
	 * 
	 * @return
	 */
	public List<SystemPerformanceTurnaroundTime> listSystemPerformanceTurnaroundTime() {

		List<SystemPerformanceTurnaroundTime> list = new ArrayList<SystemPerformanceTurnaroundTime>();

		Architecture f = this.getArchitecture();
		int runNum = 1;
		for (Iterator<Run> its = f.getSimulator().getRuns().iterator(); its.hasNext();) {
			boolean isIndicator = false;
			Run r = its.next();
			boolean band = true;

			for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
				Indicator ind = iti.next();
				if (ind.getType() instanceof SystemIndicator && band) {
					SystemPerformanceTurnaroundTime item = new SystemPerformanceTurnaroundTime();
					item.setTurnaroundTimeE(this.getTurnaroundTimeRequirement());
					item.setSystem(ind.getType().getName());
					item.setRun(Integer.toString(runNum));
					isIndicator = true;
					list.add(item);
					band = false;
				}
				if (ind.getMetric().getName().equals("System Turnaround Time")) {
					SystemPerformanceTurnaroundTime q = list.get(runNum - 1);
					q.setTurnaroundTime(this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
				}
			}
			if (isIndicator) {
				runNum++;
			}
		}
		return list;
	}

	/**
	 * Return System Throughput by run
	 * 
	 * @return
	 */
	public List<SystemPerformanceThroughput> listSystemPerformanceThroughput() {

		List<SystemPerformanceThroughput> list = new ArrayList<SystemPerformanceThroughput>();

		Architecture f = this.getArchitecture();
		int runNum = 1;
		for (Iterator<Run> its = f.getSimulator().getRuns().iterator(); its.hasNext();) {
			boolean isIndicator = false;
			Run r = its.next();
			boolean band = true;

			for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
				Indicator ind = iti.next();
				if (ind.getType() instanceof SystemIndicator && band) {
					SystemPerformanceThroughput item = new SystemPerformanceThroughput();
					item.setThroughputE(this.getThroughputRequirement());
					item.setSystem(ind.getType().getName());
					item.setRun(Integer.toString(runNum));
					isIndicator = true;
					list.add(item);
					band = false;
				}
				if (ind.getMetric().getName().equals("System Throughput")) {
					SystemPerformanceThroughput q = list.get(runNum - 1);
					q.setThroughput(this.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()));
				}
			}
			if (isIndicator) {
				runNum++;
			}
		}
		return list;
	}

	/**
	 * Return System Failures by run
	 * 
	 * @return
	 */
	public List<SystemReliability> listSystemReliability() {

		List<SystemReliability> list = new ArrayList<SystemReliability>();

		Architecture f = this.getArchitecture();
		int runNum = 1;
		for (Iterator<Run> its = f.getSimulator().getRuns().iterator(); its.hasNext();) {
			Run r = its.next();
			boolean isIndicator = false;
			for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
				Indicator ind = iti.next();
				if (ind.getMetric().getName().equals("System Failures")) {
					SystemReliability item = new SystemReliability();
					item.setFailsE(this.getQualityRequirement().getQualityScenario().getResponseMeasure().getValue());
					item.setFails(ind.getValue());
					item.setSystem(ind.getType().getName());
					item.setRun(Integer.toString(runNum));
					isIndicator = true;
					list.add(item);
				}
			}
			if (isIndicator) {
				runNum++;
			}
		}
		return list;
	}

	private double getAvailabilityTimeRequirement() {
		if (this.getQualityRequirement().getQualityScenario().getResponseMeasure().getMetric().getName()
				.equals("System Availability Time")) {
			return this.getQualityRequirement().getQualityScenario().getResponseMeasure().getValue();
		} else {
			return (this.getSimulationTime()
					- this.getQualityRequirement().getQualityScenario().getResponseMeasure().getValue());
		}
	}

	private double getNoAvailabilityTimeRequirement() {
		if (this.getQualityRequirement().getQualityScenario().getResponseMeasure().getMetric().getName()
				.equals("System No-Availability Time")) {
			return this.getQualityRequirement().getQualityScenario().getResponseMeasure().getValue();
		} else {
			return this.getSimulationTime()
					- this.getQualityRequirement().getQualityScenario().getResponseMeasure().getValue();
		}
	}

	private double getThroughputRequirement() {
		if (this.getQualityRequirement().getQualityScenario().getResponseMeasure().getMetric().getName()
				.equals("System Throughput")) {
			return this.getQualityRequirement().getQualityScenario().getResponseMeasure().getValue();
		}
		return 0;
	}

	private double getTurnaroundTimeRequirement() {
		if (this.getQualityRequirement().getQualityScenario().getResponseMeasure().getMetric().getName()
				.equals("System Turnaround Time")) {
			return this.getQualityRequirement().getQualityScenario().getResponseMeasure().getValue();
		}
		return 0;
	}

	private double convertValueAcordingToUnitRequirement(double pvalue, Unit punit) {
		double pvalueConvert = 0;
		if (punit == this.getUnitRequirement()) {
			pvalueConvert = pvalue;
		} else {
			if (this.getUnitRequirement().getName().equals("Minutes")) {
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
			} else if (this.getUnitRequirement().getName().equals("Seconds")) {
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
			} else if (this.getUnitRequirement().getName().equals("Hours")) {
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
			} else if (this.getUnitRequirement().getName().equals("Milliseconds")) {
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
			} else if (this.getUnitRequirement().getName().equals("Days")) {
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
			} else if (this.getUnitRequirement().getName().equals("Weeks")) {
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
			} else if (this.getUnitRequirement().getName().equals("Months")) {
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
			} else if (this.getUnitRequirement().getName().equals("Request/Hour")) {
				if (punit.getName().equals("Days")) {
					pvalueConvert = pvalue / 24;
				} else if (punit.getName().equals("Weeks")) {
					pvalueConvert = pvalue / 168;
				} else if (punit.getName().equals("Months")) {
					pvalueConvert = pvalue / 672;
				}
			} else if (this.getUnitRequirement().getName().equals("Request/Day")) {
				if (punit.getName().equals("Hours")) {
					pvalueConvert = pvalue * 24;
				} else if (punit.getName().equals("Weeks")) {
					pvalueConvert = pvalue / 7;
				} else if (punit.getName().equals("Months")) {
					pvalueConvert = pvalue / 28;
				} else if (punit.getName().equals("Seconds")) {
					pvalueConvert = pvalue * 86400;
				} else if (punit.getName().equals("Minutes")) {
					pvalueConvert = pvalue * 1440;
				} else if (punit.getName().equals("Milliseconds")) {
					pvalueConvert = pvalue * 86400000;
				}
			} else if (this.getUnitRequirement().getName().equals("Request/Week")) {
				if (punit.getName().equals("Hours")) {
					pvalueConvert = pvalue * 168;
				} else if (punit.getName().equals("Days")) {
					pvalueConvert = pvalue * 7;
				} else if (punit.getName().equals("Months")) {
					pvalueConvert = pvalue / 4;
				} else if (punit.getName().equals("Seconds")) {
					pvalueConvert = pvalue * 604800;
				} else if (punit.getName().equals("Minutes")) {
					pvalueConvert = pvalue * 10080;
				} else if (punit.getName().equals("Milliseconds")) {
					pvalueConvert = pvalue * 604800000;
				}
			} else if (this.getUnitRequirement().getName().equals("Request/Month")) {
				if (punit.getName().equals("Hours")) {
					pvalueConvert = pvalue * 672;
				} else if (punit.getName().equals("Days")) {
					pvalueConvert = pvalue * 28;
				} else if (punit.getName().equals("Weeks")) {
					pvalueConvert = pvalue * 4;
				} else if (punit.getName().equals("Seconds")) {
					pvalueConvert = pvalue * 2419200;
				} else if (punit.getName().equals("Minutes")) {
					pvalueConvert = pvalue * 40320;
				} else if (punit.getName().equals("Milliseconds")) {
					BigDecimal num1 = new BigDecimal(pvalue);
					BigDecimal num2 = new BigDecimal("2419200000");
					pvalueConvert = (num1.multiply(num2)).doubleValue();
				}
			}
		}
		return pvalueConvert;

	}

	public Unit getUnitRequirement() {
		return this.getQualityRequirement().getQualityScenario().getResponseMeasure().getUnit();
	}

	/**
	 * Return evaluation simulation time
	 * 
	 * @return
	 */
	private double getSimulationTime() {
		Iterator<Run> its = this.getArchitecture().getSimulator().getRuns().iterator();
		Run r = its.next();
		return this.convertValueAcordingToUnitRequirement(r.getSimulationHorizon(),
				this.getArchitecture().getSimulator().getUnit());
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

	public boolean existSystemTrueWithArchitecture() {
		for (DomainModel.AnalysisEntity.System auxTipo : this.listSystem()) {
			if (auxTipo.getArchitectures().isEmpty() == false) {
				return true;
			}
		}
		return false;
	}

	public Boolean createReport(String path, String title, Collection pdata, Boolean isUnit,
			boolean isCumplimentRequirement) {
		try {
			ireport report = new ireport();

			report.setArchive(path);

			report.addParameter("tituloMembrete", "Reportes");
			report.addParameter("tituloMembrete2", "ttt");
			report.addParameter("frase", "");
			report.addParameter("pieMembrete", "");
			report.addParameter("title", title);
			report.addParameter("title", title);
			if (isUnit) {
				report.addParameter("unit",
						this.getQualityRequirement().getQualityScenario().getResponseMeasure().getUnit().getName());
			}
			if (!isCumplimentRequirement) {
				report.addParameter("tactics",
						"The requirement hasn't been completed. You can apply the next tactics:\n" + this.getTactics());
			} else {
				report.addParameter("tactics", "The requirement has been completed.");
			}
			report.setDataCollection(pdata);

			report.print();
			report.visibleReport();
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}

	}

	private String getTactics() {
		QualityAttribute q = this.getQualityAttribute();
		String tactics = "";
		Iterator t = q.getTactics().iterator();
		while (t.hasNext()) {
			Tactic tc = (Tactic) t.next();
			tactics = tactics + "- " + tc.getName() + "\n";
		}
		return tactics;
	}

	public boolean isCumplimentRequirement() {
		boolean cumpliment = true;
		if (this.getManager().getQualityAttribute().getName().equals("Reliability")) {
			for (Iterator<Run> its = this.getManager().getArchitecture().getSimulator().getRuns().iterator(); its
					.hasNext();) {
				Run r = its.next();
				for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
					Indicator ind = iti.next();
					if (ind.getMetric().getName().equals("System Failures")) {
						if (this.getManager().getQualityRequirement().getQualityScenario().getResponseMeasure()
								.getValue() < ind.getValue()) {
							cumpliment = false;
						}
					}
				}
			}
		} else if (this.getManager().getQualityAttribute().getName().equals("Availability")) {
			for (Iterator<Run> its = this.getManager().getArchitecture().getSimulator().getRuns().iterator(); its
					.hasNext();) {
				Run r = its.next();
				for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
					Indicator ind = iti.next();
					if (ind.getMetric().getName().equals("System Availability Time")) {
						if (this.getManager().getAvailabilityTimeRequirement() > this.getManager()
								.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit()))
							cumpliment = false;
					}
					if (ind.getMetric().getName().equals("System No-Availability Time")) {
						if (this.getManager().getNoAvailabilityTimeRequirement() < this.getManager()
								.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit())) {
							cumpliment = false;
						}
					}
				}
			}
		} else if (this.getManager().getQualityAttribute().getName().equals("Performance")) {
			for (Iterator<Run> its = this.getManager().getArchitecture().getSimulator().getRuns().iterator(); its
					.hasNext();) {
				Run r = its.next();
				for (Iterator<Indicator> iti = r.getIndicators().iterator(); iti.hasNext();) {
					Indicator ind = iti.next();
					if (ind.getMetric().getName().equals("System Throughput")) {
						if (this.getManager().getThroughputRequirement() < this.getManager()
								.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit())) {
							cumpliment = false;
						}
					}
					if (ind.getMetric().getName().equals("System Turnaround Time")) {
						if (this.getManager().getTurnaroundTimeRequirement() < this.getManager()
								.convertValueAcordingToUnitRequirement(ind.getValue(), ind.getUnit())) {
							cumpliment = false;
						}
					}
				}
			}
		}
		return cumpliment;
	}

}
