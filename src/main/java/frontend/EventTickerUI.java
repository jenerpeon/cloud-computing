package frontend;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import backend.ConcreteEvent;
import backend.ConcreteUser;
import repositories.EventRepository;
import repositories.UserRepository;

@Title("EventTicker")
@Theme("valo")
@ViewScope
@SpringUI(path="/")
public class EventTickerUI extends UI {
	
	private final EventRepository eventRepo;
    private final UserRepository userRepo;
	TextField filter = new TextField();
	Button newContact = new Button("New contact");
	Grid contactList = new Grid();
	Grid eventList = new Grid();

    @Autowired
    private SpringViewProvider viewProvider;
	EventForm contactForm;
	UserForm userForm;
//	SimpleForm simpleForm = new SimpleForm();

//	private final ContactService service;// = ContactService.createDemoServiceInstance();
    @Autowired
	public EventTickerUI(EventRepository eventRepo, UserRepository userRepo){
    	this.eventRepo = eventRepo;
    	this.userRepo = userRepo;
//		this.service = ContactService.createDemoServiceInstance();
	}
	@Override
	protected void init(VaadinRequest request) {
		// fetchData();
		// service.reFetch();
//		contactForm = new EventForm(this.service);
//		userForm = new UserForm(this.service);
		configureComponents();
		buildLayout();
	}

	private void configureComponents() {
//		newContact.addClickListener(e -> contactForm.edit(new Participant()));

		filter.setInputPrompt("Filter contacts...");
//		filter.addTextChangeListener(e -> refreshEvents(e.getText()));

		eventList.setContainerDataSource(new BeanItemContainer<>(ConcreteEvent.class));
		eventList.removeColumn("description");
		eventList.removeColumn("length");
		eventList.removeColumn("width");
		eventList.setColumnOrder("id", "name", "date");
		eventList.setSelectionMode(Grid.SelectionMode.SINGLE);
//		eventList.addSelectionListener(e -> refreshUsers((ConcreteEvent) eventList.getSelectedRow()));
//		eventList.addSelectionListener(e -> addUsers((ConcreteEvent) eventList.getSelectedRow()));

		contactList.setContainerDataSource(new BeanItemContainer<>(ConcreteUser.class));
		contactList.removeAllColumns();
		contactList.addColumn("firstName");
		contactList.addColumn("name");
		//rm.save((Participant)
				// contactList.getSelectedRow()));
		contactList.setSelectionMode(Grid.SelectionMode.SINGLE);
//		contactList.addSelectionListener(e -> userForm.edit((Participant) contactList.getSelectedRow()));

		refreshContacts();

	}

	private void buildLayout() {
		HorizontalLayout actions = new HorizontalLayout(filter, filter);
		actions.setWidth("100%");
		actions.setSizeFull();
		filter.setWidth("100%");
		filter.setSizeFull();

//		HorizontalLayout Top = new HorizontalLayout(contactForm, userForm);
//		Top.setSizeFull();
//		contactForm.setWidth("100%");
//		Top.setWidth("100%");

		HorizontalLayout Bottom = new HorizontalLayout(eventList, contactList);
		eventList.setSizeFull();
		contactList.setSizeFull();
		eventList.setWidth("100%");
		contactList.setWidth("100%");
		Bottom.setSizeFull();
		Bottom.setWidth("100%");

		VerticalLayout main = new VerticalLayout(actions, Bottom);

		setContent(main);
	}

	private void addUsers(ConcreteEvent concreteEvent){
		this.userRepo.save(new ConcreteUser("dicker","peon"));
		System.out.println(userRepo.findAll().toString());
        userForm.setVisible(true);
        ConcreteUser p = new ConcreteUser();
        userForm.edit(p);
//        service.addEvent2user(concreteEvent.getId(), p.getId());
       	}

	private void refreshUsers(ConcreteEvent concreteEvent) {
/*		contactList.setContainerDataSource(
				new BeanItemContainer<>(ConcreteUser.class, service.findUsersByEId(concreteEvent.getId())));
		userForm.setVisible(true);
		contactList.setVisible(true);
		contactForm.setVisible(true);
*/
	}

	void refreshContacts() {
		refreshEvents(filter.getValue());
	}

	private void refreshEvents(String stringFilter) {
//		eventList.setContainerDataSource(new BeanItemContainer<>(ConcreteEvent.class, service.findEvents()));
		contactList.setVisible(false);
		contactForm.setVisible(false);
	}
//
//	public void fetchData() {
//		JsonParser p = new JsonParser();
//		List<Participant> users = p.fetchUsers();
//		List<ConcreteEvent> events = p.fetchEntries();
//		// Map<Long,Long> participants = p.fetchRelated();
//
//		for (Participant user : users) {
//			service.saveUser(user);
//		}
//		for (ConcreteEvent event : events) {
//			service.saveEvent(event);
//		}
//
//	}

	@WebServlet(urlPatterns = "/*")
	@VaadinServletConfiguration(ui = EventTickerUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}

}
