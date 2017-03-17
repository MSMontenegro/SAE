package BusinessLogic;

import java.util.List;

import Configuration.DOM;
import DataManager.Data;
import DataManager.HibernateManager;
import DataManager.HibernateUtil;
import DomainModel.AnalysisEntity.QualityAttribute;

/**
 * This class is responsible for the management package: system configuration
 * 
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 * @version: 08/08/2016
 */

public class SystemConfigurationManager extends HibernateManager {

	/**
	 * Attributes
	 */
	private static SystemConfigurationManager manager;

	/**
	 * Builder
	 */
	private SystemConfigurationManager() {
		super();
	}

	/**
	 * Get SystemConfigurationManager. Applied Singleton pattern
	 */
	public static SystemConfigurationManager getManager() {
		if (manager == null) {
			synchronized (SystemConfigurationManager.class) {
				manager = new SystemConfigurationManager();
			}
		}
		return manager;
	}

	public static void setManager(SystemConfigurationManager manager) {
		SystemConfigurationManager.manager = manager;
	}

	/**
	 * Getters and Setters
	 */

	public String getUserName() {
		return this.getDb().getUserName();
	}

	public String getPortNumber() {
		return this.getDb().getPortName();
	}

	public String getPassword() {
		return this.getDb().getPassword();
	}

	public String getDatabaseName() {
		return this.getDb().getDatabaseName();
	}

	/**
	 * Return if connection with database is success
	 * 
	 * @param password
	 * @param username
	 * @param portnumber
	 * @param databaseName
	 * @return
	 */
	public Boolean isConnection(String password, String username, String portnumber, String databaseName) {
		this.getDb().setPassword(password);
		this.getDb().setUserName(username);
		this.getDb().setPortName(portnumber);
		this.getDb().setDatabaseName(databaseName);
		if (HibernateUtil.getSession().isOpen()) {
			HibernateUtil.getSession().close();
		}
		HibernateUtil.initialize(this.getDb());
		HibernateUtil hu = new HibernateUtil();
		return hu.isConnection();
	}

	/**
	 * Updata database data to connection
	 * 
	 * @param password
	 * @param username
	 * @param portnumber
	 * @param databaseName
	 * @return
	 */
	public Boolean updateConnectionData(String password, String username, String portnumber, String databaseName) {
		try {
			this.getDb().setPassword(password);
			this.getDb().setUserName(username);
			this.getDb().setPortName(portnumber);
			this.getDb().setDatabaseName(databaseName);
			DOM.writePassword(password);
			DOM.writePortNumber(portnumber);
			DOM.writeUserName(username);
			DOM.writeDatabaseName(databaseName);
			if (HibernateUtil.getSession().isOpen()) {
				HibernateUtil.getSession().close();
			}
			HibernateUtil.initialize(this.getDb());
			this.dataInitialization();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Initialize data if database is empty
	 */
	private void dataInitialization() {
		List<QualityAttribute> list = this.listQualityAttribute();
		if (list != null && list.size() == 0) {
			Data.initialize();
		}
	}

	/**
	 * 
	 * @return List<QualityAttribute> with the names of the quality attributes
	 */
	private List<QualityAttribute> listQualityAttribute() {
		try {
			return this.listClass(QualityAttribute.class, "name");
		} catch (Exception e) {
			return null;
		}
	}

}