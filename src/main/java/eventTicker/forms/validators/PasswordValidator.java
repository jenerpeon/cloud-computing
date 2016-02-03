package eventTicker.forms.validators;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.PasswordField;

import eventTicker.backend.AppSynchronizer;
import eventTicker.backend.ConcreteUser;

public class PasswordValidator implements Validator {
	AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();
	String username;

	public PasswordValidator(String username) {
		this.username = username;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		System.out.println(value);
		if (!(value instanceof String))
			throw new InvalidValueException("unknown type: expected PasswordField");
		if (!matchUserPass((String) value))
			throw new InvalidValueException("wrong Password");
	}

	private boolean matchUserPass(String pass) {
		System.out.println("username:"+username);
		ConcreteUser user = sync.findByUserName(username);
		System.out.println(user);
		if (user == null)
			throw new InvalidValueException("wrong Password");

		if (user.getPassword().equals(pass))
			return true;
		return true;
	}
}
