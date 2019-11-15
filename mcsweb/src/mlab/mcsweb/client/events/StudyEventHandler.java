package mlab.mcsweb.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface StudyEventHandler extends EventHandler {
	public void actionAfterStudyEvent(StudyEvent studyEvent);
}
