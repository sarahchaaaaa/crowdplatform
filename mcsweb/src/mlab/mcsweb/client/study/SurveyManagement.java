package mlab.mcsweb.client.study;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.ImageAnchor;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.GreetingService;
import mlab.mcsweb.client.GreetingServiceAsync;
import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.Mcsweb;
import mlab.mcsweb.client.events.SurveyEvent;
import mlab.mcsweb.client.events.SurveyState;
import mlab.mcsweb.client.events.SurveyState.SurveySpecificState;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.SurveySummary;

public class SurveyManagement extends Composite {
	
	@UiField
	Column listColumn;
	
	@UiField
	HTMLPanel leftColumn;
	
	ImageAnchor imageAnchor = new ImageAnchor();
	
	private boolean isLoaded = false;
	private ArrayList<SurveyOverview> surveyList = new ArrayList<>();
	
	private Study study;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private static SurveyUiBinder uiBinder = GWT.create(SurveyUiBinder.class);

	interface SurveyUiBinder extends UiBinder<Widget, SurveyManagement> {
	}

	public SurveyManagement(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
	}
	
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		if(isLoaded){
			//do nothing
		}else {
			// TODO load survey for the first time
			imageAnchor.setUrl("images/create_new_256.png");
			imageAnchor.setResponsive(true);
			imageAnchor.setAsMediaObject(true);
			imageAnchor.setAlt("Create New Survey");
			//imageAnchor.setType(ImageType.THUMBNAIL);
			imageAnchor.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					SurveySummary surveySummary = new SurveySummary();
					surveySummary.setStudyId(study.getId());
					Mcsweb.getEventBus().fireEvent(new SurveyEvent(new SurveyState(surveySummary, SurveySpecificState.NEW)));
				}
			});
			
			leftColumn.add(new Br());
			leftColumn.add(new Br());
			leftColumn.add(imageAnchor);
			
			greetingService.getSurveyList(study.getId(), new AsyncCallback<ArrayList<SurveySummary>>() {
				
				@Override
				public void onSuccess(ArrayList<SurveySummary> result) {
					// TODO Auto-generated method stub
					for(int i=0;i<result.size();i++){
						surveyList.add(new SurveyOverview(result.get(i)));
						Window.alert("survey id :"+ result.get(i).getId() + " study id:"+ result.get(i).getStudyId());

					}
					
					for(int i=0;i<surveyList.size();i++){
						listColumn.add(surveyList.get(i));
					}

				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert("service not available");
				}
			});
						
			
			isLoaded = true;
		}
	}
	

}


class SurveyOverview extends Composite{
	Heading heading;
	Paragraph paragraph;
	HTMLPanel htmlPanel;
	
	
	public SurveyOverview(final SurveySummary surveySummary) {

		String name = surveySummary.getName();
		String modificationTime = surveySummary.getModificationTime();
		String modificationTimeZone = surveySummary.getModificationTimeZone();
		String lastSaveTime = "";
		try {
			int unixtime = Integer.parseInt(modificationTime) + Integer.parseInt(modificationTimeZone);
			lastSaveTime = JSUtil.getDatetimeFromUnix(String.valueOf(unixtime));
		} catch (Exception e) {
			// TODO: handle exception
		}
		String description = "Saved at  "+ lastSaveTime;
		
		
		heading = new Heading(HeadingSize.H3, name);
		paragraph = new Paragraph(description);
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
		htmlPanel.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Mcsweb.getEventBus().fireEvent(new SurveyEvent(new SurveyState(surveySummary, SurveySpecificState.NEW)));
			}
		}, ClickEvent.getType());
		
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

}
