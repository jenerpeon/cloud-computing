package eventTicker.forms;

import java.util.Date;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import eventTicker.backend.AppSynchronizer;
import eventTicker.backend.ConcreteEvent;
import eventTicker.views.AbstractOverview;

public class EventForm extends GridLayout {

	public static final String NAME = "eventForm";
	private AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();

	private TextField name = new TextField("Event Name");
	private TextArea description = new TextArea("Beschreibung");
	private TextField width = new TextField();
	private TextField length = new TextField();
	private DateField abstractDate = new DateField("Datum");
	private Button createButton = new Button("Speichern", this::save);
	private Button cancelButton = new Button("Abbrechen", this::cancel);
	private boolean isEdit = false;
	private ConcreteEvent cevent;
	private BeanFieldGroup<ConcreteEvent> fieldGroup;

	private void cancel(Button.ClickEvent event) {
		((Window) this.getParent()).close();
	}

	private void save(Button.ClickEvent event) {
		try {
			fieldGroup.commit();
			if (isEdit)
				sync.updateEvent(cevent);
			else
				sync.saveEvent(cevent);
		} catch (Exception e) {
			return;
		}
		AbstractOverview.updateContainers();
		((Window)getParent()).close();

	}

	public EventForm(double x, double y) {
		cevent = new ConcreteEvent();
		// Coordinate fields
		width = new TextField("X Coordinate:");
		width.setValue(String.valueOf(x));
		width.setVisible(true);

		length = new TextField("Y Coordinate:");
		length.setValue(String.valueOf(y));
		length.setVisible(true);

		configureComponents();

	}

	public EventForm(ConcreteEvent e) {
		cevent = e;
		isEdit = true;
		buildLayout();
		setBeanBindings();
		configureComponents();
	}

	public EventForm() {
		buildLayout();
		setBeanBindings();
		configureComponents();
	}

	private void setBeanBindings() {
		fieldGroup = new BeanFieldGroup<ConcreteEvent>(ConcreteEvent.class);
		fieldGroup.buildAndBindMemberFields(this);
		fieldGroup.setItemDataSource(cevent);
	}

	private void buildLayout() {

		this.setColumns(4);
		this.setRows(4);
		this.setSpacing(true);
		setMargin(true);
		setSizeUndefined();
		HorizontalLayout coordinates = new HorizontalLayout(width, length);
		coordinates.setSpacing(true);
		HorizontalLayout buttons = new HorizontalLayout(createButton, cancelButton);
		buttons.setSpacing(true);

		this.addComponent(name, 0, 0, 2, 0);
		this.addComponent(abstractDate, 0, 1, 2, 1);
		this.addComponent(width, 0, 2, 1, 2);
		this.addComponent(length, 2, 2, 2, 2);
		this.addComponent(createButton, 0, 3, 1, 3);
		this.addComponent(cancelButton, 2, 3, 2, 3);
		this.addComponent(description, 3, 0, 3, 2);

	}

	public void configureComponents() {
		createButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		name.setWidth(230, Unit.PIXELS);
		width.setCaption("x-Koordinate");
		length.setCaption("y-Koordinate");
		width.setWidth(100, Unit.PIXELS);
		length.setWidth(100, Unit.PIXELS);
		description.setRows(8);
		description.setWidth(300, Unit.PIXELS);

		abstractDate.setValue(new Date());
	}

}
