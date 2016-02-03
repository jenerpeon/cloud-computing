package eventTicker.forms.validators;

import com.vaadin.data.Validator;

import eventTicker.backend.AppSynchronizer;

public class UserExistsValidator implements Validator{
	AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();

	public UserExistsValidator() {
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if(!(value instanceof String))
			throw new InvalidValueException("unknown type: expected String");
		if (!matchUser((String)value))
			throw new InvalidValueException("Invalid User");
	}

	private boolean matchUser(String username){
		System.out.println("test existing uname");
	    if(sync.findByUserName(username)==null)
	    	return false;
	    return true;
	}
}
