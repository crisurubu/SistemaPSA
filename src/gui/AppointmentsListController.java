package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Production;
import model.entities.Vehicle;
import model.services.AppointmentsService;
import model.services.ProductionService;

public class AppointmentsListController implements Initializable, DataChangeListener{

	private Integer idVehicle;
	
	private Integer idProduction;
	
	private AppointmentsService service;
	
		
	@FXML
	private TableView<Vehicle> tableViewVehicle;
	
	@FXML
	private TableColumn<Vehicle, Integer> tableColumnId;
		
	@FXML
	private TableColumn<Vehicle, String> tableColumnChassis;

	@FXML
	private Button btNew;
	
	private ObservableList<Vehicle> obsList;
		
	@FXML
	public void onBtNewAction(ActionEvent event)  {

		Vehicle obj = new Vehicle();
		obj = tableViewVehicle.getSelectionModel().getSelectedItem();
		addAppointments();
		if (obj == null) {
			
		}
		else 
			{
				Stage parentStage = gui.util.Utils.currentStage(event);
				Production objProduction = new Production();
				objProduction.setId(idProduction);
				objProduction.setVehicle_Id(idVehicle);
				
				createDialogForm(objProduction,"/gui/AppointmentsForm.fxml", parentStage);

			}

		
	}
	

	
	public void setAppointmentsService(AppointmentsService service) {
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
		tableViewVehicle.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Vehicle> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewVehicle.setItems(obsList);
	

	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	
	
	private void createDialogForm(Production obj, String absoluteName, Stage parentStege) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			AppointmentsFormController controller = loader.getController();
			controller.setProduction(obj);
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Appointments data:");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStege);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Erro loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void addAppointments() {
			
			try {
				Vehicle obj = new Vehicle();
				obj = tableViewVehicle.getSelectionModel().getSelectedItem();
				Integer id = obj.getId();
				this.idVehicle = id;
				
				if(id == null) {
					throw new IllegalStateException("id was null");
				}
				else {
					
						List<Vehicle> listVehicle = new ArrayList<>();
						listVehicle.add(service.findById(id));
										
						for(Vehicle Vehicles : listVehicle) {
						Vehicles.getId();
						 									  
						}
						returnIdProduction();
			    }
				
		    }
			catch (NullPointerException e) {
			Alerts.showAlert("Error ", "Select Vehicle in Table ", e.getMessage(), AlertType.ERROR);
				
		    }
			
		}
		private void returnIdProduction() {
			
			ProductionService productionService = new ProductionService();
			List<Production> list = new ArrayList<>();
			list.add(productionService.findByIdProduction(idVehicle));
			
			for(Production production : list) {
				Integer idProduction = production.getId();
				this.idProduction = idProduction;
				
			}
			
			
			
		}
		
		
	
	}
