package eventTicker.layoutComponents.menu;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Window;

import eventTicker.backend.AccountingSessionKeys;
import eventTicker.backend.AppSynchronizer;

public abstract class AbstractMenu extends HorizontalLayout implements Component {
	protected MenuBar barmenu = new MenuBar();
	protected AccountingSessionKeys keys = AccountingSessionKeys.getInstance();
	protected AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();

	protected boolean sessionValidator() {
		try {
			Object etname = getSession().getCurrent().getAttribute("etNAME");
			System.out.println(etname);
			Object etkey = getSession().getCurrent().getAttribute("etKEY");
			System.out.println(etkey);
			if (etname != null && etkey != null) {
				boolean authValid = keys.authenticate(etkey.toString());
				System.out.println("authsucced"+authValid);
				return authValid;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public AbstractMenu() {
		init();
	}

	protected abstract void init();

	protected void openModal(Component c) {
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

}
