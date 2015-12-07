package httpjsonclient;

import java.util.List;
import java.util.Map;

public class Entry {

	private String name;
	private Map<String,String> next;
	private List<Map> items;

	public void setNext(Map<String,String> next){
		this.next = next;
	}
	
	public void setItems(List<Map> items){
		this.items = items;
	}
	
	public String getNext(){
		return this.next.toString();
	}
	
	public String getItems(){
		return this.items.toString();
	}
	//getters and setters
}