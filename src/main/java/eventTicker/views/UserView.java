package eventTicker.views;

import java.util.ArrayList;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.DetailsGenerator;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import eventTicker.backend.AppSynchronizer;
import eventTicker.backend.ConcreteEvent;
import eventTicker.backend.ConcreteUser;
import eventTicker.forms.EventForm;

@Theme("valo")
public class UserView extends AbstractOverview {

	public static final String NAME = "user";

	AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();
	private ConcreteUser cuser;
	private ShortcutListener deleteUser;
	private ShortcutListener deleteEvent;
	private ClickListener participateListener;
	private ItemClickListener showDetails;
	private DetailsGenerator detailGenerator;

	private Object swapId;

	private void participate(ConcreteEvent cevent) {
		if (!sessionValidator())
			return;
		sync.clearUserContainer();
		cuser = sync.findByUserName(getSession().getAttribute("etNAME").toString());
		if (cuser != null) {
			if (!sync.getRelated().containsKey(cevent.getId())) {
				sync.getRelated().put(cevent.getId(), new ArrayList());
			}
			if (sync.getRelated().get(cevent.getId()).contains(cuser.getId())) {
				sync.deleteRealation(cuser.getId(), cevent.getId());
			} else
				sync.saveRelation(cuser.getId(), cevent.getId());

			userContainer.clearSortOrder();
		}
		sync.restoreUserContainer();

	}

	private void configureListeners() {
		detailGenerator = new DetailsGenerator() {
			@Override
			public Component getDetails(com.vaadin.ui.Grid.RowReference rowReference) {

				ConcreteEvent cevent = (ConcreteEvent) ((BeanItem) rowReference.getItem()).getBean();
				Button participate = new Button("Teilnahme aendern");
				Button editButton = new Button("Bearbeiten");
				Button removeButton = new Button("Loeschen");
				Label spacer = new Label();
				spacer.setWidth("50px");
				participate.setStyleName(ValoTheme.BUTTON_PRIMARY);

				participate.addClickListener(e -> participate(cevent));
				editButton.addClickListener(e -> openModal(new EventForm(cevent)));
				removeButton.addClickListener(e -> sync.deleteEvent(cevent));

				HorizontalLayout layout = new HorizontalLayout(spacer, participate, editButton, removeButton);
				layout.setSpacing(true);
				layout.setMargin(false);
				layout.setSizeUndefined();

				return layout;
			}
		};
		participateListener = new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					ConcreteUser user = sync.findByUserName(getSession().getAttribute("etNAME").toString());
					sync.saveRelation(user.getId(), (long) 0);
				} catch (Exception e) {
				}
			}
		};

		showDetails = new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				Object itemId = event.getItemId();
				if (swapId != null && itemId != swapId && eventContainer.isDetailsVisible(swapId)) {
					eventContainer.setDetailsVisible(swapId, false);
				}
				if (eventContainer.isDetailsVisible(itemId))
					eventContainer.setDetailsVisible(itemId, false);
				else {
					eventContainer.setDetailsVisible(itemId, true);
					swapId = itemId;
				}
			}
		};

		deleteUser = new ShortcutListener("Delete", KeyCode.DELETE, null) {

			@Override
			public void handleAction(final Object sender, final Object target) {
				if (userContainer.getSelectedRow() != null) {
					ConcreteUser e = (ConcreteUser) eventContainer.getSelectedRow();
					userContainer.getContainerDataSource().removeItem(userContainer.getSelectedRow());
				}

			}

		};

		deleteEvent = new ShortcutListener("Delete", KeyCode.DELETE, null) {
			@Override
			public void handleAction(final Object sender, final Object target) {
				if (eventContainer.getSelectedRow() != null) {
					ConcreteEvent e = (ConcreteEvent) eventContainer.getSelectedRow();
					sync.deleteEvent(e);
					eventContainer.getContainerDataSource().removeItem(eventContainer.getSelectedRow());
				}
			}
		};

	}

	protected void concreteConfiguration() {
		configureListeners();
		userContainer.addShortcutListener(deleteUser);
		eventContainer.addShortcutListener(deleteEvent);
		eventContainer.setDetailsGenerator(detailGenerator);
		eventContainer.addItemClickListener(showDetails);

	}
}
