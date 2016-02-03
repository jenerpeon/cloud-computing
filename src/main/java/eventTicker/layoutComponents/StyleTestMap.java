package eventTicker.layoutComponents;

import java.util.concurrent.Semaphore;

import org.vaadin.addon.vol3.OLMap;
import org.vaadin.addon.vol3.client.OLCoordinate;
import org.vaadin.addon.vol3.client.style.OLFillStyle;
import org.vaadin.addon.vol3.client.style.OLStyle;
import org.vaadin.addon.vol3.client.style.OLTextStyle;
import org.vaadin.addon.vol3.feature.OLFeature;
import org.vaadin.addon.vol3.feature.OLPoint;
import org.vaadin.addon.vol3.layer.OLVectorLayer;
import org.vaadin.addon.vol3.source.OLVectorSource;
import org.vaadin.addon.vol3.util.StyleUtils;

import eventTicker.backend.ConcreteEvent;

public class StyleTestMap extends VectorLayerMap {
	private OLMap map;
	public static Semaphore mutex = new Semaphore(1);

	public StyleTestMap() {

	}

	@Override
	protected OLMap createMap() {
		map = super.createMap();
		OLVectorLayer vectorLayer = (OLVectorLayer) map.getLayers().get(1);
		OLStyle style = StyleUtils.createDefaultStyle();
		style.circleStyle.fill = new OLFillStyle("magenta");
		style.circleStyle.stroke.color = "green";

		vectorLayer.setStyle(style);
		return map;
	}

	public void addPoint(String id, double x, double y) {
		OLCoordinate center = new OLCoordinate();
		center.x = x;
		center.y = y;
		OLFeature feature = createPointFeatureWithLabel(id, x, y);
		((OLVectorSource) vectorLayer.getSource()).addFeature(feature);
		this.map.getView().setCenter(x, y);
		this.map.getView().setZoom(5);
		this.map.markAsDirtyRecursive();

	}

	public void delPoints() {
		for (OLFeature f : ((OLVectorSource) vectorLayer.getSource()).getFeatures()) {
			((OLVectorSource) vectorLayer.getSource()).removeFeatureById(f.getId());
		}

	}

	public void addPointExclusive(ConcreteEvent cevent) {
		try {
			delPoints();
			OLCoordinate center = new OLCoordinate();
			center.x = cevent.getWidth();
			center.y = cevent.getLength();
			OLFeature feature = createPointFeatureWithLabel(cevent.getName(), cevent.getWidth(), cevent.getLength());
			((OLVectorSource) vectorLayer.getSource()).addFeature(feature);
			this.map.getView().setCenter(cevent.getWidth(), cevent.getLength());
			this.map.getView().setZoom(4);
		} catch (Exception e) {}
	}

	private OLFeature createPointFeatureWithLabel(String id, double x, double y) {
		OLFeature testFeature = new OLFeature(id);
		testFeature.setGeometry(new OLPoint(x, y));
		// create a feature specific style.
		OLStyle style = StyleUtils.createDefaultStyle();
		style.textStyle = new OLTextStyle();
		style.textStyle.text = id;
		style.textStyle.offsetX = 1.1;
		style.textStyle.offsetY = 1.1;
		style.textStyle.font = "18px";
		style.textStyle.fill = new OLFillStyle("black");
		style.textStyle.stroke = StyleUtils.createDefaultStrokeStyle();
		style.textStyle.stroke.color = "black";
		style.textStyle.stroke.width = 1.0;
		style.circleStyle.stroke.color = "red";
		style.circleStyle.fill.color = "blue";
		testFeature.setStyle(style);
		return testFeature;
	}
}
