package eventTicker.forms;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eventTicker.backend.AccountingSessionKeys;
import eventTicker.backend.AppSynchronizer;
import eventTicker.backend.ConcreteUser;
import eventTicker.forms.validators.PasswordValidator;
import eventTicker.forms.validators.UserExistsValidator;
import eventTicker.views.GuestView;
import eventTicker.views.UserView;

public class LoginForm extends FormLayout {

	private AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();
	private AccountingSessionKeys accountingkeys = AccountingSessionKeys.getInstance();

	private final TextField user = new TextField("Benutzername:");
	private final PasswordField password = new PasswordField("Passwort:");
	private final Button login = new Button("Login", this::save);
	private final Button cancel = new Button("Abbrechen", this::cancel);

	private ConcreteUser cuser;

	private void configureComponents() {
		login.setStyleName(ValoTheme.BUTTON_PRIMARY);
		login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
	}

	private void cancel(Button.ClickEvent event) {
		((Window) this.getParent()).close();
	}

	private void save(Button.ClickEvent event) {
		fieldValidation();
		sync.clearUserContainer();
		cuser = sync.findByUserName(user.getValue());
		sync.restoreUserContainer();
		if (cuser == null)
			return;
		if (cuser.getId() != null && accountingkeys.isLoggedIn(cuser.getId())) {
			Notification.show("Hello:" + cuser.getWeblogin() + '\n' + "You are Allready Logged in");
			((Window) getUI().getParent()).close();
		}
		if (validateUser() && validatePassword()) {
			getUI().getSession().setAttribute("etNAME", cuser.getWeblogin());
			getUI().getSession().setAttribute("etKEY", accountingkeys.startSession(cuser.getId()));
			Notification.show(cuser.getWeblogin() + ":logged in");
			getUI().getNavigator().navigateTo(UserView.NAME);
			((Window) getParent()).close();
		} else {
			Notification.show("Wrong credentials!");
		}
	}

	private void buildLayout() {
		setMargin(true);
		setSizeUndefined();

		HorizontalLayout actions = new HorizontalLayout(login, cancel);
		actions.setSpacing(true);

		addComponents(user, password, actions);
	}

	public LoginForm() {
		configureComponents();
		buildLayout();
	}

	private void fieldValidation() {
		user.addValidator(new UserExistsValidator());
		password.addValidator(new PasswordValidator(user.getValue()));
	}

	private boolean validateUser() {
		sync.clearUserContainer();
		if (sync.findByUserName(user.getValue()) != null){
			sync.restoreUserContainer();
			return true;
		}
		sync.restoreUserContainer();
		return false;
	}

	private boolean validatePassword() {
		if (cuser != null && cuser.getPassword().replace("\"", "").equals(password.getValue()))
			return true;
		else
			return false;
	}
}
