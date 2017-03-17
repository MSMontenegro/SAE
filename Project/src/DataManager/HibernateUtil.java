package DataManager;

import java.awt.HeadlessException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import Configuration.DatabaseConnection;

/**
 * This class initialize Hibernate
 *
 */

public class HibernateUtil {

	public static SessionFactory psessionFactory;
	public static Session psession;

	public static void initialize(DatabaseConnection db) {
		try {
			AnnotationConfiguration conf = new AnnotationConfiguration();
			try {
				conf.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
				conf.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				conf.setProperty("hibernate.connection.url",
						"jdbc:postgresql://localhost:" + db.getPortName() + "/" + db.getDatabaseName());
			} catch (Exception pe) {
				JOptionPane.showMessageDialog(null, "Database error N� 2001");
			}

			conf.setProperty("hibernate.connection.username", db.getUserName());
			conf.setProperty("hibernate.connection.password", db.getPassword());
			conf.setProperty("hibernate.connection.pool_size", "10");
			conf.setProperty("hibernate.hbm2ddl.auto", "update");

			conf.addPackage("DomainModel.AnalysisEntity");
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.Artifact.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.ArtifactType.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.Environment.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.EnvironmentType.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.GenericScenario.class);

			conf.addAnnotatedClass(DomainModel.AnalysisEntity.Metric.class);

			conf.addAnnotatedClass(DomainModel.AnalysisEntity.QualityAttribute.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.QualityRequirement.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.Response.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.ResponseMeasure.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.ResponseType.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.ResponseMeasureType.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.SpecificScenario.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.Stimulus.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.StimulusSource.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.StimulusSourceType.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.StimulusType.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.System.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.Unit.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.TacticType.class);
			conf.addAnnotatedClass(DomainModel.AnalysisEntity.Tactic.class);

			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureSpecificationEntity.StartPoint.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureSpecificationEntity.ORJoin.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureSpecificationEntity.ANDJoin.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureSpecificationEntity.ORFork.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureSpecificationEntity.ANDFork.class);
			conf.addAnnotatedClass(
					DomainModel.SoftwareArchitectureSpecificationEntity.SpecificationParameter.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureSpecificationEntity.Responsibility.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureSpecificationEntity.Path.class);
			conf.addAnnotatedClass(
					DomainModel.SoftwareArchitectureSpecificationEntity.CompositeComponent.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureSpecificationEntity.SimpleComponent.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureSpecificationEntity.Architecture.class);

			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureEvaluationEntity.Indicator.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureEvaluationEntity.IndicatorType.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureEvaluationEntity.ResponsabilityIndicator.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureEvaluationEntity.SystemIndicator.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureEvaluationEntity.Simulator.class);
			conf.addAnnotatedClass(DomainModel.SoftwareArchitectureEvaluationEntity.Run.class);

			try {
				psessionFactory = conf.buildSessionFactory();
				psession = psessionFactory.openSession();
			} catch (HibernateException pe) {
				JOptionPane.showMessageDialog(null, pe);
			}
		} catch (HeadlessException pex) {
			throw new ExceptionInInitializerError(pex);
		} catch (MappingException pex) {
			throw new ExceptionInInitializerError(pex);
		}
	}

	public Boolean isConnection() {
		try {
			try {
				getSession().connection().isClosed();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		} catch (HibernateException pe) {
			return false;
		}
	}

	public static Session getSession() throws HibernateException {
		return psession;
	}

}
