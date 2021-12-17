package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Appointments;
import model.exceptions.ValidationException;
import model.services.AppointmentsService;

public class AppointmentsFormAddController implements Initializable {

	private Appointments entity;

	private AppointmentsService service;


	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtAppointments;

	@FXML
	private TextField txtProduction_Id;
	
	@FXML
	private TextField txtStatus;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorCelular;

	@FXML
	private Label labelErrorAdmissionDate;

	@FXML
	private Label labelErrorDepartment;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	//private ObservableList<Occupation> obsList;

	public void setAppointments(Appointments entity) {
		this.entity = entity;
	}

	public void setServices(AppointmentsService service) {
		this.service = service;
		
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	private Appointments getFormData() {
		Appointments obj = new Appointments();
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtAppointments.getText() == null || txtAppointments.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setAppointments(txtAppointments.getText());
		
		if (txtProduction_Id.getText() == null || txtProduction_Id.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty");
		}
		obj.setProduction_Id(Utils.tryParseToInt(txtProduction_Id.getText()));
		
		
		if (txtStatus.getText() == null || txtStatus.getText().trim().equals("")) {
			exception.addError("celular", "Field can't be empty");
		}
		obj.setStatus("OPEN");
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtAppointments, 300);
		Constraints.setTextFieldMaxLength(txtProduction_Id, 100);
		Constraints.setTextFieldMaxLength(txtStatus, 5);
		

	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtAppointments.setText(entity.getAppointments());
		txtProduction_Id.setText(String.valueOf(entity.getProduction_Id()));
		txtStatus.setText(entity.getStatus());
		

	}

	/*public void loadAssociateObjects() {
		if (occupationService == null) {
			throw new IllegalStateException("OccupationService was null");
		}

		List<Occupation> list = occupationService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxOccupation.setItems(obsList);
	}*/

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText((fields.contains("name") ? errors.get("appointments") : ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("production_Id") : ""));
		labelErrorCelular.setText((fields.contains("celular") ? errors.get("status") : ""));
	
			
		
		
	}

	
}
