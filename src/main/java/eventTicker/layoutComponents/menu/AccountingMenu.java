package eventTicker.layoutComponents.menu;

import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;

import eventTicker.backend.ConcreteUser;
import eventTicker.forms.LoginForm;
import eventTicker.forms.RegistrationForm;
import eventTicker.views.UserView;

public class AccountingMenu extends AbstractMenu implements Component {

	public AccountingMenu() {
		super();
	}

	@Override
	protected void init() {
		MenuBar.Command loginCommand = new MenuBar.Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				openModal(new LoginForm());
			}
		};

		MenuBar.Command logoutCommand = new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				Notification.show(getSession().getCurrent().getAttribute("etNAME").toString() + ": logged out");
				keys.endSession(getSession().getCurrent().getAttribute("etNAME").toString(), getSession().getAttribute("etKEY").toString());
				getSession().setAttribute("etNAME", null);
				getSession().setAttribute("etKEY", null);
				getUI().getNavigator().navigateTo(UserView.NAME);
			}
		};

		MenuBar.Command registerCommand = new MenuBar.Command() {

			public void menuSelected(MenuItem selectedItem) {
				openModal(new RegistrationForm(new ConcreteUser()));
			}
		};
		if (sessionValidator()) {
			MenuItem logout = barmenu.addItem("logout", logoutCommand);
		} else {
			MenuItem login = barmenu.addItem("login", loginCommand);
			MenuItem register = barmenu.addItem("register", registerCommand);
		}
		this.addComponent(barmenu);
	}
}
