package eventTicker.layoutComponents.menu;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window;

import eventTicker.backend.ConcreteEvent;
import eventTicker.backend.dbWrapper.DbSynchronizer;
import eventTicker.forms.EventForm;
import eventTicker.views.UserView;

public class NavMenu extends AbstractMenu implements Component {

	public NavMenu() {
		super();
	}

	public void openModal(Component c) {
		Window window = new Window();
		window.setSizeUndefined();
		window.setContent(c);
		window.setModal(true);
		window.setDraggable(false);
		window.setClosable(false);
		window.setResizable(false);

		window.setWidth(c.getWidth(), c.getWidthUnits());
		window.setHeight(c.getHeight(), c.getHeightUnits());
		getUI().addWindow(window);
	}

	@Override
	protected void init() {
		MenuBar.Command navigateCommand = new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				getUI().getNavigator().navigateTo(selectedItem.getText());
			}
		};
		MenuBar.Command refreshCommand = new MenuBar.Command() {

			public void menuSelected(MenuItem selectedItem) {
				getUI().getNavigator().navigateTo(UserView.NAME);
			}
		};
		MenuBar.Command eventCommand = new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				openModal(new EventForm());
			}
		};

		try {
			if (sessionValidator()) {
				MenuItem newEvent = barmenu.addItem("new Event", null, eventCommand);
			}
			MenuItem overview = barmenu.addItem("refresh", null, refreshCommand);
		} catch (Exception e) {

		}
		this.addComponent(barmenu);

	}

}
