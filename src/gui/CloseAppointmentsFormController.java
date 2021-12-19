package gui;

import java.net.URL;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Appointments;
import model.entities.Production;
import model.entities.Vehicle;
import model.entities.VehicleStatus;
import model.exceptions.ValidationException;
import model.services.AppointmentsService;
import model.services.ProductionService;
import model.services.VehicleService;

public class CloseAppointmentsFormController implements Initializable {

	private Appointments entity;

	private AppointmentsService service;
	
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtAppoitments;

	@FXML
	private TextField txtProductionId;
	
	@FXML
	private TextField txtStatus;

	
	
	@FXML
	private Label labelErrorAppointments;

	@FXML
	private Label labelErrorProductionId;
	
	@FXML
	private Label labelErrorStatus;


	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	

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
			updateVehicleStatus();
			
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

		if (txtAppoitments.getText() == null || txtAppoitments.getText().trim().equals("")) {
			exception.addError("Appointments", "Field can't be empty");
		}
		obj.setAppointments(txtAppoitments.getText());
		
		if (txtStatus.getText() == null || txtStatus.getText().trim().equals("")) {
			exception.addError("Status", "Field can't be empty");
		}
		obj.setStatus(txtStatus.getText());
		
		
		if (txtProductionId.getText() == null || txtProductionId.getText().trim().equals("")) {
			exception.addError("Production Id", "Field can't be empty");
		}
		obj.setProduction_Id(Utils.tryParseToInt(txtProductionId.getText()));
		
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
		Constraints.setTextFieldMaxLength(txtAppoitments, 2070);
		Constraints.setTextFieldInteger(txtProductionId);
		Constraints.setTextFieldMaxLength(txtStatus, 20);
		

	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtAppoitments.setText(entity.getAppointments());
		txtProductionId.setText(String.valueOf(entity.getProduction_Id()));
		txtStatus.setText("CLOSE");
		txtProductionId.setEditable(false);
	
		
	}

	

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorAppointments.setText((fields.contains("appointments") ? errors.get("appointments") : ""));
		labelErrorProductionId.setText((fields.contains("production_Id") ? errors.get("production_Id") : ""));
		labelErrorStatus.setText((fields.contains("status") ? errors.get("status") : ""));
		
		
		
	}
	
	private Integer getVehicleId() {
		ProductionService service = new ProductionService();
		Integer id = Utils.tryParseToInt(txtProductionId.getText());
		List<Production> list = new ArrayList<Production>();
		list.add(service.findByProduction(id));
		
		Integer idVehicle = 0;
		for(Production production : list) {
			 idVehicle =  production.getVehicle_Id();
			
		}
		return idVehicle;
		
	}
	
	private void updateVehicleStatus() {
		VehicleService service = new VehicleService();
		List<Vehicle> list = new ArrayList<>();
		list.add(service.findById(getVehicleId()));
		System.out.println(list);
		
		for(Vehicle vehicle : list) {
			Integer id = vehicle.getId();
			String chassis = vehicle.getChassis();
			String model = vehicle.getModel();
			Date dateEntry = vehicle.getDateEntry();
			Date exitDate = vehicle.getExitDate();
			VehicleStatus status = new VehicleStatus();
			status.setId(3);
			
			Vehicle newVehicle = new Vehicle(id, chassis, model, dateEntry, exitDate, status);
			service.saveOrUpdate(newVehicle);
			
		}
	}

}
