package eventTicker.views;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import eventTicker.backend.AccountingSessionKeys;
import eventTicker.backend.AppSynchronizer;
import eventTicker.backend.ConcreteEvent;
import eventTicker.backend.containerFilters.UserFilter;
import eventTicker.forms.EventForm;
import eventTicker.layoutComponents.StyleTestMap;
import eventTicker.layoutComponents.menu.AccountingMenu;
import eventTicker.layoutComponents.menu.NavMenu;

public abstract class AbstractOverview extends GridLayout implements View {

	protected StyleTestMap map = new StyleTestMap();
	protected NavMenu menu1;
	protected AccountingMenu menu2;
	protected AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();
	protected AccountingSessionKeys keys = AccountingSessionKeys.getInstance();
	protected TextField filter = new TextField();
	protected static Grid userContainer;
	protected static Grid eventContainer;
	public static Panel rightContent = new Panel();
	public static Panel leftContent = new Panel();
	public static Panel botContent = new Panel();
	public static Component swapLeft;
	public static Component swapRight;
	public static Component swapBot;

	protected abstract void concreteConfiguration();

	public static void updateContainers() {
		eventContainer.clearSortOrder();
		userContainer.clearSortOrder();
	}

	protected boolean sessionValidator() {
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

	protected void buildLayout() {
		this.setColumns(3);
		this.setRows(3);
		this.setMargin(true);
		this.setSpacing(true);
		this.removeAllComponents();
		this.setRowExpandRatio(1, 1);
		this.setRowExpandRatio(2, 2);

		filter.setWidth("100%");
		filter.setHeight("40px");

		menu1 = new NavMenu();
		menu1.setHeight("40px");
		menu1.setSizeFull();

		menu2 = new AccountingMenu();
		menu2.setHeight("40px");
		menu2.setSizeFull();

		this.map = new StyleTestMap();
		map.setWidth("100%");
		map.setSizeFull();

		eventContainer.setSizeFull();
		userContainer.setSizeFull();

		rightContent.setContent(userContainer);
		rightContent.setSizeFull();

		leftContent.setContent(eventContainer);
		leftContent.setSizeFull();

		botContent.setContent(map);
		botContent.setSizeFull();

		this.addComponent(menu1, 0, 0);
		this.addComponent(filter, 1, 0);
		this.addComponent(menu2, 2, 0);
		this.addComponent(leftContent, 0, 1, 1, 1);
		this.addComponent(rightContent, 2, 1, 2, 1);
		this.addComponent(botContent, 0, 2, 2, 2);

		this.setComponentAlignment(menu2, Alignment.TOP_RIGHT);

		this.setSizeFull();
		this.setWidth(100, Sizeable.Unit.PERCENTAGE);
		this.setHeight(100, Sizeable.Unit.PERCENTAGE);

	}

	private void stashContent() {
		swapLeft = leftContent.getContent();
		swapRight = rightContent.getContent();
		swapBot = botContent.getContent();
	}

	protected void eventDetails(ConcreteEvent e) {
		// stashContent();
		openModal(new EventForm(e));
	}

	protected void configureComponents() {
		filter.setInputPrompt("Lookup event by Substring...");
		filter.setTextChangeEventMode(TextChangeEventMode.EAGER);
		filter.addTextChangeListener(e -> eventLookupFilter(e.getText()));
		filter.addTextChangeListener(e -> userLookupFilter(e.getText()));

		userContainer.removeAllColumns();
		userContainer.addColumn("firstName");
		userContainer.addColumn("name");
		userContainer.setSelectionMode(Grid.SelectionMode.SINGLE);
		userContainer.setColumnOrder("firstName", "name");

		eventContainer.removeAllColumns();
		eventContainer.addColumn("name").setExpandRatio(1);
		eventContainer.setSelectionMode(Grid.SelectionMode.SINGLE);
		eventContainer.setColumnOrder("name");
		eventContainer.addSelectionListener(e -> relatedUserLookup((ConcreteEvent) eventContainer.getSelectedRow()));

		try {
			eventContainer.addSelectionListener(
					e -> this.map.addPointExclusive((ConcreteEvent) eventContainer.getSelectedRow()));
		} catch (Exception e) {
		}

	};

	protected void configureContainers() {
		userContainer = new Grid(sync.getUserContainer());
		eventContainer = new Grid(sync.getEventContainer());
		userContainer.addItemClickListener(new ItemClickEvent.ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if (!event.isDoubleClick())
					return;
				return;
			}

		});
	}

	protected void eventLookupFilter(String substr) {
		try {
			Container.Filter eventFilter = new Or(new SimpleStringFilter("name", substr, true, false),
					new SimpleStringFilter("id", substr, true, false));
			sync.getEventContainer().removeAllContainerFilters();
			sync.getEventContainer().addContainerFilter(eventFilter);

		} catch (Exception e) {
		}
	}

	protected void userLookupFilter(String substr) {
		try {
			Container.Filter userFilter = new Or(new SimpleStringFilter("firstName", substr, true, false),
					new SimpleStringFilter("name", substr, true, false));

			sync.getUserContainer().removeAllContainerFilters();
			sync.getUserContainer().addContainerFilter(userFilter);
		} catch (Exception e) {
		}
	}

	protected void relatedUserLookup(ConcreteEvent event) {
		try {
			Container.Filter userFilter = new UserFilter(sync.getRelated().get(event.getId()), "id");
			sync.getUserContainer().removeAllContainerFilters();
			sync.getUserContainer().addContainerFilter(userFilter);

		} catch (Exception e) {
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		configureContainers();
		configureComponents();
		concreteConfiguration();
		buildLayout();

	}

}
