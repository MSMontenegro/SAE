package Configuration;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class performs file operations xml
 * 
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 */
public class DOM {

	private static String PATH = Platform.getInstallLocation().getURL().getPath()
			+ "plugins/SAE/Configuration/databaseconfiguration.xml";

	public static String readPassword() {
		return CryptoUtils.decrypt(internalStructureRead("password"));
	}

	public static String readUserName() {
		return internalStructureRead("username");
	}

	public static String readDatabaseName() {
		return internalStructureRead("database");
	}

	public static String readPortNumber() {
		return internalStructureRead("portnumber");
	}

	public static void writePassword(String password) {
		String encryptorPassword = CryptoUtils.encrypt(password);
		internalStructureWrite("password", encryptorPassword);
	}

	public static void writeUserName(String username) {
		internalStructureWrite("username", username);
	}

	public static void writeDatabaseName(String database) {
		internalStructureWrite("database", database);
	}

	public static void writePortNumber(String portnumber) {
		internalStructureWrite("portnumber", portnumber);
	}

	/**
	 * Write in configuration file, attribute with value
	 * 
	 * @param attribute
	 * @param value
	 */
	private static void internalStructureWrite(String attribute, String value) {
		File fXmlFile = new File(PATH);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			try {
				doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("databaseconfiguration");

				for (int i = 0; i < nList.getLength(); i++) {
					Node nNode = nList.item(i);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						eElement.getElementsByTagName(attribute).item(0).setTextContent(value);
					}
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer;
				try {
					transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(PATH));
					try {
						transformer.transform(source, result);
					} catch (TransformerException e) {
						e.printStackTrace();
					}
				} catch (TransformerConfigurationException e) {
					e.printStackTrace();
				}

			} catch (SAXException | IOException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read attribute in configuration file
	 * 
	 * @param attribute
	 * @param value
	 */
	private static String internalStructureRead(String attribute) {
		File currentDirFile = new File(".");
		String helper = currentDirFile.getAbsolutePath();
		try {
			String currentDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		File fXmlFile = new File(PATH);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			try {
				doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("databaseconfiguration");

				for (int i = 0; i < nList.getLength(); i++) {
					Node nNode = nList.item(i);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						return eElement.getElementsByTagName(attribute).item(0).getTextContent();
					}
				}

			} catch (SAXException | IOException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return "";
	}

}
