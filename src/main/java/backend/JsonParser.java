package backend;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

	// private JsonDumpContainer entry;
	private String events;
	private String users;
	private String match;

	public JsonParser() {
		this.events = "https://apex.oracle.com/pls/apex/tudws/eventmanagement/events";
		this.users = "https://apex.oracle.com/pls/apex/tudws/eventmanagement/nutzer";
		this.match = "https://apex.oracle.com/pls/apex/tudws/eventmanagement/teilnahmen";
	}

	public List<ConcreteEvent> fetchEntries() {
		List<ConcreteEvent> entries = new ArrayList<ConcreteEvent>();
		ObjectMapper o = new ObjectMapper();
		try {
			JsonNode node = o.readValue(new URL(events), JsonNode.class);
			o.toString();
			JsonNode refArray = node.get("next");
			JsonNode refNode = refArray.get("$ref");
			String ref = refNode.asText();
			JsonNode itemArray = node.get("items");

			for (int i = 0; i < itemArray.size(); i++) {
				ConcreteEvent event = new ConcreteEvent();
				JsonNode item = itemArray.get(i);

				event.setId(item.get("id").asLong());
				event.setName(item.get("name").asText());
				event.setLength(item.get("laenge").asDouble());
				event.setWidth(item.get("breite").asDouble());
				event.setDate(item.get("datum").asText());
				event.setDescription(item.get("beschreibung").asText());

				System.out.println(event.toString());
				entries.add(event);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entries;
	}

	public Map<Long, List<Long>> fetchRelated() {
		Map<Long, List<Long>> participants = new HashMap<Long, List<Long>>();
		List<Long> array;
		ObjectMapper o = new ObjectMapper();
		try {
			JsonNode node = o.readValue(new URL(match), JsonNode.class);
			JsonNode refArray = node.get("next");
			JsonNode refNode = refArray.get("$ref");
			String ref = refNode.asText();
			JsonNode itemArray = node.get("items");

			for (int i = 0; i < itemArray.size(); i++) {
				JsonNode item = itemArray.get(i);

				Long u_id = item.get("ntz_id").asLong();
				Long e_id = item.get("eve_id").asLong();

				if(!participants.containsKey(e_id)){
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

	public List<ConcreteUser> fetchUsers() {
		List<ConcreteUser> userList = new ArrayList<ConcreteUser>();
		ObjectMapper o = new ObjectMapper();
		try {
			JsonNode node = o.readValue(new URL(users), JsonNode.class);
			JsonNode refArray = node.get("next");
			JsonNode refNode = refArray.get("$ref");
			String ref = refNode.asText();
			JsonNode itemArray = node.get("items");

			for (int i = 0; i < itemArray.size(); i++) {
				ConcreteUser user = new ConcreteUser();
				JsonNode item = itemArray.get(i);

				user.setId(item.get("id").asLong());
				user.setFirstName(item.get("vorname").asText());
				user.setName(item.get("name").asText());
				user.setLength(item.get("laenge").asDouble());
				user.setWidth(item.get("breite").asDouble());
				user.setWeblogin(item.get("weblogin").toString());
				user.setPassword(item.get("passwort").toString());
				user.setBirthDate(item.get("geburtsdatum").toString());

				System.out.println(user.toString());
				userList.add(user);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return userList;
	}

}