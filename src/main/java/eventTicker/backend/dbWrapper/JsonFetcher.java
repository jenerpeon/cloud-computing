package eventTicker.backend.dbWrapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eventTicker.backend.ConcreteEvent;
import eventTicker.backend.ConcreteUser;

public class JsonFetcher extends DbSynchronizer {

	private static JsonFetcher jsonFetcher;

	private static List<ConcreteEvent> eventContainer = new ArrayList<ConcreteEvent>();
	private static Map<Long, List<Long>> participants = new HashMap<Long, List<Long>>();
	private static List<ConcreteUser> userContainer = new ArrayList<ConcreteUser>();

	public static JsonFetcher getInstance() {
		if (jsonFetcher == null)
			jsonFetcher = new JsonFetcher();
		return jsonFetcher;
	}

	public JsonFetcher() {
	}

	public List<ConcreteEvent> fetchEvents() {
		ObjectMapper o = new ObjectMapper();
		try {
			JsonNode node = HttpResponse(eventUrl);
			JsonNode refArray = node.get("next");
			JsonNode refNode = refArray.get("$ref");
			String ref = refNode.asText();
			JsonNode itemArray = node.get("items");
			eventContainer = new ArrayList<ConcreteEvent>();

			int j = itemArray.size();
			for (int i = 0; i < j; i++) {
				ConcreteEvent event = new ConcreteEvent();
				JsonNode item = itemArray.get(i);

				JsonNode status = item.get("status");

				event.setId(item.get("id").asLong());
				event.setName(item.get("name").asText());
				event.setLength(item.get("laenge").asDouble());
				event.setWidth(item.get("breite").asDouble());
				event.setDate(item.get("datum").asText());
				event.setDescription(item.get("beschreibung").asText());

				if (status != null && status.asText().equals("D".toString())) {
					deletedEventContainer.addBean(event);
				} else {
					eventContainer.add(event);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return eventContainer;
	}

	public Map<Long, List<Long>> fetchRelations() {
		List<Long> array;
		ObjectMapper o = new ObjectMapper();
		try {

			JsonNode node = HttpResponse(matchUrl);
			JsonNode refArray = node.get("next");
			JsonNode refNode = refArray.get("$ref");
			String ref = refNode.asText();
			JsonNode itemArray = node.get("items");

			participants = new HashMap<Long, List<Long>>();

			for (int i = 0; i < itemArray.size(); i++) {
				JsonNode item = itemArray.get(i);

				Long u_id = item.get("ntz_id").asLong();
				Long e_id = item.get("eve_id").asLong();

				if (!participants.containsKey(e_id)) {
					array = new LinkedList<Long>();
					participants.put(e_id, array);
				}
				participants.get(e_id).add(u_id);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return participants;

	}

	private JsonNode HttpResponse(URL url) throws JsonProcessingException, IOException {
		HttpURLConnection conn = null;
		JsonNode node = null;
		ObjectMapper o = new ObjectMapper();
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.addRequestProperty("PASS", "48fhGGDo3ksss92Lk04saabAdS");
		conn.setDoOutput(true);

		node = o.readTree(conn.getInputStream());
		return node;
	}

	public List<ConcreteUser> fetchUsers() {
		JsonNode node = null;
		try {
			node = HttpResponse(usersUrl);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonNode refArray = node.get("next");
		JsonNode refNode = refArray.get("$ref");
		String ref = refNode.asText();
		JsonNode itemArray = node.get("items");

		userContainer = new ArrayList<ConcreteUser>();

		for (int i = 0; i < itemArray.size(); i++) {
			ConcreteUser user = new ConcreteUser();
			JsonNode item = itemArray.get(i);

			JsonNode status = item.get("status");

			user.setId(item.get("id").asLong());
			user.setFirstName(item.get("vorname").asText());
			user.setName(item.get("name").asText());
			user.setLength(item.get("laenge").asDouble());
			user.setWidth(item.get("breite").asDouble());
			user.setWeblogin(item.get("weblogin").toString());
			user.setPassword(item.get("passwort").toString());
			user.setBirthDate(item.get("geburtsdatum").toString());

			if (status != null && status.asText().equals("D")) {
			} else {
				userContainer.add(user);
			}

		}
		return userContainer;
	}
}
