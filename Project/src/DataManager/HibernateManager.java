package DataManager;

import java.util.List;

import javax.swing.JOptionPane;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import Configuration.DatabaseConnection;
import DomainModel.SoftwareArchitectureSpecificationEntity.Architecture;

/**
 * This class management Hibernate (list class, save and update objects)
 * 
 */

public class HibernateManager extends HibernateUtil {
	public Transaction tx;
	private DatabaseConnection db;

	public DatabaseConnection getDb() {
		if (db == null) {
			synchronized (DatabaseConnection.class) {
				db = new DatabaseConnection();
			}
		}
		return db;
	}

	public void setDb(DatabaseConnection db) {
		this.db = db;
	}
	
	public Transaction getTx() {
		return tx;
	}

	public void setTx(Transaction ptx) {
		this.tx = ptx;
	}

	public List listClass(Class pclass, String prdenAttribute) {
		Criteria crit = getSession().createCriteria(pclass).addOrder(Order.asc(prdenAttribute));
		return crit.list();
	}

	public List listClass(Class pclase, String patributoOrden, boolean pstate) {
		Criteria crit = getSession().createCriteria(pclase).addOrder(Order.asc(patributoOrden))
				.add(Restrictions.eq("state", pstate));
		return  crit.list();
	}
	
	public List listClass(Class pclase, String patributoOrden, Architecture parchitecture) {
		Criteria crit = getSession().createCriteria(pclase).addOrder(Order.asc(patributoOrden))
				.add(Restrictions.eq("state", parchitecture));
		return crit.list();
	}

	public boolean saveObject(Object pobjet) {
		try {
			Session s = HibernateUtil.getSession();
			Transaction tx = s.beginTransaction();
			s.save(pobjet);
			tx.commit();
			System.out.println(" saveObjet() " + pobjet.getClass() + ": " + pobjet.toString()); 
			return true;
		} catch (Exception pex) {
			System.out.println("error " + pex); 
			System.out.println(
					"Repository.saveObjet(Object objetj)" + pobjet.getClass() + ": " + pobjet.toString() + pex);
			pex.printStackTrace();
			getTx().rollback();
			return false;
		}
	}

	public boolean updateObject(Object pobjet) {
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		try {
			s.update(pobjet);
			tx.commit();
			System.out.println(" updateObjet() " + pobjet.getClass() + ": " + pobjet.toString()); 
			return true;
		} catch (HibernateException pe) {
			JOptionPane.showMessageDialog(null, pe);
			tx.rollback();
			return false;
		}
	}
	
	public boolean deleteObject(Object pobjet) {
		Session s = HibernateUtil.getSession();
		Transaction tx = s.beginTransaction();
		try {
			s.delete(pobjet);
			tx.commit();
			//System.out.println(" deleteObjet() " + pobjet.getClass() + ": " + pobjet.toString()); 
			return true;
		} catch (HibernateException pe) {
			JOptionPane.showMessageDialog(null, pe);
			tx.rollback();
			return false;
		}
	}

}
