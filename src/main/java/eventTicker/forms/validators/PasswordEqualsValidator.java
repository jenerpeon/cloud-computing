package eventTicker.forms.validators;

import com.vaadin.data.Validator;
import com.vaadin.ui.PasswordField;

public class PasswordEqualsValidator implements Validator {
	PasswordField pass;
	PasswordField rePass;

	public PasswordEqualsValidator(PasswordField pass, PasswordField rePass) {
		pass = pass;
		rePass = rePass;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!isValid(value))
			throw new InvalidValueException("Passwoerter stimmen nicht Ueberein");

	}

	public boolean isValid(Object value) {
		System.out.println("pw1:"+pass+"pw2:"+rePass);
		if (!(pass != null && rePass != null))
			return false;
		if (pass.equals(rePass))
			return true;
		return false;
	}

}
