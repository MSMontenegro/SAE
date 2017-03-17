package Presentation.controllerReports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta fuente de datos encapsula una coleccion del tipo java.util.Collection,
 * Cumple con el estandar usado por las fuentes de datos Jasper usadas por Ireport
 * @author Fernando Boiero
 */
public class DataSourceCollection {
	private static java.util.Collection DATA_COLLECTION;
     
	public  JRDataSource createBeanCollectionDatasource()
    {     
        return new JRBeanCollectionDataSource(createBeanCollection());
    }    
     
     public static  java.util.Collection  createBeanCollection()
     {   
            
        return getColeccionDeDatos();
     }

    public static java.util.Collection getColeccionDeDatos() {
        return DATA_COLLECTION;
    }

    public static void setColeccionDeDatos(java.util.Collection aColeccionDeDatos) {
        DATA_COLLECTION = aColeccionDeDatos;
    }
    
}
