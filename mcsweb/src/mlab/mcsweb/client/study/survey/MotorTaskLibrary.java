package mlab.mcsweb.client.study.survey;

public class MotorTaskLibrary {
	private static final MotorTaskLibrary instance = new MotorTaskLibrary();
	MotorTask[] taskList = new MotorTask[13];
	private MotorTaskLibrary() {
		// TODO Auto-generated constructor stub
		taskList[0] = new MotorTask("Signature", "MT001", "");
		taskList[1] = new MotorTask("Trace Shapes", "MT002", "");
		taskList[2] = new MotorTask("Trace Shapes with Speech", "MT003", "");
		taskList[3] = new MotorTask("Trace Shapes Cognitive", "MT004", "");
		taskList[4] = new MotorTask("Motor Function", "MT005", "");
		taskList[5] = new MotorTask("Motor Fucntion with Speech", "MT006", "");
		taskList[6] = new MotorTask("Motor Function Cognitive", "MT007", "");
		taskList[7] = new MotorTask("Memory Game", "MT008", "");
		taskList[8] = new MotorTask("Connect the Dots", "MT009", "");
		taskList[9] = new MotorTask("Visuospatial Test", "MT010", "");
		taskList[10] = new MotorTask("Color Test", "MT011", "");
		taskList[11] = new MotorTask("Picture Test", "MT012", "");
		taskList[12] = new MotorTask("Balance Test", "MT013", "");
		taskList[13] = new MotorTask("Target Test", "MT014", "");
	}
	
	public static MotorTaskLibrary getInstance() {
		return instance;
	}

	MotorTask[] getAllMotorTask(){
		return taskList;
	}
	
	String getMotorCode(String name){
		for(int i=0;i<taskList.length;i++){
			if(taskList[i].getName().equalsIgnoreCase(name)){
				return taskList[i].getCode();
			}
		}
		return "";
	}
}

class MotorTask {
	String name;
	String code;
	String details;

	public MotorTask(String name, String code, String details) {
		super();
		this.name = name;
		this.code = code;
		this.details = details;
	}

	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}

	public String getDetails() {
		return details;
	}

}
