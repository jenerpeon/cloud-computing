package eventTicker.forms;

import java.util.Date;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eventTicker.backend.AppSynchronizer;
import eventTicker.backend.ConcreteUser;
import eventTicker.forms.validators.PasswordEqualsValidator;
import eventTicker.forms.validators.UniqueUserValidator;
import eventTicker.forms.validators.UserExistsValidator;

public class RegistrationForm extends FormLayout {
	private AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();

	private final TextField weblogin = new TextField("Benutzername:");
	private final TextField firstName = new TextField("Vorname");
	private final TextField name = new TextField("Nachname");
	private final DateField birthDate = new DateField("Geburtstag");
	private final PasswordField password = new PasswordField("Passwort:");
	private final PasswordField passwordValid = new PasswordField("Passwort:");

	private final Button register = new Button("Registrieren", this::save);
	private final Button cancel = new Button("Abbrechen", this::cancel);

	private ConcreteUser cuser;
	private BeanFieldGroup<ConcreteUser> fieldGroup;

	public RegistrationForm(ConcreteUser user) {
		cuser = user;
		setBeanBindings();
		fieldValidation();
		configureComponents();
		buildLayout();
	}

	private void configureComponents() {
		register.setStyleName(ValoTheme.BUTTON_PRIMARY);
		register.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		birthDate.setValue(new Date());
	}

	private void buildLayout() {
		setMargin(true);
		setSizeUndefined();
		HorizontalLayout actions = new HorizontalLayout(register, cancel);
		actions.setSpacing(true);
		addComponents(weblogin, firstName, name, birthDate, password, passwordValid, actions);
	}

	private void setBeanBindings() {
		fieldGroup = new BeanFieldGroup<ConcreteUser>(ConcreteUser.class);
		fieldGroup.buildAndBindMemberFields(this);
		fieldGroup.setItemDataSource(cuser);
	}

	private void fieldValidation() {
	}

	private void cancel(Button.ClickEvent event) {
		((Window) this.getParent()).close();
	}

	private void save(Button.ClickEvent event) {
		try {
			System.out.println("pw:" + password.getValue());
			fieldGroup.commit();
			System.out.println("saving:" + cuser);
			sync.saveUser(cuser);
		} catch (CommitException e) {
			passwordValid.setValidationVisible(true);
			System.out.println("save user transaction failed:" + e.getMessage());
		}

	}

}
