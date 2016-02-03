package eventTicker.forms;

import java.util.Date;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import eventTicker.backend.AppSynchronizer;
import eventTicker.backend.ConcreteEvent;
import eventTicker.backend.ConcreteUser;
import eventTicker.views.UserView;

public class UserForm extends Window implements View, Button.ClickListener {
	public static final String NAME = "eventForm";
	private AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();

	private final TextField name;
	private final TextField width;
	private final TextField length;
	private final TextField firstName;

	private final DateField birthDate;
	private final TextField weblogin;

	private final Button createButton;
	private final Button cancelButton;

	private ConcreteUser user;

	public UserForm() {
		user = new ConcreteUser();

		setSizeFull();
		name = new TextField("name");
		firstName = new TextField("vorname");
		weblogin = new TextField("weblogin");
		weblogin.setValue("none");
		birthDate = new DateField();
		birthDate.setValue(new Date());
		// Coordinate fields
		width = new TextField("X Coordinate:");
		width.setValue("13.74687679111048");
		length = new TextField("Y Coordinate:");
		length.setValue("51.0624915630149");

		// Create and cancel buttons
		createButton = new Button("Create", this);
		cancelButton = new Button("Cancel", this);

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(firstName, name, birthDate, width, length, weblogin);

		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		HorizontalLayout buttons = new HorizontalLayout(cancelButton, createButton);
		// The view root layout
		VerticalLayout viewLayout = new VerticalLayout(fields, buttons);
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		viewLayout.setMargin(true);
		viewLayout.setWidth("400px");

		this.setWidth("500px");
		this.setHeight("500px");
		this.setSizeFull();
		this.setModal(true);
		this.setContent(viewLayout);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// focus the username field when user arrives to the login view
		name.focus();
	}

	protected void onCreateClicked() {
		try {
			user.setLength(Double.parseDouble(length.getValue()));
			user.setWidth(Double.parseDouble(width.getValue()));
			user.setBirthDate(birthDate.getValue().toString());
			user.setWeblogin(weblogin.getValue().toString());
			user.setName(name.getValue());
			user.setFirstName(firstName.getValue());
		} catch (Exception e) {
			Notification.show("invalid input data");
			return;
		}

		sync.saveUser(user);

		Notification.show("event created" + user.getName() + '\n' + user.getBirthDate() + '\n' + user.getLength()
				+ user.getWidth());
	}

	@Override
	public void buttonClick(ClickEvent event) {
		// on create
		if (event.getButton().equals(createButton)) {
			onCreateClicked();
		}
		// on cancel
		else {
			Notification.show("Creation canceled");
		}
		getUI().getNavigator().navigateTo(UserView.NAME);

		this.close();
	}

}
