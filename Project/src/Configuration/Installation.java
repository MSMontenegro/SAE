package Configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.Platform;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * This class is responsible for the management package: Analysis
 * 
 * @author: Micaela Montenegro. E-mail: mica.montenegro.sistemas@gmail.com
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 */
public class Installation {
	private static final String PATH = Platform.getInstallLocation().getURL().getPath() + "plugins/SAE";

	public void exists() throws Exception {
		File directory = new File(PATH);

		if (!directory.exists()) {
			// create directories
			directory.mkdir();

			File configuration = new File(PATH + "/Configuration");
			configuration.mkdir();
			this.generateXML();

			File run = new File(PATH + "/Run");
			run.mkdir();
			for (int i = 1; i < 11; i++) {
				File runIterator = new File(PATH + "/Run/Run" + i);
				runIterator.mkdir();
			}

			File simulator = new File(PATH + "/Simulator");
			simulator.mkdir();

			File simulatorsrc = new File(PATH + "/Simulator/src");
			simulatorsrc.mkdir();

			File reports = new File(PATH + "/reports");
			reports.mkdir();

			// extract simulation project and reports in folder
			extractJar(simulatorsrc);
			extractReports(reports);
		}

	}

	private void extractJar(java.io.File directory) throws IOException {
		Path temp = Files.createTempFile("project-", ".jar");
		Files.copy(getClass().getClassLoader().getResourceAsStream("/Configuration/project.jar"), temp,
				StandardCopyOption.REPLACE_EXISTING);
		FileInputStream input = new FileInputStream(temp.toFile());
		java.util.jar.JarInputStream jarInput = new java.util.jar.JarInputStream(input);
		java.util.jar.JarEntry jarEntry = null;
		while ((jarEntry = jarInput.getNextJarEntry()) != null) {
			java.io.File file = new java.io.File(directory, jarEntry.getName());
			if (jarEntry.isDirectory()) {
				if (!file.exists())
					file.mkdirs();
			} else {
				java.io.File dir = new java.io.File(file.getParent());
				if (!dir.exists())
					dir.mkdirs();
				byte[] bytes = new byte[1024];
				java.io.InputStream inputStream = new BufferedInputStream(jarInput);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				int read = -1;
				while ((read = inputStream.read(bytes)) != -1) {
					fileOutputStream.write(bytes, 0, read);
				}
				fileOutputStream.close();
			}
		}
	}

	private void extractReports(java.io.File directory) throws IOException {
		Path temp = Files.createTempFile("reports-", ".jar");
		Files.copy(getClass().getClassLoader().getResourceAsStream("/Configuration/reports.jar"), temp,
				StandardCopyOption.REPLACE_EXISTING);
		FileInputStream input = new FileInputStream(temp.toFile());
		java.util.jar.JarInputStream jarInput = new java.util.jar.JarInputStream(input);
		java.util.jar.JarEntry jarEntry = null;
		while ((jarEntry = jarInput.getNextJarEntry()) != null) {
			java.io.File file = new java.io.File(directory, jarEntry.getName());
			if (jarEntry.isDirectory()) {
				if (!file.exists())
					file.mkdirs();
			} else {
				java.io.File dir = new java.io.File(file.getParent());
				if (!dir.exists())
					dir.mkdirs();
				byte[] bytes = new byte[1024];
				java.io.InputStream inputStream = new BufferedInputStream(jarInput);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				int read = -1;
				while ((read = inputStream.read(bytes)) != -1) {
					fileOutputStream.write(bytes, 0, read);
				}
				fileOutputStream.close();
			}
		}
	}

	private void generateXML() {
		try {
			String nameXML = "databaseconfiguration";

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation implementation = builder.getDOMImplementation();
			Document document = implementation.createDocument(null, nameXML, null);
			document.setXmlVersion("1.0");

			// Main Node
			Element root = document.getDocumentElement();

			Element itemUsername = document.createElement("username");
			Text nodeUsernameValue = document.createTextNode("postgres");
			itemUsername.appendChild(nodeUsernameValue);

			root.appendChild(itemUsername);

			Element itemPassword = document.createElement("password");
			Text nodePasswordValue = document.createTextNode("3325230E8CFE8DA79488EE7BE3CE4AE8");
			itemPassword.appendChild(nodePasswordValue);

			root.appendChild(itemPassword);

			Element itemPortnumber = document.createElement("portnumber");
			Text nodePortnumberValue = document.createTextNode("5432");
			itemPortnumber.appendChild(nodePortnumberValue);

			root.appendChild(itemPortnumber);

			Element itemDatabase = document.createElement("database");
			Text nodeDatabaseValue = document.createTextNode("database");
			itemDatabase.appendChild(nodeDatabaseValue);

			root.appendChild(itemDatabase);

			// Generate XML
			Source source = new DOMSource(document);
			// Indicamos donde lo queremos almacenar
			Result result = new StreamResult(new java.io.File(PATH + "/Configuration/" + nameXML + ".xml")); // nombre
			// del
			// archivo
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
		} catch (ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException e) {
			System.err.println(e);
		}
	}

}
