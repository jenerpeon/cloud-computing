package eventTicker.backend;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;

import eventTicker.backend.dbWrapper.DbSynchronizer;
import eventTicker.backend.dbWrapper.JsonFetcher;
import eventTicker.backend.dbWrapper.XmlSender;

public class AppSynchronizer {

	private DbSynchronizer dbSyncer = DbSynchronizer.getInstance();
	private static JsonFetcher jsonFetcher = JsonFetcher.getInstance();
	private XmlSender xmlSender = XmlSender.getInstance();

	private static AppSynchronizer synchronizerInstance = new AppSynchronizer();
	public static BeanItemContainer eventContainer = new BeanItemContainer<>(ConcreteEvent.class);
	public static BeanItemContainer userContainer = new BeanItemContainer<>(ConcreteUser.class);
	public static Map<Long, List<Long>> related;
	public static List<ConcreteEvent> eventListContainer;
	public static List<ConcreteUser> userListContainer;
	public long eventCount = 0;
	public long userCount = 0;
	private Collection<Filter> filtersEvents;
	private Collection<Filter> filtersUsers;

	public static AppSynchronizer getSynchronizerInstance() {
		if (synchronizerInstance == null) {
			synchronizerInstance = new AppSynchronizer();
		}

		return synchronizerInstance;
	}

	private AppSynchronizer() {

	}

	public BeanItemContainer getEventContainer() {
		return eventContainer;
	}

	public BeanItemContainer getUserContainer() {
		return userContainer;
	}

	public Map<Long, List<Long>> getRelated() {
		return related;
	}

	public void clearUserContainer() {
		filtersUsers = userContainer.getContainerFilters();
		userContainer.removeAllContainerFilters();

	}

	public void clearEventContainer() {
		filtersEvents = eventContainer.getContainerFilters();
		eventContainer.removeAllContainerFilters();
	}

	public void restoreUserContainer() {
		try {
			for (Filter f : filtersUsers)
				userContainer.addContainerFilter(f);
		} catch (Exception e) {
		}
	}

	public void restoreEventContainer() {
		try {
			for (Filter f : filtersEvents)
				eventContainer.addContainerFilter(f);
		} catch (Exception e) {
		}

	}

	// Uncomment for Fetching user in Event data. This doesent seem necessary
	public void saveRelation(Long uid, Long eid) {
		related.get(eid).add(uid);
		xmlSender.saveRelation(eid, uid);
	}

	public void saveEvent(ConcreteEvent event) {
		event.setId((long) 999);
		eventListContainer.add(event);
		xmlSender.saveEvent(event);
	}

	public void updateEvent(ConcreteEvent event) {
		xmlSender.updateEvent(event);
	}

	public void saveUser(ConcreteUser user) {
		xmlSender.saveUser(user);
	}

	public void deleteEvent(ConcreteEvent event) {
		eventListContainer.remove(event);
		xmlSender.deleteEvent(event);
	}

	public void deleteUser(ConcreteUser user) {
		xmlSender.deleteUser(user);
	}

	public void deleteRealation(Long uid, Long eid) {
		related.get(eid).remove(uid);
		xmlSender.deleteRelation(eid, uid);
	}

	public ConcreteUser findByUserName(String uname) {
		uname = uname.replace("\"", "");
		ConcreteUser user = null;
		this.clearUserContainer();
		for (Object id : userContainer.getItemIds()) {
			user = (ConcreteUser) userContainer.getItem(id).getBean();
			if (user.getWeblogin().toLowerCase().replace("\"", "").equals(uname.toLowerCase())) {
				return user;
			}
		}
		this.restoreUserContainer();
		return user;
	}

}