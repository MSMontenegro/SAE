package DomainModel.ReportsEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import Presentation.controllerReports.DataSourceCollection;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * 
 * @author: Florencia Rossini. E-mail: flori.rossini@gmail.com
 */

public class ireport {
	private DataSourceCollection dataSource;
	private JasperReport masterReport;
	private JasperPrint jasperPrint;
	private Map parameters;
	private String archive;

	public void addParameter(String pparamenterName, Object pparameterValue) {
		getParameters().put(pparamenterName, pparameterValue);
	}

	public void print() {
		try {
			JRDataSource jrd = null;
			if (this.getJasperPrint() == null) {
				setMasterReport((JasperReport) JRLoader.loadObjectFromFile(this.archive));
				try {
					jrd = this.getDataSource().createBeanCollectionDatasource();
				} catch (Exception ek) {
					System.out.println("error 1");
					ek.printStackTrace();
				}
				System.out.println("parametros=" + this.getParameters().toString());
				System.out.println("fuenteDeDatos: cantidad=" + DataSourceCollection.getColeccionDeDatos().size() + " "
						+ DataSourceCollection.getColeccionDeDatos().toString());
				jasperPrint = JasperFillManager.fillReport(this.getMasterReport(), this.getParameters(), jrd);
			}
		} catch (Exception e) {
			System.out.println(" " + e);
		}

	}

	public void setDataCollection(Collection pdata) {
		DataSourceCollection.setColeccionDeDatos(pdata);
	}

	public void visibleReport() {
		JasperViewer jviewer = new JasperViewer(this.getJasperPrint(), false);
		jviewer.setTitle("Report");
		jviewer.setVisible(true);
	}

	// Getters and setters
	public Map getParameters() {
		if (parameters == null) {
			parameters = new HashMap();
		}

		return parameters;

	}

	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public String getArchive() {
		return archive;
	}

	public void setArchive(String archive) {
		this.archive = archive;
	}

	public JasperReport getMasterReport() {
		return masterReport;
	}

	public void setMasterReport(JasperReport masterReport) {
		this.masterReport = masterReport;
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public DataSourceCollection getDataSource() {
		if (dataSource == null) {
			dataSource = new DataSourceCollection();
		}
		return dataSource;
	}

	public void setDataSource(DataSourceCollection dataSource) {
		this.dataSource = dataSource;
	}

}
