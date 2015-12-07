package Views;

import java.io.IOException;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonParseException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import httpjsonclient.JsonParser;

@SpringView(name = DefaultView.VIEW_NAME)
public class DefaultView extends VerticalLayout implements View {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1764076678506314602L;
	public static final String VIEW_NAME = "";
	private JsonParser p = new JsonParser();

    @PostConstruct
    void init() {
//    	this.p = new JsonParser();
    }

    @Override
    public void enter(ViewChangeEvent event) {
			try {
				p.parseURL(" https://apex.oracle.com/pls/apex/tudws/get/events");
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   	addComponent(new Label(p.getEntry().getItems()));
    }
}