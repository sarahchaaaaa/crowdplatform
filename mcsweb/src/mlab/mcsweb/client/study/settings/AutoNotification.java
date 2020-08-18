package mlab.mcsweb.client.study.settings;

import java.util.ArrayList;
import java.util.Iterator;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Radio;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.DataView;
import com.googlecode.gwt.charts.client.Selection;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import com.googlecode.gwt.charts.client.options.Options;
import com.googlecode.gwt.charts.client.table.Table;
import com.googlecode.gwt.charts.client.table.TableOptions;
import com.googlecode.gwt.charts.client.table.TablePage;
import com.googlecode.gwt.charts.client.table.TableSort;

import mlab.mcsweb.client.JSUtil;
import mlab.mcsweb.client.services.SettingsService;
import mlab.mcsweb.client.services.SettingsServiceAsync;
import mlab.mcsweb.shared.AutoNotificationInfo;
import mlab.mcsweb.shared.Response;
import mlab.mcsweb.shared.Study;
import mlab.mcsweb.shared.Util;

public class AutoNotification extends Composite {

	@UiField
	Label globalErrorLabel, settingErrorLabel, adminErrorLabel;

	@UiField
	TextBox textTitle, textMessage, textBaseInterval, textIncreaseFactor, textNotifCount, textAttemptsBeforeNotify,
			textDaysBeforeNotify, textAttemptsBeforeTerminate, textDaysBeforeTerminate, textEmail;

	@UiField
	Radio notifyAndRadio, notifyOrRadio, terminateAndRadio, terminateOrRadio;

	@UiField
	Button buttonActivate, buttonDeactivate, buttonUpdate, buttonAdd, buttonEdit, buttonDelete, buttonSubmit,
			buttonCancel;

	@UiField
	HTMLPanel adminSubPanel, adminListPanel;

	private AutoNotificationInfo info;
	private Study study;
	Div listDiv;

	private Boolean tableLoaded = false;
	private ArrayList<String> adminList = new ArrayList<>();
	Table adminTable = new Table();
	DataView dataView = null;
	
	private String currentEmail = "";
	private boolean fromEdit = false;


	private final SettingsServiceAsync service = GWT.create(SettingsService.class);

	private static AutoNotificationUiBinder uiBinder = GWT.create(AutoNotificationUiBinder.class);

	interface AutoNotificationUiBinder extends UiBinder<Widget, AutoNotification> {
	}

	public AutoNotification(Study study) {
		initWidget(uiBinder.createAndBindUi(this));
		this.study = study;
		try {
			listDiv = new Div();
			listDiv.setId("admin_list_div");

			buttonAdd.setIcon(IconType.PLUS);
			buttonEdit.setIcon(IconType.EDIT);
			buttonDelete.setIcon(IconType.REMOVE);
			
			adminSubPanel.setVisible(false);

		} catch (Exception e) {
			// TODO: handle exception
//			Window.alert("exception in constructor" + e.getMessage());
		}

	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();

		service.getAutoNotificationSetting(this.study.getId(), new AsyncCallback<AutoNotificationInfo>() {

			@Override
			public void onSuccess(AutoNotificationInfo result) {
				info = result;
				populateSettingInfo();
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				// globalErrorLabel.setText("Error: Unable to load onLoad");

			}
		});

		if (!tableLoaded) {
			adminListPanel.add(listDiv);
			adminListPanel.add(new Br());

			service.getAllNotificationAdmin(this.study.getId(), new AsyncCallback<ArrayList<String>>() {
				@Override
				public void onSuccess(ArrayList<String> result) {
					adminList = result;
					listDiv.add(adminTable);
					drawAdminTable();
					tableLoaded = true;

				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});

		}

	} // end of onLoad

	void populateSettingInfo() {
		globalErrorLabel.setText("");
		settingErrorLabel.setText("");

		if (info != null) {
			textTitle.setText(info.getNotificationTitle());
			textMessage.setText(info.getNotificationMessage());
			if (info.getBaseInterval() > 0) {
				textBaseInterval.setText(String.valueOf(info.getBaseInterval()));
			} else {
				textBaseInterval.setText("");
			}
			if (info.getIncreaseFactor() > 0) {
				textIncreaseFactor.setText(String.valueOf(info.getIncreaseFactor()));
			} else {
				textIncreaseFactor.setText("");
			}

			if (info.getNumberOfPushAtAStage() > 0) {
				textNotifCount.setText(String.valueOf(info.getNumberOfPushAtAStage()));
			} else {
				textNotifCount.setText("");
			}

			if (info.getAttemptsBeforeNotify() > 0) {
				textAttemptsBeforeNotify.setText(String.valueOf(info.getAttemptsBeforeNotify()));
			} else {
				textAttemptsBeforeNotify.setText("");
			}

			if (info.getDaysBeforeNotify() > 0) {
				textDaysBeforeNotify.setText(String.valueOf(info.getDaysBeforeNotify()));
			} else {
				textDaysBeforeNotify.setText("");
			}

			boolean logicRequired = info.getAttemptsBeforeNotify() > 0 && info.getDaysBeforeNotify() > 0;

			if (info.getNotifyBooleanOperator().equalsIgnoreCase("and") && logicRequired) {
				notifyAndRadio.setValue(true);
				notifyOrRadio.setValue(false);
			} else if (info.getNotifyBooleanOperator().equalsIgnoreCase("or") && logicRequired) {
				notifyAndRadio.setValue(false);
				notifyOrRadio.setValue(true);
			} else {
				notifyAndRadio.setValue(false);
				notifyOrRadio.setValue(false);
			}

			if (info.getAttemptsBeforeTerminate() > 0) {
				textAttemptsBeforeTerminate.setText(String.valueOf(info.getAttemptsBeforeTerminate()));
			} else {
				textAttemptsBeforeTerminate.setText("");
			}

			if (info.getDaysBeforeTerminate() > 0) {
				textDaysBeforeTerminate.setText(String.valueOf(info.getDaysBeforeTerminate()));
			} else {
				textDaysBeforeTerminate.setText("");
			}

			logicRequired = info.getAttemptsBeforeTerminate() > 0 && info.getDaysBeforeTerminate() > 0;
			if (info.getTerminateBooleanOperator().equalsIgnoreCase("and") && logicRequired) {
				terminateAndRadio.setValue(true);
				terminateOrRadio.setValue(false);
			} else if (info.getTerminateBooleanOperator().equalsIgnoreCase("or") && logicRequired) {
				terminateAndRadio.setValue(false);
				terminateOrRadio.setValue(true);
			} else {
				terminateAndRadio.setValue(false);
				terminateOrRadio.setValue(false);
			}

			if (info.getActive() == 0) {
				buttonActivate.setVisible(true);
				buttonDeactivate.setVisible(false);
			} else {
				buttonActivate.setVisible(false);
				buttonDeactivate.setVisible(true);
			}

		}else {
			buttonActivate.setVisible(true);
			buttonDeactivate.setVisible(false);			
		}
	}

	boolean isValidSetting() {
		if (textTitle.getText().trim().isEmpty()) {
			settingErrorLabel.setText("Title can't be empty");
			return false;
		}

		if (textMessage.getText().trim().isEmpty()) {
			settingErrorLabel.setText("Message can't be empty");
			return false;
		}
		if (textBaseInterval.getText().trim().isEmpty()) {
			settingErrorLabel.setText("Base Interval can't be empty");
			return false;
		}
		try {
			double base = Double.parseDouble(textBaseInterval.getText().trim());
			if (base < 1.0 || base > 72) {
				settingErrorLabel.setText("Base Interval has to be within range of 1 to 72 hours");
				return false;
			}
		} catch (Exception e) {
			settingErrorLabel.setText("Base Interval has to be a number");
			return false;
		}

		if (textIncreaseFactor.getText().trim().isEmpty()) {
			settingErrorLabel.setText("Increase Factor can't be empty");
			return false;
		}

		try {
			double factor = Double.parseDouble(textIncreaseFactor.getText().trim());
			if (factor < 1 || factor > 9) {
				settingErrorLabel.setText("Increase factor has to be within range of 1 to 9");
				return false;
			}
		} catch (Exception e) {
			settingErrorLabel.setText("Increase factor must have to be a number");
			return false;
		}

		if (textNotifCount.getText().trim().isEmpty()) {
			settingErrorLabel.setText("Number of notifications at each stage can't be empty");
			return false;
		}

		try {
			int count = Integer.parseInt(textNotifCount.getText().trim());
			if (count < 1 || count > 999) {
				settingErrorLabel.setText("Number of notifications at each has to be within a range of 1 to 999");
				return false;
			}
		} catch (Exception e) {
			settingErrorLabel.setText("Number of notifications at each stage has to be an integer");
			return false;
		}

		if (!textAttemptsBeforeNotify.getText().trim().isEmpty()) {
			try {
				int count = Integer.parseInt(textAttemptsBeforeNotify.getText().trim());
				if (count < 1 || count > 999) {
					settingErrorLabel
							.setText("Number of notifications before notify has to be within a range of 1 to 999");
					return false;

				}
			} catch (Exception e) {
				settingErrorLabel.setText("Number of notifications before notify has to be an integer");
				return false;
			}

		}
		if (!textDaysBeforeNotify.getText().trim().isEmpty()) {
			try {
				int days = Integer.parseInt(textDaysBeforeNotify.getText().trim());
				if (days < 1 || days > 999) {
					settingErrorLabel.setText("Number of days before notify has to be within a range of 1 to 999");
					return false;

				}
			} catch (Exception e) {
				settingErrorLabel.setText("Number of days before notify has to be an integer");
				return false;
			}
		}
		if (!textAttemptsBeforeTerminate.getText().trim().isEmpty()) {
			try {
				int count = Integer.parseInt(textAttemptsBeforeTerminate.getText().trim());
				if (count < 1 || count > 999) {
					settingErrorLabel
							.setText("Number of notifications before terminate has to be within a range of 1 to 999");
					return false;

				}
			} catch (Exception e) {
				settingErrorLabel.setText("Number of notifications before terminate has to be an integer");
				return false;
			}

		}
		if (!textDaysBeforeTerminate.getText().trim().isEmpty()) {
			try {
				int days = Integer.parseInt(textDaysBeforeTerminate.getText().trim());
				if (days < 1 || days > 999) {
					settingErrorLabel.setText("Number of days before terminate has to be within a range of 1 to 999");
					return false;

				}
			} catch (Exception e) {
				settingErrorLabel.setText("Number of days before terminate has to be an integer");
				return false;
			}
		}

		return true;

	}

	@UiHandler("buttonUpdate")
	void updateDataUploadSetting(ClickEvent event) {

		globalErrorLabel.setText("");
		settingErrorLabel.setText("");
		if (isValidSetting()) {
			try {
				final AutoNotificationInfo temp = new AutoNotificationInfo();
				temp.setStudyId(this.study.getId());
				temp.setModificationTime(JSUtil.getUnixtime());
				temp.setModificationTimeZone(JSUtil.getTimezoneOffset());
				temp.setNotificationTitle(textTitle.getText().trim());
				temp.setNotificationMessage(textMessage.getText().trim());
				temp.setBaseInterval(Double.parseDouble(textBaseInterval.getText().trim()));
				temp.setIncreaseFactor(Double.parseDouble(textIncreaseFactor.getText().trim()));
				temp.setNumberOfPushAtAStage(Integer.parseInt(textNotifCount.getText().trim()));
				if (!textAttemptsBeforeNotify.getText().trim().isEmpty()) {
					temp.setAttemptsBeforeNotify(Integer.parseInt(textAttemptsBeforeNotify.getText().trim()));					
				}
				if (!textDaysBeforeNotify.getText().trim().isEmpty()) {
					temp.setDaysBeforeNotify(Integer.parseInt(textDaysBeforeNotify.getText().trim()));					
				}

				boolean logicRequired = temp.getAttemptsBeforeNotify() > 0 && temp.getDaysBeforeNotify() > 0;

				if (notifyAndRadio.getValue() && logicRequired) {
					temp.setNotifyBooleanOperator("and");
				} else if (notifyOrRadio.getValue() && logicRequired) {
					temp.setNotifyBooleanOperator("or");
				} else {
					temp.setNotifyBooleanOperator("");
				}

				if (!textAttemptsBeforeTerminate.getText().trim().isEmpty()) {
					temp.setAttemptsBeforeTerminate(Integer.parseInt(textAttemptsBeforeTerminate.getText().trim()));					
				}
				if (!textDaysBeforeTerminate.getText().trim().isEmpty()) {
					temp.setDaysBeforeTerminate(Integer.parseInt(textDaysBeforeTerminate.getText().trim()));					
				}
				
				logicRequired = temp.getAttemptsBeforeTerminate() > 0 && temp.getDaysBeforeTerminate() > 0;
				if (terminateAndRadio.getValue() && logicRequired) {
					temp.setTerminateBooleanOperator("and");
				} else if (terminateOrRadio.getValue() && logicRequired) {
					temp.setTerminateBooleanOperator("or");
				} else {
					temp.setTerminateBooleanOperator("");
				}
				service.updateAutoNotificationSetting(temp, new AsyncCallback<Response>() {

					@Override
					public void onSuccess(Response result) {
						if (result.getCode()==0) {
							info = temp;
							globalErrorLabel.setText("");
							settingErrorLabel.setText("");
							Notify.notify("Auto notification setting has been updated successfully", NotifyType.SUCCESS);							
						}else {
							globalErrorLabel.setText(result.getMessage());
							Notify.notify("The system fails to activate the auto notification process", NotifyType.DANGER);							
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						globalErrorLabel.setText("Service is not available, please try later!");

					}
				});

			} catch (Exception e) {
				// TODO: handle exception
//				Window.alert("exception "+ e.getLocalizedMessage());
			}
		}

	} // end of update

	@UiHandler("buttonActivate")
	void activateAutoNotification(ClickEvent event) {
		if (info != null && info.getActive() == 0 && !info.getNotificationTitle().isEmpty()) {
			service.updateAutoNotificationState(this.study.getId(), 1, new AsyncCallback<Response>() {

				@Override
				public void onFailure(Throwable caught) {
					globalErrorLabel.setText("Service is not available, please try later!");
					buttonActivate.setVisible(true);
					buttonDeactivate.setVisible(false);						
				}

				@Override
				public void onSuccess(Response result) {
					if (result.getCode()==0) {
						globalErrorLabel.setText("");
						Notify.notify("Auto notification has been activated successfully", NotifyType.SUCCESS);
						buttonActivate.setVisible(false);
						buttonDeactivate.setVisible(true);
						info.setActive(1);
					}else{
						globalErrorLabel.setText(result.getMessage());
						Notify.notify("The system fails to activate the auto notification process", NotifyType.DANGER);						
						buttonActivate.setVisible(true);
						buttonDeactivate.setVisible(false);
					}
				}
			});
		}else{
			globalErrorLabel.setText("Update notification setting before service activation");
			buttonActivate.setVisible(true);
			buttonDeactivate.setVisible(false);									
		}
	}

	@UiHandler("buttonDeactivate")
	void deactivateAutoNotification(ClickEvent event) {
		if (info!=null && info.getActive()==1) {
			service.updateAutoNotificationState(this.study.getId(), 0, new AsyncCallback<Response>() {

				@Override
				public void onFailure(Throwable caught) {
					globalErrorLabel.setText("Service is not available, please try later!");
					buttonActivate.setVisible(false);
					buttonDeactivate.setVisible(true);						
				}

				@Override
				public void onSuccess(Response result) {
					if (result.getCode()==0) {
						globalErrorLabel.setText("");
						Notify.notify("Auto notification has been deactivated successfully", NotifyType.SUCCESS);
						buttonActivate.setVisible(true);
						buttonDeactivate.setVisible(false);
						info.setActive(0);
					}else{
						globalErrorLabel.setText(result.getMessage());
						Notify.notify("The system fails to deactivate the auto notification process", NotifyType.DANGER);						
						buttonActivate.setVisible(false);
						buttonDeactivate.setVisible(true);						
					}
				}
			});
			
		}else{
			globalErrorLabel.setText("Auto notification process is already deactivated. Please contact administator.");
			buttonActivate.setVisible(false);
			buttonDeactivate.setVisible(true);									
	
		}
	}

	protected void reloadTable() {
		service.getAllNotificationAdmin(this.study.getId(), new AsyncCallback<ArrayList<String>>() {
			@Override
			public void onSuccess(ArrayList<String> result) {
				// TODO Auto-generated method stub
				adminList = result;
				listDiv.add(adminTable);
				drawAdminTable();
				tableLoaded = true;
			}

			@Override
			public void onFailure(Throwable caught) {
				// Window.alert("reloadTable onFailure");
				// TODO Auto-generated method stub

			}
		});
	}
	
	@UiHandler("buttonCancel")
	void cancelAction(ClickEvent event) {
		adminErrorLabel.setText("");
		currentEmail = "";
		textEmail.setText("");
		adminSubPanel.setVisible(false);
	}
	
	@UiHandler("buttonSubmit")
	void updateAdmin(ClickEvent event) {
		adminErrorLabel.setText("");
		String email = textEmail.getText().trim();
		if (Util.isEmailFOrmatValid(email)) {

			if (fromEdit) {
				//update
				service.editNotificationAdmin(study.getId(), currentEmail, email, new AsyncCallback<Response>() {
				
					@Override
					public void onSuccess(Response result) {
						// TODO Auto-generated method stub
						if (result.getCode() == 0) {
							reloadTable();
							textEmail.setText("");
							adminSubPanel.setVisible(false);
						} else {
							adminErrorLabel.setText("Service not available, please try later.");
						}
						
					}
					@Override
					public void onFailure(Throwable caught) {
						adminErrorLabel.setText("Service not available, please try later.");
					}
				});
			} else {
				service.addNotificationAdmin(study.getId(), email, new AsyncCallback<Response>() {

					@Override
					public void onSuccess(Response result) {
						// TODO Auto-generated method stub
						if (result.getCode() == 0) {
							reloadTable();
							textEmail.setText("");
							adminSubPanel.setVisible(false);
						} else {
							adminErrorLabel.setText("Service not available, please try later.");
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						adminErrorLabel.setText("Service not available, please try later.");
					}
				}); 
				
//				errorLabel.setText("Email submitted.");

			} 

		} else {
			adminErrorLabel.setText("Invalid Email!");
		}

	}


	@UiHandler("buttonAdd")
	void addAdmin(ClickEvent event) {
		adminSubPanel.setVisible(true);
		textEmail.setText("");
		fromEdit = false;
		currentEmail = "";
	}

	@UiHandler("buttonEdit")
	void editCollaborator(ClickEvent event) {
		ArrayList<String> checkedUsers = getCheckedUsers();
		if (checkedUsers.size() == 0) {
			Notify.notify("Select an admin to edit", NotifyType.WARNING);
		} else if (checkedUsers.size() > 1) {
			Notify.notify("Select an admin to edit", NotifyType.WARNING);
		} else {
			// Not sure what to put here yet...
			String editAdmin = getCheckedAdmin(checkedUsers.get(0));
			adminSubPanel.setVisible(true);
			textEmail.setText(editAdmin);
			fromEdit = true;
			currentEmail = editAdmin;
		}
	}

	@UiHandler("buttonDelete")
	void deleteCollaborators(ClickEvent event) {
		// for instant let's say a string appended with |
		String list = "";
		// get id's of selected list
		ArrayList<String> checkedUsers = getCheckedUsers();
		if (checkedUsers.size() == 0) {
			Notify.notify("Select an admin to delete", NotifyType.WARNING);
		} else {
			for (int i = 0; i < checkedUsers.size(); i++) {
				list += (checkedUsers.get(i) + "|");
				String toRemove = getCheckedAdmin(checkedUsers.get(i));
				adminList.remove(toRemove);
			}
			list = list.substring(0, list.length() - 1);

			service.deleteNotificationAdmins(study.getId(), list, new AsyncCallback<Response>() {
				@Override
				public void onSuccess(Response result) {
					if (result.getCode() == 0) {
						// drawParticipantsTable();
						// TODO: temporary, need to think what will happen if
						// update fails
					}
					drawAdminTable();
				}

				@Override
				public void onFailure(Throwable caught) {

				}
			});

		}

	}

	private ArrayList<String> getCheckedUsers() {
		NodeList<Element> nodelist = querySelector("[id^=allcheck]");
		ArrayList<String> checkedUsers = new ArrayList<>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			boolean checked = nodelist.getItem(i).getPropertyBoolean("checked");
			if (checked) {
				checkedUsers.add(nodelist.getItem(i).getId().split("-")[1]);
			}
		}
		return checkedUsers;
	}

	private String getCheckedAdmin(String email) {
		for (int i = 0; i < adminList.size(); i++) {
			if (email.equalsIgnoreCase(adminList.get(i))) {
				return adminList.get(i);
			}
		}
		return null;
	}

	public void drawAdminTable() {
		if (adminList.size() == 0) {
			adminTable.setVisible(false);
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
		} else {
			adminTable.setVisible(true);
			buttonEdit.setVisible(true);
			buttonDelete.setVisible(true);
			dataView = DataView.create(getDataTableFromList());
			// dataView.hideColumns(new int[]{1});
			adminTable.draw(dataView, getTableOptions());
		}
	}

	protected TableOptions getTableOptions() {
		TableOptions options = (TableOptions) Options.create();

		options.setAllowHtml(true);
		options.setPage(TablePage.ENABLE);
		options.setSort(TableSort.ENABLE);
		options.setPageSize(100);
		options.setStartPage(0);
		options.setWidth(getParent().getOffsetWidth());
		// options.setSort(Policy.ENABLE);
		// options.setPage(Policy.ENABLE);
		// //options.setWidth(width)
		// //options.setStartPage(0);
		// //options.setWidth("100%");
		// //options.setHeight("100%");
		// options.setOption("startPage", 0);
		// options.setOption("width", "100%");
		return options;
	}

	protected DataTable getDataTableFromList() {
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "");
		dataTable.addColumn(ColumnType.STRING, "Email");

		dataTable.addRows(adminList.size());
		int columnIndex = 0;
		int rowIndex = 0;
		Iterator<String> it = adminList.iterator();
		while (it.hasNext()) {
			String email = (String) it.next();
			String checkbox = "<input type=\"checkbox\" text-align=\"center\"id=\"allcheck-" + email + "\">";
			dataTable.setValue(rowIndex, columnIndex++, checkbox);
			dataTable.setValue(rowIndex, columnIndex++, email);

			rowIndex += 1;
			columnIndex = 0;
		}
		return dataTable;
	}

	private SelectHandler createSelectHandler(final Table table, final DataTable dataTable) {
		return new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				String message = "";
				JsArray<Selection> selections = table.getSelection();
				for (int i = 0; i < selections.length(); i++) {
					// if (selections.get(i).) {
					String id = dataTable.getFormattedValue(selections.get(i).getRow(), 1);
					message += (id + " ");
					// }

				}

			}
		};
	}

	public native void getColumnSelection(String checkboxId)/*-{
															
															}-*/;

	public final native NodeList<Element> querySelector(String selector)/*-{
		return $wnd.document.querySelectorAll(selector);
	}-*/;

	public static final native JsArrayString split(String string, String separator) /*-{
		return string.split(separator);
	}-*/;

}
