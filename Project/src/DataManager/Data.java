package DataManager;

import java.util.HashSet;
import java.util.Set;

import BusinessLogic.AnalysisManager;
import DomainModel.AnalysisEntity.ArtifactType;
import DomainModel.AnalysisEntity.EnvironmentType;
import DomainModel.AnalysisEntity.GenericScenario;
import DomainModel.AnalysisEntity.Metric;
import DomainModel.AnalysisEntity.QualityAttribute;
import DomainModel.AnalysisEntity.ResponseMeasureType;
import DomainModel.AnalysisEntity.ResponseType;
import DomainModel.AnalysisEntity.StimulusSourceType;
import DomainModel.AnalysisEntity.StimulusType;
import DomainModel.AnalysisEntity.Tactic;
import DomainModel.AnalysisEntity.TacticType;
import DomainModel.AnalysisEntity.Unit;

/**
 * This class initialize the conditions, quality attributes and its responsible
 * for creating specific scenarios (Creator Pattern).
 * 
 */
public class Data {
	static Metric m1 = new Metric("Responsibility Downtime");
	static Metric m2 = new Metric("Responsibility Recovery Time");
	static Metric m3 = new Metric("Responsibility Turnaround Time");
	static Metric m4 = new Metric("Responsibility Failures");
	static Metric m5 = new Metric("System Availability Time");
	static Metric m6 = new Metric("System No-Availability Time");
	static Metric m7 = new Metric("System Throughput");
	static Metric m8 = new Metric("System Turnaround Time");
	static Metric m9 = new Metric("System Failures");
	static Metric m10 = new Metric("Mean Execution Time");
	static Metric m11 = new Metric("Mean Downtime");
	static Metric m12 = new Metric("Mean Recovery Time");
	static Metric m13 = new Metric("Mean Time B Fail");

	public static void initialize() {

		AnalysisManager manager = AnalysisManager.getManager();

		Unit unit1 = new Unit("Seconds");
		manager.saveObject(unit1);
		Unit unit2 = new Unit("Minutes");
		manager.saveObject(unit2);
		Unit unit3 = new Unit("Hours");
		manager.saveObject(unit3);
		Unit unit4 = new Unit("Milliseconds");
		manager.saveObject(unit4);
		Unit unit5 = new Unit("Days");
		manager.saveObject(unit5);
		Unit unit6 = new Unit("Weeks");
		manager.saveObject(unit6);
		//Unit unit7 = new Unit("Months");
		//manager.saveObject(unit7);
		Unit unit8 = new Unit("Request/Hour");
		manager.saveObject(unit8);
		Unit unit9 = new Unit("Request/Day");
		manager.saveObject(unit9);
		Unit unit10 = new Unit("Request/Week");
		manager.saveObject(unit10);
		Unit unit11 = new Unit("Request/Month");
		manager.saveObject(unit11);

		Set<Unit> units = new HashSet<Unit>();
		units.add(unit1);
		units.add(unit2);
		units.add(unit3);
		units.add(unit4);
		units.add(unit5);
		units.add(unit6);
		//units.add(unit7);
		
		Set<Unit> unitsThroughput = new HashSet<Unit>();
		unitsThroughput.add(unit8);
		unitsThroughput.add(unit9);
		unitsThroughput.add(unit10);
		unitsThroughput.add(unit11);

		m1.setUnits(units);
		m2.setUnits(units);
		m3.setUnits(units);
		m4.setUnits(units);
		m5.setUnits(units);
		m6.setUnits(units);
		m7.setUnits(unitsThroughput);
		m8.setUnits(units);
		m9.setUnits(units);
		m10.setUnits(units);
		m11.setUnits(units);
		m12.setUnits(units);
		m13.setUnits(units);

		manager.saveObject(m1);
		manager.saveObject(m2);
		manager.saveObject(m3);
		manager.saveObject(m4);
		manager.saveObject(m5);
		manager.saveObject(m6);
		manager.saveObject(m7);
		manager.saveObject(m8);
		manager.saveObject(m9);
		manager.saveObject(m10);
		manager.saveObject(m11);
		manager.saveObject(m12);
		manager.saveObject(m13);

		TacticType tacticType1 = new TacticType("Fault detection");
		manager.saveObject(tacticType1);
		Tactic tactic11 = new Tactic("Ping/echo", tacticType1);
		Tactic tactic12 = new Tactic("Heartbeat", tacticType1);
		Tactic tactic13 = new Tactic("Exceptions", tacticType1);
		manager.saveObject(tactic11);
		manager.saveObject(tactic12);
		manager.saveObject(tactic13);

		TacticType tacticType2 = new TacticType("Fault recovery");
		manager.saveObject(tacticType2);
		Tactic tactic21 = new Tactic("Voting", tacticType2);
		Tactic tactic22 = new Tactic("Active redundancy", tacticType2);
		Tactic tactic23 = new Tactic("Passive redundancy", tacticType2);
		Tactic tactic24 = new Tactic("Spare", tacticType2);
		Tactic tactic25 = new Tactic("Shadow operation", tacticType2);
		Tactic tactic26 = new Tactic("State resynchronization", tacticType2);
		Tactic tactic27 = new Tactic("Checkpoint/rollback", tacticType2);
		manager.saveObject(tactic21);
		manager.saveObject(tactic22);
		manager.saveObject(tactic23);
		manager.saveObject(tactic24);
		manager.saveObject(tactic25);
		manager.saveObject(tactic25);
		manager.saveObject(tactic26);
		manager.saveObject(tactic27);

		TacticType tacticType3 = new TacticType("Fault prevention");
		manager.saveObject(tacticType3);
		Tactic tactic31 = new Tactic("Removal from service", tacticType3);
		Tactic tactic32 = new Tactic("Transactions", tacticType3);
		Tactic tactic33 = new Tactic("Process monitor", tacticType3);
		manager.saveObject(tactic31);
		manager.saveObject(tactic32);
		manager.saveObject(tactic33);

		Set<Tactic> tactics = new HashSet<Tactic>();
		tactics.add(tactic11);
		tactics.add(tactic12);
		tactics.add(tactic13);
		tactics.add(tactic21);
		tactics.add(tactic22);
		tactics.add(tactic23);
		tactics.add(tactic24);
		tactics.add(tactic25);
		tactics.add(tactic26);
		tactics.add(tactic27);
		tactics.add(tactic31);
		tactics.add(tactic32);
		tactics.add(tactic33);

		createQualityAttributeAvailability(manager, tactics);
		createQualityAttributeReliability(manager, tactics);
		createQualityAttributePerformance(manager);
	}

	private static void createQualityAttributeAvailability(HibernateManager pmanager, Set<Tactic> ptactics) {
		StimulusSourceType ss1 = new StimulusSourceType("Internal to the system");
		StimulusSourceType ss2 = new StimulusSourceType("External to the system");
		Set<StimulusSourceType> stimulusSourceTypes = new HashSet<StimulusSourceType>();
		stimulusSourceTypes.add(ss1);
		stimulusSourceTypes.add(ss2);

		StimulusType s1 = new StimulusType("Omission");
		StimulusType s2 = new StimulusType("Crash");
		StimulusType s3 = new StimulusType("Timing");
		StimulusType s4 = new StimulusType("Response");
		Set<StimulusType> stimulusTypes = new HashSet<StimulusType>();
		stimulusTypes.add(s1);
		stimulusTypes.add(s2);
		stimulusTypes.add(s3);
		stimulusTypes.add(s4);

		EnvironmentType e1 = new EnvironmentType("Normal operation");
		EnvironmentType e2 = new EnvironmentType("Degraded mode");
		Set<EnvironmentType> environmentTypes = new HashSet<EnvironmentType>();
		environmentTypes.add(e1);
		environmentTypes.add(e2);

		ArtifactType a1 = new ArtifactType("System's processors");
		ArtifactType a2 = new ArtifactType("Communication channels");
		ArtifactType a3 = new ArtifactType("Persistent storage");
		ArtifactType a4 = new ArtifactType("Processes");
		Set<ArtifactType> artifactTypes = new HashSet<ArtifactType>();
		artifactTypes.add(a1);
		artifactTypes.add(a2);
		artifactTypes.add(a3);
		artifactTypes.add(a4);

		ResponseType r1 = new ResponseType("Record it");
		ResponseType r2 = new ResponseType("Notify");
		ResponseType r3 = new ResponseType("Disable sources of events");
		ResponseType r4 = new ResponseType("Be unavailable for a prespecified interval");
		ResponseType r5 = new ResponseType("Continue to operate in normal or degraded mode");
		Set<ResponseType> responseTypes = new HashSet<ResponseType>();
		responseTypes.add(r1);
		responseTypes.add(r2);
		responseTypes.add(r3);
		responseTypes.add(r4);
		responseTypes.add(r5);

		Set<Metric> metrics = new HashSet<Metric>();
		metrics.add(m5);
		metrics.add(m6);

		ResponseMeasureType type1 = new ResponseMeasureType("Time interval when the system must be available", metrics);
		ResponseMeasureType type2 = new ResponseMeasureType("Availability Time", metrics);
		ResponseMeasureType type3 = new ResponseMeasureType("Time interval in which system can be in degraded mode",
				metrics);
		ResponseMeasureType type4 = new ResponseMeasureType("Repair time", metrics);
		Set<ResponseMeasureType> responseMeasureTypes = new HashSet<ResponseMeasureType>();
		responseMeasureTypes.add(type1);
		responseMeasureTypes.add(type2);
		responseMeasureTypes.add(type3);
		responseMeasureTypes.add(type4);

		QualityAttribute attribute;
		attribute = new QualityAttribute("Availability", new GenericScenario(stimulusSourceTypes, stimulusTypes,
				environmentTypes, artifactTypes, responseTypes, responseMeasureTypes), ptactics);
		attribute.setAttributeGenericScenario();
		pmanager.saveObject(attribute);

	}

	private static void createQualityAttributeReliability(HibernateManager pmanager,  Set<Tactic> ptactics) {
		StimulusSourceType ss1 = new StimulusSourceType("Internal to the system");
		StimulusSourceType ss2 = new StimulusSourceType("External to the system");
		Set<StimulusSourceType> stimulusSourceTypes = new HashSet<StimulusSourceType>();
		stimulusSourceTypes.add(ss1);
		stimulusSourceTypes.add(ss2);

		StimulusType s1 = new StimulusType("Omission");
		StimulusType s2 = new StimulusType("Crash");
		StimulusType s3 = new StimulusType("Timing");
		StimulusType s4 = new StimulusType("Response");
		Set<StimulusType> stimulusTypes = new HashSet<StimulusType>();
		stimulusTypes.add(s1);
		stimulusTypes.add(s2);
		stimulusTypes.add(s3);
		stimulusTypes.add(s4);

		EnvironmentType e1 = new EnvironmentType("Normal operation");
		EnvironmentType e2 = new EnvironmentType("Degraded mode");
		Set<EnvironmentType> environmentTypes = new HashSet<EnvironmentType>();
		environmentTypes.add(e1);
		environmentTypes.add(e2);

		ArtifactType a1 = new ArtifactType("System's processors");
		ArtifactType a2 = new ArtifactType("Communication channels");
		ArtifactType a3 = new ArtifactType("Persistent storage");
		ArtifactType a4 = new ArtifactType("Processes");
		Set<ArtifactType> artifactTypes = new HashSet<ArtifactType>();
		artifactTypes.add(a1);
		artifactTypes.add(a2);
		artifactTypes.add(a3);
		artifactTypes.add(a4);

		ResponseType r1 = new ResponseType("Record it");
		ResponseType r2 = new ResponseType("Notify");
		ResponseType r3 = new ResponseType("Disable sources of events");
		ResponseType r4 = new ResponseType("Be unavailable for a prespecified interval");
		ResponseType r5 = new ResponseType("Continue to operate in normal or degraded mode");
		Set<ResponseType> responseTypes = new HashSet<ResponseType>();
		responseTypes.add(r1);
		responseTypes.add(r2);
		responseTypes.add(r3);
		responseTypes.add(r4);
		responseTypes.add(r5);

		Metric metric1 = new Metric("Number of failures");
		pmanager.saveObject(metric1);
		Set<Metric> metrics = new HashSet<Metric>();

		metrics.add(metric1);

		ResponseMeasureType type1 = new ResponseMeasureType("Failures", metrics);
		Set<ResponseMeasureType> responseMeasureTypes = new HashSet<ResponseMeasureType>();
		responseMeasureTypes.add(type1);

		QualityAttribute attribute;
		attribute = new QualityAttribute("Reliability", new GenericScenario(stimulusSourceTypes, stimulusTypes,
				environmentTypes, artifactTypes, responseTypes, responseMeasureTypes), ptactics);
		attribute.setAttributeGenericScenario();
		pmanager.saveObject(attribute);

	}

	private static void createQualityAttributePerformance(HibernateManager pmanager) {
		StimulusSourceType ss1 = new StimulusSourceType("Stimulus from internal source");
		StimulusSourceType ss2 = new StimulusSourceType("Stimulus from external source");
		Set<StimulusSourceType> stimulusSourceTypes = new HashSet<StimulusSourceType>();
		stimulusSourceTypes.add(ss1);
		stimulusSourceTypes.add(ss2);

		StimulusType s1 = new StimulusType("Periodic events arrive");
		StimulusType s2 = new StimulusType("Sporadic events arrive");
		StimulusType s3 = new StimulusType("Stochastic events arrive");
		Set<StimulusType> stimulusTypes = new HashSet<StimulusType>();
		stimulusTypes.add(s1);
		stimulusTypes.add(s2);
		stimulusTypes.add(s3);

		EnvironmentType e1 = new EnvironmentType("Normal mode");
		EnvironmentType e2 = new EnvironmentType("Overload mode");
		Set<EnvironmentType> environmentTypes = new HashSet<EnvironmentType>();
		environmentTypes.add(e1);
		environmentTypes.add(e2);

		ArtifactType a1 = new ArtifactType("System");
		ArtifactType a2 = new ArtifactType("Functionality");
		Set<ArtifactType> artifactTypes = new HashSet<ArtifactType>();
		artifactTypes.add(a1);
		artifactTypes.add(a2);

		ResponseType r1 = new ResponseType("Processes stimulus");
		ResponseType r2 = new ResponseType("Changes level of service");
		ResponseType r3 = new ResponseType("Disable sources of events");
		Set<ResponseType> responseTypes = new HashSet<ResponseType>();
		responseTypes.add(r1);
		responseTypes.add(r2);
		responseTypes.add(r3);

		Set<Metric> metrics = new HashSet<Metric>();
		metrics.add(m7);
		metrics.add(m8);

		ResponseMeasureType type1 = new ResponseMeasureType("Latency", metrics);
		ResponseMeasureType type2 = new ResponseMeasureType("Deadline", metrics);
		ResponseMeasureType type3 = new ResponseMeasureType("Throughput", metrics);
		ResponseMeasureType type4 = new ResponseMeasureType("Jitter", metrics);
		ResponseMeasureType type5 = new ResponseMeasureType("Miss rate", metrics);
		ResponseMeasureType type6 = new ResponseMeasureType("Data loss", metrics);
		Set<ResponseMeasureType> responseMeasureTypes = new HashSet<ResponseMeasureType>();
		responseMeasureTypes.add(type1);
		responseMeasureTypes.add(type2);
		responseMeasureTypes.add(type3);
		responseMeasureTypes.add(type4);
		responseMeasureTypes.add(type5);
		responseMeasureTypes.add(type6);

		TacticType tacticType1 = new TacticType("Resource demand");
		pmanager.saveObject(tacticType1);
		Tactic tactic11 = new Tactic("Increase computational efficiency", tacticType1);
		Tactic tactic12 = new Tactic("Reduce computational overhead", tacticType1);
		Tactic tactic13 = new Tactic("Manage event rate", tacticType1);
		Tactic tactic14 = new Tactic("Control frequency of sampling", tacticType1);
		Tactic tactic15 = new Tactic("Bound execution times", tacticType1);
		Tactic tactic16 = new Tactic("Bound queue sizes", tacticType1);
		pmanager.saveObject(tactic11);
		pmanager.saveObject(tactic12);
		pmanager.saveObject(tactic13);
		pmanager.saveObject(tactic14);
		pmanager.saveObject(tactic15);
		pmanager.saveObject(tactic16);

		TacticType tacticType2 = new TacticType("Resource management");
		pmanager.saveObject(tacticType2);
		Tactic tactic21 = new Tactic("Introduce concurrency", tacticType2);
		Tactic tactic22 = new Tactic("Maintain multiple copies of either data or computations", tacticType2);
		Tactic tactic23 = new Tactic("Increase available resources", tacticType2);
		pmanager.saveObject(tactic21);
		pmanager.saveObject(tactic22);
		pmanager.saveObject(tactic23);

		TacticType tacticType3 = new TacticType("Resource arbitration");
		pmanager.saveObject(tacticType3);
		Tactic tactic31 = new Tactic("First-in/First-out", tacticType3);
		Tactic tactic32 = new Tactic("Fixed-priority scheduling", tacticType3);
		Tactic tactic33 = new Tactic("Dynamic priority scheduling", tacticType3);
		Tactic tactic34 = new Tactic("Static scheduling", tacticType3);
		pmanager.saveObject(tactic31);
		pmanager.saveObject(tactic32);
		pmanager.saveObject(tactic33);
		pmanager.saveObject(tactic34);

		Set<Tactic> tactics = new HashSet<Tactic>();
		tactics.add(tactic11);
		tactics.add(tactic12);
		tactics.add(tactic13);
		tactics.add(tactic14);
		tactics.add(tactic15);
		tactics.add(tactic16);
		tactics.add(tactic21);
		tactics.add(tactic22);
		tactics.add(tactic23);
		tactics.add(tactic31);
		tactics.add(tactic32);
		tactics.add(tactic33);
		tactics.add(tactic34);

		QualityAttribute attribute;
		attribute = new QualityAttribute("Performance", new GenericScenario(stimulusSourceTypes, stimulusTypes,
				environmentTypes, artifactTypes, responseTypes, responseMeasureTypes), tactics);
		attribute.setAttributeGenericScenario();
		pmanager.saveObject(attribute);

	}

}
