package eventTicker.forms.validators;

import com.vaadin.data.Validator;

import eventTicker.backend.AppSynchronizer;

public class UniqueUserValidator implements Validator{
	AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();

	public UniqueUserValidator() {
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if(!(value instanceof String))
			throw new InvalidValueException("unknown type: expected String");
		if (matchUser((String)value))
			throw new InvalidValueException("User with this Name alredy exists");
	}

	private boolean matchUser(String username){
		System.out.println("test existing uname");
	    if(sync.findByUserName(username)!=null)
	    	return true;
	    return false;
	}
}
