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
import model.entities.Production;
import model.entities.VehicleStatus;
import model.exceptions.ValidationException;
import model.services.ProductionService;
import model.services.VehicleStatusService;

public class ProductionFormController implements Initializable{
	
	private Production entity;
	
	private ProductionService service;
	
	private VehicleStatusService vehicleStatus;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtChassis;
	
	@FXML
	private TextField txtModel;
	
	@FXML
	private DatePicker DateEntry;
	
	@FXML
	private DatePicker ExitDate;
	
	@FXML
	private ComboBox<VehicleStatus> comboBoxStatus;
	
	@FXML
	private Label labelStatus;
	
	@FXML
	private Label labelErrorChassis;
	
	@FXML
	private Label labelErrorModel;
	
	@FXML
	private Label labelErrorDateEntry;
	
	@FXML
	private Label labelErrorExitDate;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	private ObservableList<VehicleStatus> obsList;
	
	
	public void setProduction(Production entity) {
		this.entity = entity;
	}
	
	public void setProductionService(ProductionService service, VehicleStatusService vehicleStatus) {
		this.service = service;
		this.vehicleStatus = vehicleStatus;

	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}

	private Production getFormData() {
		Production obj = new Production();
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtChassis.getText() == null || txtChassis.getText().trim().equals("")) {
			exception.addError("Chassis", "Field can't be empty");
		}
		//obj.setChassis(txtChassis.getText());
		

		
		
		if(exception.getErrors().size() > 0) {
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
		Constraints.setTextFieldMaxLength(txtChassis, 70);
		Constraints.setTextFieldMaxLength(txtModel, 40);
		Utils.formatDatePicker(DateEntry, "dd/MM/yyyy");
		Utils.formatDatePicker(ExitDate, "dd/MM/yyyy");
		initializeComboBoxStatus();
		
				
	}
	
	public void updateFormData() {
		
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		//txtChassis.setText(entity.getChassis());
		
		
	}
	
	public void loadAssociateObjects() {
		if (vehicleStatus == null) {
			throw new IllegalStateException("VehicleService was null");
		}

		List<VehicleStatus> list = vehicleStatus.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxStatus.setItems(obsList);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		labelErrorChassis.setText((fields.contains("chassis") ? errors.get("chassis") : ""));
		labelErrorModel.setText((fields.contains("model") ? errors.get("model") : ""));
		labelErrorDateEntry.setText((fields.contains("dateEntry") ? errors.get("dateEntry") : ""));
		labelErrorExitDate.setText((fields.contains("exitDate") ? errors.get("exitDate") : ""));
		
		
	}
	private void initializeComboBoxStatus() {
		Callback<ListView<VehicleStatus>, ListCell<VehicleStatus>> factory = lv -> new ListCell<VehicleStatus>() {
			@Override
			protected void updateItem(VehicleStatus item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getStatus());
			}
		};
		comboBoxStatus.setCellFactory(factory);
		comboBoxStatus.setButtonCell(factory.call(null));
	}

	
	

}
