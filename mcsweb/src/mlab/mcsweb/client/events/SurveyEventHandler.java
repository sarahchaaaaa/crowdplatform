package mlab.mcsweb.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface SurveyEventHandler extends EventHandler {
	public void actionAfterSurveyEvent(SurveyEvent surveyEvent);
}
