package eventTicker.backend.containerFilters;

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class UserFilter implements Container.Filter {

	protected List<Long> users;
	protected String propertyId;

	public UserFilter(List<Long> users, String propertyId) {
		this.users = users;
		this.propertyId = propertyId;
	}

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		Property p = item.getItemProperty(propertyId);

		if (p == null || !p.getType().equals(Long.class)) {
			return false;
		}
		Long value = (Long) p.getValue();

		if (users == null)
			return false;
		for (Long uid : users) {
			if (value.equals(uid)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		return propertyId != null && propertyId.equals(this.propertyId);
	}

}
