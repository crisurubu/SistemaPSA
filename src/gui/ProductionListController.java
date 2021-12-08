package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Production;
import model.entities.Vehicle;
import model.entities.VehicleStatus;
import model.services.ProductionService;
import model.services.VehicleService;
import model.services.VehicleStatusService;

public class ProductionListController implements Initializable, DataChangeListener{

	private ProductionService service;
	
	@FXML
	private TableView<Vehicle> tableViewProduction;
	
	@FXML
	private TableColumn<Vehicle, Integer> tableColumnId;
		
	@FXML
	private TableColumn<Vehicle, String> tableColumnChassis;
	
	
	@FXML
	private Button btNew;
	
	private ObservableList<Vehicle> obsList;
	
	@FXML
	private TableColumn<Production, Production> tableColumnEDIT;

	@FXML
	private TableColumn<Production, Production> tableColumnREMOVE;

	
	@FXML
	public void onBtNewAction(ActionEvent event) {

		addVehicleInProduction();
		

		
	}

	public void setProductionService(ProductionService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnChassis.setCellValueFactory(new PropertyValueFactory<>("chassis"));
				
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProduction.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Vehicle> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewProduction.setItems(obsList);
	

	}

	

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void addVehicleInProduction() {
		
		try {
			Vehicle obj = new Vehicle();
			obj = tableViewProduction.getSelectionModel().getSelectedItem();
			Integer id = obj.getId();
			
			if(id == null) {
				throw new IllegalStateException("id was null");
			}
			else {
			
			
			Production objPro = new Production(null, id);
			service.saveOrUpdate(objPro);
			List<Vehicle> list = new ArrayList<>();
			list.add(service.findById(id));
			
			VehicleStatusService status = new VehicleStatusService();
			List<VehicleStatus> listStatus = new ArrayList<>();
			listStatus.add(status.findById(2));
			
			VehicleService vehicleService = new VehicleService();
			
			for(Vehicle vehicle : list) {
				
				vehicle.setVehicleStatus(status.findById(2));
				vehicleService.saveOrUpdate(vehicle);
				updateTableView();
				
			}
			JOptionPane.showMessageDialog(null, "Success Vehicle Add In Production");
			
		  }
	    }
		catch (NullPointerException e) {
		Alerts.showAlert("Error ", "Select Vehicle in Table ", e.getMessage(), AlertType.ERROR);
			
	    }
		
	}
	

	

}
