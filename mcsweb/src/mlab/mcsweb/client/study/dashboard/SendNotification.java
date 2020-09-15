package mlab.mcsweb.client.study.dashboard;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import mlab.mcsweb.client.services.DashboardService;
import mlab.mcsweb.client.services.DashboardServiceAsync;
import mlab.mcsweb.shared.Participant;
import mlab.mcsweb.shared.Response;

public class SendNotification extends Composite {
	
	@UiField
	Button testButton;

	
	private final DashboardServiceAsync service = GWT.create(DashboardService.class);

	private static SendNotificationUiBinder uiBinder = GWT.create(SendNotificationUiBinder.class);

	interface SendNotificationUiBinder extends UiBinder<Widget, SendNotification> {
	}

	public SendNotification() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("testButton")
	void testService(ClickEvent event){
		ArrayList<Participant> participants = new ArrayList<>();
		service.sendNotification(participants, "Test Title", "Test Message", new AsyncCallback<Response>() {
			
			@Override
			public void onSuccess(Response result) {
				if(result.getCode()==0){
					Notify.notify("Test service call successful", NotifyType.SUCCESS);
				}else{
					Notify.notify("Service is not available, please try later.", NotifyType.DANGER);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Notify.notify("Service is not available, please try later.", NotifyType.DANGER);
			}
		});
	}
	
}
