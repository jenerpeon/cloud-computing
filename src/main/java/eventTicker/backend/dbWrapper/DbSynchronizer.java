package eventTicker.backend.dbWrapper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.util.BeanItemContainer;

import eventTicker.backend.ConcreteEvent;
import eventTicker.backend.ConcreteUser;

public class DbSynchronizer {

	private static DbSynchronizer jsonParser = new DbSynchronizer();

	private XmlSender xmlSender;
	private JsonFetcher jsonFetcher;

	public static String events = "https://apex.oracle.com/pls/apex/tudws/eventmanagement/events";
	public static String nutzer = "https://apex.oracle.com/pls/apex/tudws/eventmanagement/nutzer";
	public static String teilnahmen = "https://apex.oracle.com/pls/apex/tudws/eventmanagement/teilnahmen";

	// get singleton Instance
	public static DbSynchronizer getInstance() {
		if (jsonParser == null)
			jsonParser = new DbSynchronizer();

		return jsonParser;
	}

	// Http Header Variables
	Map<String, String> getHeaders;
	Map<String, String> postHeaders;

	protected String eventJson = "";
	protected String userJson= "";
	protected String relationJson="";

	protected URL eventUrl;
	protected URL usersUrl;
	protected URL matchUrl;

	protected BeanItemContainer eventContainer = new BeanItemContainer<>(ConcreteEvent.class);
	protected BeanItemContainer userContainer = new BeanItemContainer<>(ConcreteUser.class);

	public static BeanItemContainer deletedEventContainer = new BeanItemContainer<>(ConcreteEvent.class);
	protected BeanItemContainer deletedUserContainer = new BeanItemContainer<>(ConcreteUser.class);

	public DbSynchronizer() {

		try {
			this.eventUrl = new URL("https://apex.oracle.com/pls/apex/tudws/eventmanagement/events");
			this.usersUrl = new URL("https://apex.oracle.com/pls/apex/tudws/eventmanagement/nutzer");
			this.matchUrl = new URL("https://apex.oracle.com/pls/apex/tudws/eventmanagement/teilnahmen");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getHeaders = new HashMap<String, String>();
		getHeaders.put("accept", "application/json");
		getHeaders.put("PASS", "48fhGGDo3ksss92Lk04saabAdS");

		postHeaders = new HashMap<String, String>();
		postHeaders.put("User-Agent", "USER_AGENT");
		postHeaders.put("Content-Type", "application/octet-stream");
	}

	public XmlSender getXmlSender() {
		return this.xmlSender;
	}

	public JsonFetcher getJsonFetcher() {
		return this.jsonFetcher;
	}

}