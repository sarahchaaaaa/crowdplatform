package mlab.mcsweb.client.study;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

abstract class BaseOverview extends Composite{
	private Heading heading;
	private Paragraph paragraph;
	private HTMLPanel htmlPanel;
	
	public BaseOverview() {
		// TODO Auto-generated constructor stub
	}
	
	public void setOverviewPanel(String headingText, String content){
		heading = new Heading(HeadingSize.H3, headingText);
		paragraph = new Paragraph(content);
		htmlPanel = new HTMLPanel("");
		htmlPanel.addStyleName("panel_border");
		htmlPanel.getElement().getStyle().setMargin(5, Unit.PCT);;
		htmlPanel.add(new Br());
		Row r1 = new Row();
		r1.addStyleName("col-lg-offset-1 col-md-offset-1 col-xs-offset-1");
		r1.add(heading);
		htmlPanel.add(r1);
		htmlPanel.add(new Br());
		Row r2 = new Row();
		r2.addStyleName("col-lg-offset-2 col-md-offset-2 col-xs-offset-2");
		r2.add(paragraph);
		htmlPanel.add(r2);
		htmlPanel.add(new Br());
		/*
		htmlPanel.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Mcsweb.getEventBus().fireEvent(new SurveyEvent(new SurveyState(surveySummary, SurveySpecificState.NEW)));
			}
		}, ClickEvent.getType());*/
		
		htmlPanel.addDomHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				// TODO Auto-generated method stub
				htmlPanel.getElement().getStyle().setBackgroundColor("#9fc69f");
				htmlPanel.getElement().getStyle().setCursor(Cursor.POINTER);
			}
		}, MouseOverEvent.getType());
		
		htmlPanel.addDomHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				// TODO Auto-generated method stub
 				htmlPanel.getElement().getStyle().setBackgroundColor("white");
 				htmlPanel.getElement().getStyle().setCursor(Cursor.DEFAULT);
			}
		}, MouseOutEvent.getType());
		
		initWidget(htmlPanel);
		
	}
	
	
	HTMLPanel getOverviewPanel(){
		return htmlPanel;
	}
	
	abstract void addClickAction();

}
