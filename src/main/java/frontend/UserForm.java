package frontend;

import com.vaadin.data.fieldgroup.BeanFieldGroup;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import backend.ContactService;
import backend.ConcreteUser;
public class UserForm extends FormLayout {

	Button save = new Button("Save", this::save);
	Button cancel = new Button("Cancel", this::cancel);
	// TextField id = new TextField("id");

//	FieldGroup fieldGroup = new BeanFieldGroup<Participant>(Participant.class);
//	fieldGroup.setItemDataSource(new BeanItem<Participant>(new Participant()));
	@PropertyId("firstName")
	TextField firstName = new TextField("vorname");
	@PropertyId("name")
	TextField name = new TextField("name");
//	
//	 * TextField width = new TextField("breite"); TextField length = new
//	 * TextField("laenge")
//	 
	// TextField birthDate = new TextField("birth");

	@PropertyId("weblogin")
	TextField weblogin = new TextField("weblogin");
	@PropertyId("password")
	TextField password = new TextField("password");

	private final ContactService service;
	ConcreteUser contact;
	BeanFieldGroup<ConcreteUser> formFieldBindings;

	public UserForm(ContactService service) {
		this.service = service.createDemoServiceInstance();
		formFieldBindings = new BeanFieldGroup<ConcreteUser>(ConcreteUser.class);

		configureComponents();
		buildLayout();
	}

	private void configureComponents() {
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		setVisible(false);
	}

	private void buildLayout() {
		setSizeUndefined();
		setMargin(true);

		HorizontalLayout actions = new HorizontalLayout(save, cancel);
		actions.setSpacing(true);
		addComponents(actions, firstName, name, weblogin, password);
	}

	public void save(Button.ClickEvent event) {
        try {
            // Commit the fields from UI to DAO
            formFieldBindings.commit();

            // Save DAO to backend with direct synchronous service API
            service.saveUser(contact);

            String msg = String.format("Saved '%s %s'.",
                    contact.getFirstName(),
                    contact.getName());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
//            getUI().refreshContacts();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        	System.out.println(e.getInvalidFields().toString());
        }
    }
/*		contact = new Participant();
		contact.setFirstName(firstName.toString());
		String value = 
//		contact.setName(name);
//		ContactService.saveUser(contact);
//		System.out.println(contact.getName()+contact.getFirstName());
	*/	
	
//		System.out.println(formFieldBindings.getFields());
		// System.out.println(formFieldBindings.getBoundPropertyIds());
		/*
		 * try { // formFieldBindings.commit(); } catch (CommitException e) {
		 * System.out.println("#########" + e.getInvalidFields().toString()); }
		 */

	public void cancel(Button.ClickEvent event) {
		// Place to call business logic.
		Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
		getUI().contactList.select(null);
	}

	void edit(ConcreteUser contact) {
		this.contact = contact;
		if (contact != null) {
			// Bind the properties of the contact POJO to fiels in this form
			formFieldBindings = BeanFieldGroup.bindFieldsBuffered(contact, this);
			firstName.focus();
		}
		setVisible(contact != null);
	}

	@Override
	public EventTickerUI getUI() {
		return (EventTickerUI) super.getUI();
	}

}
