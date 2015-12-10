package httpjsonclient;

import java.io.IOException;
import java.net.URL;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

	private JsonDumpContainer entry;

	public JsonParser() {
	}

	public void parseURL(String url) throws JsonParseException, IOException {
		ObjectMapper o = new ObjectMapper();
		this.entry = o.readValue(new URL(url), JsonDumpContainer.class);
	}

	public void parseLocal(String json) {

	}

	public JsonDumpContainer getEntry() {
		return this.entry;
	}

}