package frontend;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import backend.ContactService;
import backend.ConcreteUser;

/* Create custom UI Components.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from VerticalLayout. Use
 * Use BeanFieldGroup to bind data fields from DTO to UI fields.
 * Similarly named field by naming convention or customized
 * with @PropertyId annotation.
 */
public class EventForm extends FormLayout {
	// {"id":1,"name":"This is a test entry for our lovely
	// Web-Service.","beschreibung":"This is a test entry for our lovely
	// Web-Service. This is a test entry for our lovely Web-Service. This is a
	// test entry for our lovely Web-Service. This is a test entry for our
	// lovely Web-Service. This is a test entry for our lovely Web-Service. This
	// is a test entry for our lovely Web-Service. This is a test entry for our
	// lovely Web-Service. This is a test entry for our lovely Web-Service. This
	// is a test entry for our lovely Web-Service. This is a test entry for our
	// lovely Web-Service. This is a test entry for our lovely Web-Service. This
	// is a test entry for our lovely Web-Service. This is a test entry for our
	// lovely Web-Service. This is a test entry for our lovely
	// Web-Service.","datum":"2015-11-18T17:58:00Z","breite":51.435345,"laenge":23.435435,"status":"V"}
	/*
	 * private Long id; private String name = ""; private String description =
	 * ""; private double width = 0; private double length = 0; private String
	 * date;
	 */

	private final ContactService service;
	Button save = new Button("Save", this::save);
	Button cancel = new Button("Cancel", this::cancel);

	TextField name = new TextField("name");
	TextField description = new TextField("description");

	TextField length = new TextField("length");

	TextField width = new TextField("breite");
	DateField birth = new DateField("birth");
	TextField weblogin = new TextField("weblogin");
	TextField password = new TextField("password");

	ConcreteUser contact;

	// Easily bind forms to beans and manage validation and buffering
	BeanFieldGroup<ConcreteUser> formFieldBindings;



	@Autowired
	public EventForm(ContactService service) {
		this.service = service.createDemoServiceInstance();

		configureComponents();
		buildLayout();
		length.setConverter(Integer.class);
	}

	private void configureComponents() {
		/*
		 * Highlight primary actions.
		 *
		 * With Vaadin built-in styles you can highlight the primary save button
		 * and give it a keyboard shortcut for a better UX.
		 */
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		setVisible(false);
	}

	private void buildLayout() {
		setSizeUndefined();
		setMargin(true);

		HorizontalLayout actions = new HorizontalLayout(save, cancel);
		actions.setSpacing(true);

		addComponents(actions, name, weblogin, birth);
	}

	public void save(Button.ClickEvent event) {
		try {
			// Commit the fields from UI to DAO
			formFieldBindings.commit();

			// Save DAO to backend with direct synchronous service API
			service.saveUser(contact);

			String msg = String.format("Saved '%s %s'.", contact.getFirstName(), contact.getName());
			Notification.show(msg, Type.TRAY_NOTIFICATION);
//			getUI().refreshContacts();
		} catch (FieldGroup.CommitException e) {
			// Validation exceptions could be shown here
			System.out.println(e.getInvalidFields().toString());
		}
	}

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
		}
		setVisible(contact != null);
	}

	@Override
	public EventTickerUI getUI() {
		return (EventTickerUI) super.getUI();
	}

}
