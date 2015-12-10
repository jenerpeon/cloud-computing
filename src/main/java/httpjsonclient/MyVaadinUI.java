package httpjsonclient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("valo")
public class MyVaadinUI extends UI {

	private static final long serialVersionUID = 1L;
	private BeanItemContainer<ItemEntity> container;
	private JsonParser p = new JsonParser();

	TextField filter = new TextField();
	Grid contactList = new Grid();
	Button newContact = new Button("New Event");

	@Override
	protected void init(VaadinRequest request) {
		configureComponents();
		buildLayout();
		fetchData();
	}

	private void buildLayout() {

		// outer Layout
		final VerticalLayout root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.setSpacing(true);
		root.setWidth("90%");
//		root.setWidthUndefined();
		root.addComponent(new Label("News Ticker"));

		HorizontalLayout viewContainer = new HorizontalLayout();
		viewContainer.setMargin(true);
		viewContainer.setSpacing(true);

		// leftlayout
		// Tree tree = new Tree("My Tree", getItemContainer());
		// tree.setSizeFull();
		// rightlayout
		VerticalLayout rightView = new VerticalLayout();
		HorizontalLayout actions = new HorizontalLayout(filter, newContact);

		actions.setSpacing(true);
		rightView.setSpacing(true);

		rightView.addComponent(actions);
		Table t = new Table("Event Table", this.container);

//		Collection<String> propids = container.getContainerPropertyIds();
//		System.out.println(propids);
		t.setWidthUndefined();

		rightView.addComponent(new Table("table example", t));
		rightView.setWidthUndefined();
		root.addComponent(rightView);
		root.setExpandRatio(rightView, 1);

		setContent(root);

	}

	private void configureComponents() {
		Item item = null;
		this.container = new BeanItemContainer<ItemEntity>(ItemEntity.class);
		this.container.removeContainerProperty("description");
		this.container.removeContainerProperty("location");
	}

	public void fetchData() {
		try {
			p.parseURL("https://apex.oracle.com/pls/apex/tudws/eventmanagement/events");
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Map<String, String>> o = p.getEntry().getItems();
		for (Map m : o) {
			String id = m.get("id").toString();
			String name = m.get("name").toString();
			String description = m.get("beschreibung").toString();
			String date = m.get("datum").toString();
			String breite = m.get("breite").toString();
			String laenge = m.get("laenge").toString();
			this.container.addItem(new ItemEntity(id, name, description, date));
			System.out.println(id + name + description + date + breite + laenge);
		}
	}
}