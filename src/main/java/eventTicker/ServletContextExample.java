package eventTicker;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import eventTicker.backend.AppSynchronizer;
import eventTicker.backend.dbWrapper.JsonFetcher;

public class ServletContextExample implements ServletContextListener{
	AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();
	JsonFetcher jsonFetcher = JsonFetcher.getInstance();

	ServletContext context;
	public void contextInitialized(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				sync.eventListContainer = jsonFetcher.fetchEvents();
				sync.userListContainer = jsonFetcher.fetchUsers();
				sync.related = jsonFetcher.fetchRelations();
				sync.eventContainer.removeAllItems();
				sync.userContainer.removeAllItems();
				sync.eventContainer.addAll(sync.eventListContainer);
				sync.userContainer.addAll(sync.userListContainer);
				// related = relatedSync;
			}
		}, 0, 10000);
		//Do stuff on Context creation
	}
	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		//Do stuff on ContextDestruction
	}
}