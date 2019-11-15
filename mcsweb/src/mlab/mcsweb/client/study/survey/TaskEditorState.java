package mlab.mcsweb.client.study.survey;


public class TaskEditorState {
	public enum EditorSpecificState {
		ADD, RESIZE;
	}

	private EditorSpecificState editorSpecificState;

	public TaskEditorState(EditorSpecificState editorSpecificState) {
		super();
		this.editorSpecificState = editorSpecificState;
	}
	
	public EditorSpecificState getEditorSpecificState() {
		return editorSpecificState;
	}

}
