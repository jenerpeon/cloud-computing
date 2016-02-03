package eventTicker.views;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;

import eventTicker.backend.ConcreteEvent;
import eventTicker.backend.ConcreteUser;
import eventTicker.layoutComponents.StyleTestMap;
import eventTicker.layoutComponents.menu.NavMenu;

@Theme("valo")
public class GuestView extends AbstractOverview {
	public static final String NAME = "guest";

	@Override
	protected void concreteConfiguration() {
	}
}
