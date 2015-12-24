package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

public class ContactService {

	private static Map<Long, List<Long>> event2user;
	private static List<ConcreteEvent> eventList;
	private static List<ConcreteUser> userList;
	// Further Implementation of synchronization optimization will rock
	/*
	 * private static List<ConcreteEvent> newEvents; private static
	 * List<ConcreteEvent> removeEvents; private static List<ConcreteEvent>
	 * modifiedEvents;
	 */
	/*
	 * private static List<Participant> newUsers; private static
	 * List<Participant> removeUsers; private static List<Participant>
	 * modifiedUsers;
	 */
	private static ContactService instance;
	

	private ContactService() {
		dataStructInit();
		fetch();
	}

	public static ContactService createDemoServiceInstance() {
		if (instance == null) {

			final ContactService contactService = new ContactService();
			instance = contactService;
		}
		return instance;
	}

	private static void dataStructInit() {
		eventList = new LinkedList<ConcreteEvent>();
		userList = new LinkedList<ConcreteUser>();
	}

	public static synchronized void fetch() {
		JsonParser p = new JsonParser();

		for (ConcreteUser user : p.fetchUsers()) {
			userList.add(user);
			instance.saveUser(user);
		}
		for (ConcreteEvent eve : p.fetchEntries()) {
			eventList.add(eve);
			instance.saveEvent(eve);
		}
		event2user = p.fetchRelated();

	}

	private static HashMap<Long, ConcreteUser> contacts = new HashMap<>();
	private static HashMap<Long, ConcreteEvent> events = new HashMap<>();
	
	public synchronized void addEvent2user(Long uid, Long eid){
		if(event2user.containsKey(eid)){
			event2user.get(eid).add(uid);
			return;
		}
		return;
	}

	public synchronized List<ConcreteEvent> findEvents() {
		return new ArrayList<ConcreteEvent>(events.values());
	}

	public synchronized List<ConcreteUser> findUsers() {
		return new ArrayList<ConcreteUser>(contacts.values());
	}

	public synchronized ConcreteEvent findEventById(Long id) {
		return events.get(id);
	}

	public synchronized ConcreteUser findUserById(Long id) {
		return contacts.get(id);
	}

	public synchronized List<ConcreteUser> findUsersByEId(Long id) {
    	List<ConcreteUser> users = new ArrayList<ConcreteUser>();
		for(Long uid : event2user.get(id)){
			users.add(findUserById(uid));
		}
		return users;
	}

	public synchronized List<ConcreteUser> findAllUsers() {
		ArrayList<ConcreteUser> p = new ArrayList<ConcreteUser>();
		p.addAll(contacts.values());
		return p;
	}

	public synchronized List<ConcreteUser> findAll(String stringFilter) {
		ArrayList<ConcreteUser> arrayList = new ArrayList<ConcreteUser>();
		for (ConcreteUser contact : contacts.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(ContactService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<ConcreteUser>() {

			@Override
			public int compare(ConcreteUser o1, ConcreteUser o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized long countEvent() {
		return events.size();
	}

	public synchronized long countUser() {
		return contacts.size();
	}

	public synchronized boolean deleteUser(ConcreteUser entry) {
		if (contacts.remove(entry.getId()) != null)
			return true;
		return false;
	}

	public synchronized boolean deleteEvent(ConcreteEvent entry) {
		if (events.remove(entry.getId()) != null)
			return true;
		return false;

	}

	public static synchronized boolean saveUser(ConcreteUser entry) {
		Long id = entry.getId();
		if (!contacts.containsKey(id)) {
			try {
				entry = (ConcreteUser) BeanUtils.cloneBean(entry);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			contacts.put(id, entry);
			return true;
		}
		return false;
	}

	public static synchronized boolean saveEvent(ConcreteEvent entry) {
		Long id = entry.getId();
		if (!events.containsKey(id)) {
			try {
				entry = (ConcreteEvent) BeanUtils.cloneBean(entry);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			events.put(id, entry);
			return true;
		}
		return false;
	}
}
