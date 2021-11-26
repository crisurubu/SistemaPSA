package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Employee;
import model.entities.Occupation;
import model.exceptions.ValidationException;
import model.services.EmployeeService;
import model.services.OccupationService;

public class EmployeeFormController implements Initializable {

	private Employee entity;

	private EmployeeService service;

	private OccupationService occupationService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtCelular;

	@FXML
	private DatePicker dpAdmissionDate;

	@FXML
	private TextField txtDepartment;

	@FXML
	private ComboBox<Occupation> comboBoxOccupation;

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

	private ObservableList<Occupation> obsList;

	public void setEmployee(Employee entity) {
		this.entity = entity;
	}

	public void setServices(EmployeeService service, OccupationService occupationService) {
		this.service = service;
		this.occupationService = occupationService;
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

	private Employee getFormData() {
		Employee obj = new Employee();
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty");
		}
		obj.setEmail(txtEmail.getText());
		
		
		if (txtCelular.getText() == null || txtCelular.getText().trim().equals("")) {
			exception.addError("celular", "Field can't be empty");
		}
		obj.setEmail(txtCelular.getText());
		
		
		if(dpAdmissionDate.getValue() == null) {
			exception.addError("dpAdmissionDate", "Field can't be empty");
		}
		else {
			Instant instant = Instant.from(dpAdmissionDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setAdmissionDate(Date.from(instant));
		}
				
		if(txtDepartment.getText() == null || txtDepartment.getText().trim().equals("")) {
			exception.addError("department", "Fields can 't empty");
		}
		obj.setDepartment(txtDepartment.getText());
		
		
		obj.setOccupation(comboBoxOccupation.getValue());

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
		Constraints.setTextFieldMaxLength(txtName, 70);
		
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Constraints.setTextFieldMaxLength(txtCelular, 15);
		Utils.formatDatePicker(dpAdmissionDate, "dd/MM/yyyy");
		Constraints.setTextFieldMaxLength(txtDepartment, 30);
		initializeComboBoxOccupation();

	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		txtEmail.setText(entity.getCelular());
		
		if (entity.getAdmissionDate() != null) {

			dpAdmissionDate.setValue(LocalDate.ofInstant(entity.getAdmissionDate().toInstant(), ZoneId.systemDefault()));
		}
		
		if(entity.getOccupation() == null) {
			comboBoxOccupation.getSelectionModel().selectFirst();
		}
		else {
			comboBoxOccupation.setValue(entity.getOccupation());
		}

	}

	public void loadAssociateObjects() {
		if (occupationService == null) {
			throw new IllegalStateException("OccupationService was null");
		}

		List<Occupation> list = occupationService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxOccupation.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		labelErrorCelular.setText((fields.contains("celular") ? errors.get("celular") : ""));
		labelErrorDepartment.setText((fields.contains("department") ? errors.get("department") : ""));
			
		
		
	}

	private void initializeComboBoxOccupation() {
		Callback<ListView<Occupation>, ListCell<Occupation>> factory = lv -> new ListCell<Occupation>() {
			@Override
			protected void updateItem(Occupation item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxOccupation.setCellFactory(factory);
		comboBoxOccupation.setButtonCell(factory.call(null));
	}

}
