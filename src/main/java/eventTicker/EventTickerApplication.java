
package eventTicker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import eventTicker.backend.AccountingSessionKeys;
import eventTicker.backend.AppSynchronizer;
import eventTicker.views.GuestView;
import eventTicker.views.UserView;

@Theme("valo")
@SuppressWarnings("serial")
// @ComponentScan
public class EventTickerApplication extends UI {

	private AccountingSessionKeys keys = AccountingSessionKeys.getInstance();
	private AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();

	public boolean sessionValidator() {
		try {
			Object etname = getSession().getCurrent().getAttribute("etNAME");
			System.out.println(etname);
			Object etkey = getSession().getCurrent().getAttribute("etKEY");
			System.out.println(etkey);
			if (etname != null && etkey != null) {
				boolean authValid = keys.authenticate(etkey.toString());
				System.out.println("authsucced" + authValid);
				return authValid;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = EventTickerApplication.class, widgetset = "eventTicker.AppWidgetSet")
	public static class webServlet extends VaadinServlet{
	}

	@Override
	protected void init(VaadinRequest request) {

		final VerticalLayout root = new VerticalLayout();
		final Panel viewContainer = new Panel();

		root.setHeight("100%");
		root.setSizeFull();

		root.addComponent(viewContainer);
		root.setComponentAlignment(viewContainer, Alignment.TOP_CENTER);

		viewContainer.setWidth(80, Unit.EM);
		viewContainer.setHeight(100, Unit.PERCENTAGE);

		setContent(root);
		Navigator navigator = new Navigator(this, viewContainer);
		navigator.addView(UserView.NAME, UserView.class);
		navigator.addView(GuestView.NAME, GuestView.class);

		getNavigator().navigateTo(GuestView.NAME);
		getNavigator().addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {

				// Check if a user has logged in
				boolean isLoggedIn = sessionValidator();
				boolean isLoginView = event.getNewView() instanceof GuestView;
				boolean isUserView = event.getNewView() instanceof UserView;

				if (!isLoggedIn && isUserView) {
					// Redirect to login view always if a user has not yet
					// logged in
					getNavigator().navigateTo(GuestView.NAME);
					return false;

				} else if (isLoggedIn && isLoginView) {
					getNavigator().navigateTo(UserView.NAME);
					// If someone tries to access to login view while logged in,
					// then cancel
					return false;
				}
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {

			}
		});
	}
}
