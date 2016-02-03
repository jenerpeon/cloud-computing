package eventTicker.layoutComponents;

import org.vaadin.addon.vol3.OLMap;
import org.vaadin.addon.vol3.OLMapOptions;
import org.vaadin.addon.vol3.OLView;
import org.vaadin.addon.vol3.OLViewOptions;
import org.vaadin.addon.vol3.client.OLCoordinate;
import org.vaadin.addon.vol3.client.OLExtent;
import org.vaadin.addon.vol3.client.Projections;
import org.vaadin.addon.vol3.client.source.OLMapQuestLayerName;
import org.vaadin.addon.vol3.layer.OLLayer;
import org.vaadin.addon.vol3.layer.OLTileLayer;
import org.vaadin.addon.vol3.source.OLMapQuestSource;
import org.vaadin.addon.vol3.source.OLSource;
import org.vaadin.addon.vol3.util.SimpleContextMenu;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import eventTicker.backend.ConcreteEvent;
import eventTicker.forms.EventForm;

@SuppressWarnings("serial")
public class MapContainer extends VerticalLayout {
	protected OLMap map;

	private void openModal(Component c) {
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


	public MapContainer() {
		this.setSizeFull();
		map = createMap();
		createContextMenu();
		this.addViewChangeListener();
		this.addComponent(map);
		map.getView().fitExtent(createExtent());
		addViewChangeListener();
	}

	protected OLMap createMap() {
		OLMap map = new OLMap(new OLMapOptions().setInputProjection(Projections.EPSG4326));
		OLLayer layer = createLayer(createSource());
		map.addLayer(layer);
		map.setView(createView());
		map.setSizeFull();
		map.addClickListener(new OLMap.ClickListener() {
			@Override
			public void onClick(OLMap.OLClickEvent clickEvent) {
                ConcreteEvent cevent = new ConcreteEvent();
                cevent.setWidth(clickEvent.getCoordinate().x);
                cevent.setLength(clickEvent.getCoordinate().y);
				openModal(new EventForm(cevent));
			}
		});
		return map;
	}

	private void addViewChangeListener() {
		map.getView().addViewChangeListener(new OLView.ViewChangeListener() {
			@Override
			public void resolutionChanged(Double newResolution) {
			}

			@Override
			public void rotationChanged(Double rotation) {
			}

			@Override
			public void centerChanged(OLCoordinate centerPoint) {
			}

			@Override
			public void zoomChanged(Integer zoom) {
			}

			@Override
			public void extentChanged(OLExtent extent) {
			}
		});
	}

	protected OLSource createSource() {
		return new OLMapQuestSource(OLMapQuestLayerName.OSM);
	}

	protected OLLayer createLayer(OLSource source) {
		return new OLTileLayer(source);
	}

	protected OLView createView() {
		OLViewOptions opts = new OLViewOptions();
		opts.setInputProjection(Projections.EPSG4326);
		OLView view = new OLView(opts);
		view.setZoom(1);
		view.setCenter(0, 0);
		return view;
	}

	protected void resetView() {
		map.getView().setCenter(0, 0);
		map.getView().setZoom(1);
	}

	protected AbstractLayout createControls() {
		HorizontalLayout controls = new HorizontalLayout();
		controls.setSpacing(true);
		controls.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Button button = new Button("Reset view");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				resetView();
			}
		});
		controls.addComponent(button);
		button = new Button("Show view state");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				OLCoordinate center = map.getView().getCenter();
				StringBuilder message = new StringBuilder();
				message.append("center: ").append(center.toString()).append("\n");
				message.append("rotation: ").append(map.getView().getRotation()).append("\n");
				message.append("zoom: ").append(map.getView().getZoom()).append("\n");
				message.append("resolution: ").append(map.getView().getResolution()).append("\n");
				Notification.show(message.toString());
			}
		});
		controls.addComponent(button);
		button = new Button("Toggle map visibility");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				map.setVisible(!map.isVisible());
			}
		});
		controls.addComponent(button);
		button = new Button("Fit extent");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				map.getView().fitExtent(createExtent());
			}
		});
		controls.addComponent(button);
		return controls;
	}

	protected OLExtent createExtent() {
		OLExtent extent = new OLExtent();
		extent.minX = 13.7388;
		extent.maxX = 13.7489;
		extent.minY = 51.050;
		extent.maxY = 51.060;
		return extent;
	}

	protected void createContextMenu() {
		SimpleContextMenu simpleContextMenu = new SimpleContextMenu(map);
		simpleContextMenu.addItem("Test context menu item", new SimpleContextMenu.Command() {
			@Override
			public void execute() {
				Notification.show("context item clicked");
			}
		});
	}

}
