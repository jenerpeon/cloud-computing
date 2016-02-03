package eventTicker.backend.dbWrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eventTicker.backend.ConcreteEvent;
import eventTicker.backend.ConcreteUser;

public class XmlSender extends DbSynchronizer {

	protected static XmlSender xmlSender;

	public static XmlSender getInstance() {
		if (xmlSender == null) {
			xmlSender = new XmlSender();
		}
		return xmlSender;
	}

	public String xmlEventBuilder(ConcreteEvent cevent, String type) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element dml = doc.createElement("dml");
			doc.appendChild(dml);

			Element event = doc.createElement("event");
			dml.appendChild(event);

			if (type.equals("delete")) {
				Element id = doc.createElement("id");
				id.appendChild(doc.createTextNode(cevent.getId().toString()));
				event.appendChild(id);
			} else {
				Element id = doc.createElement("id");
				id.appendChild(doc.createTextNode("23"));
				event.appendChild(id);
			}
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(cevent.getName()));
			event.appendChild(name);

			Element beschreibung = doc.createElement("beschreibung");
			beschreibung.appendChild(doc.createTextNode(cevent.getDescription()));
			event.appendChild(beschreibung);

			Element datum = doc.createElement("datum");
			datum.appendChild(doc.createTextNode("22-12-1960"));
			event.appendChild(datum);

			Element breite = doc.createElement("breite");
			breite.appendChild(doc.createTextNode(String.valueOf(cevent.getWidth())));
			event.appendChild(breite);

			Element laenge = doc.createElement("laenge");
			laenge.appendChild(doc.createTextNode(String.valueOf(cevent.getLength())));
			event.appendChild(laenge);

			if (type.equals("delete")) {
				Element status = doc.createElement("status");
				status.appendChild(doc.createTextNode("D"));
				event.appendChild(status);

				Element dmltyp = doc.createElement("dmltyp");
				dmltyp.appendChild(doc.createTextNode("UPDATE"));
				event.appendChild(dmltyp);

			} else {
				Element status = doc.createElement("status");
				status.appendChild(doc.createTextNode("V"));
				event.appendChild(status);

				Element dmltyp = doc.createElement("dmltyp");
				dmltyp.appendChild(doc.createTextNode("INSERT"));
				event.appendChild(dmltyp);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			StringWriter sw = new StringWriter();

			transformer.transform(new DOMSource(doc), new StreamResult(sw));

			return sw.toString();

		} catch (Exception e) {
			System.out.println("XML Template failed:" + e.getMessage());
			return "";
		}
	}

	public String xmlUserBuilder(ConcreteUser user, String type) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element dml = doc.createElement("dml");
			doc.appendChild(dml);

			Element nutzer = doc.createElement("nutzer");
			dml.appendChild(nutzer);

			if (type.equals("delete")) {
				Element id = doc.createElement("id");
				id.appendChild(doc.createTextNode(user.getId().toString()));
				nutzer.appendChild(id);
			} else {
				Element id = doc.createElement("id");
				id.appendChild(doc.createTextNode("23"));
				nutzer.appendChild(id);
			}
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(user.getName()));
			nutzer.appendChild(name);

			Element vorname = doc.createElement("vorname");
			vorname.appendChild(doc.createTextNode(user.getFirstName()));
			nutzer.appendChild(vorname);

			Element geburtsdatum = doc.createElement("geburtsdatum");
			geburtsdatum.appendChild(doc.createTextNode("22-12-1960"));
			nutzer.appendChild(geburtsdatum);

			Element breite = doc.createElement("breite");
			breite.appendChild(doc.createTextNode(String.valueOf(user.getWidth())));
			nutzer.appendChild(breite);

			Element laenge = doc.createElement("laenge");
			laenge.appendChild(doc.createTextNode(String.valueOf(user.getLength())));
			nutzer.appendChild(laenge);

			Element weblogin = doc.createElement("weblogin");
			weblogin.appendChild(doc.createTextNode(String.valueOf(user.getWeblogin())));
			nutzer.appendChild(weblogin);

			Element password = doc.createElement("passwort");
			password.appendChild(doc.createTextNode(String.valueOf(user.getPassword())));
			nutzer.appendChild(password);

			if (type.equals("delete")) {
				Element status = doc.createElement("status");
				status.appendChild(doc.createTextNode("D"));
				nutzer.appendChild(status);

				Element dmltyp = doc.createElement("dmltyp");
				dmltyp.appendChild(doc.createTextNode("UPDATE"));
				nutzer.appendChild(dmltyp);

			} else {
				Element status = doc.createElement("status");
				status.appendChild(doc.createTextNode(""));
				nutzer.appendChild(status);

				Element dmltyp = doc.createElement("dmltyp");
				dmltyp.appendChild(doc.createTextNode("INSERT"));
				nutzer.appendChild(dmltyp);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			StringWriter sw = new StringWriter();

			transformer.transform(new DOMSource(doc), new StreamResult(sw));

			return sw.toString();

		} catch (Exception e) {
			System.out.println("XML Template failed:" + e.getMessage());
			return "";
		}
	}

	public String xmlRelationBuilder(Long uid, Long eid, String type) {
		System.out.println("insert" + uid + "|" + eid);
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element dml = doc.createElement("dml");
			doc.appendChild(dml);

			Element kntzeve = doc.createElement("kntzeve");
			dml.appendChild(kntzeve);

			Element ntzid = doc.createElement("ntzid");
			ntzid.appendChild(doc.createTextNode(uid.toString()));
			kntzeve.appendChild(ntzid);

			Element eveid = doc.createElement("eveid");
			eveid.appendChild(doc.createTextNode(eid.toString()));
			kntzeve.appendChild(eveid);

			if (type.equals("delete")) {
				Element status = doc.createElement("status");
				status.appendChild(doc.createTextNode("D"));
				kntzeve.appendChild(status);

				Element dmltyp = doc.createElement("dmltyp");
				dmltyp.appendChild(doc.createTextNode("UPDATE"));
				kntzeve.appendChild(dmltyp);

			} else {
				Element status = doc.createElement("status");
				status.appendChild(doc.createTextNode(""));
				kntzeve.appendChild(status);

				Element dmltyp = doc.createElement("dmltyp");
				dmltyp.appendChild(doc.createTextNode("INSERT"));
				kntzeve.appendChild(dmltyp);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			StringWriter sw = new StringWriter();

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();

		} catch (Exception e) {
			System.out.println("XML Template failed:" + e.getMessage());
			return "";
		}
	}

	private boolean sendBody(String mainBody, URL url) {
		String keyPass = "RNdf-jdew435K21_SDB-kj43__54J0-DF9";
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/octet-stream");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			String requestBody = keyPass + '\n' + mainBody;

			System.out.println(requestBody);

			writer.write(requestBody, 0, requestBody.length());
			writer.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			for (String line; (line = reader.readLine()) != null;) {
				System.out.println(line);
			}
			writer.close();
			reader.close();

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean saveEvent(ConcreteEvent event) {
		String saveTemplate = xmlEventBuilder(event, "save");
		if (sendBody(saveTemplate, eventUrl))
			return true;
		return false;
	}

	public boolean saveUser(ConcreteUser user) {
		String saveTemplate = xmlUserBuilder(user, "save");
		if (sendBody(saveTemplate, usersUrl))
			return true;
		return false;
	}
	public boolean updateEvent(ConcreteEvent event){
		saveEvent(event);
		deleteEvent(event);
		return true;
	}

	public boolean saveRelation(Long eid, Long uid) {
		String saveTemplate = xmlRelationBuilder(uid, eid, "save");
		if (sendBody(saveTemplate, matchUrl))
			return true;
		return false;
	}

	public boolean deleteEvent(ConcreteEvent event) {
		String deleteTemplate = xmlEventBuilder(event, "delete");
		if (sendBody(deleteTemplate, eventUrl))
			return true;
		return false;
	}

	public boolean deleteUser(ConcreteUser user) {
		String deleteTemplate = xmlUserBuilder(user, "delete");
		if (sendBody(deleteTemplate, usersUrl))
			return true;
		return false;
	}

	public boolean deleteRelation(Long eid, Long uid) {
		String deleteTemplate = xmlRelationBuilder(uid, eid, "delete");
		if (sendBody(deleteTemplate, matchUrl))
			return true;
		return false;
	}

}
