package httpjsonclient;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import Views.DefaultView;

@SpringUI
@Theme("valo")
public class MyVaadinUI extends UI {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SpringViewProvider viewProvider;

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.setSpacing(true);
		setContent(root);

		final CssLayout navigationBar = new CssLayout();
		navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		navigationBar.addComponent(createNavigationButton("All Entries view", DefaultView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("View Single View", DefaultView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("next Entry", DefaultView.VIEW_NAME));
		root.addComponent(navigationBar);

		final Panel viewContainer = new Panel("Entry Content");
		viewContainer.setSizeFull();
		root.addComponent(viewContainer);
		root.setExpandRatio(viewContainer, 1.0f);
		Navigator navigator = new Navigator(this, viewContainer);
		navigator.addProvider(viewProvider);
		navigator.addView("", DefaultView.class);
		setNavigator(navigator);
	}

	private Button createNavigationButton(String caption, final String viewName) {
		Button button = new Button(caption);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		return button;
	}
}