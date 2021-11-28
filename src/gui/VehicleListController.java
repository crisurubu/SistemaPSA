package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vehicle;
import model.services.VehicleService;

public class VehicleListController implements Initializable, DataChangeListener{

	private VehicleService service;
	
	@FXML
	private TableView<Vehicle> tableViewVehicle;
	
	@FXML
	private TableColumn<Vehicle, String> tableColumnChassis;
	
	@FXML
	private TableColumn<Vehicle, String> tableColumnModel;
	
	@FXML
	private TableColumn<Vehicle, Date> tableColumnDateEntry;
	
	@FXML
	private TableColumn<Vehicle, Date> tableColumnExitDate;
	
	@FXML
	private TableColumn<Vehicle, String> tableColumnStatus;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Vehicle> obsList;
	
	@FXML
	private TableColumn<Vehicle, Vehicle> tableColumnEDIT;

	@FXML
	private TableColumn<Vehicle, Vehicle> tableColumnREMOVE;

	
	
	
	
	@FXML
	public void onBtNewAction(ActionEvent event) {

		Stage parentStage = gui.util.Utils.currentStage(event);
		Vehicle obj = new Vehicle();

		createDialogForm(obj, "/gui/VehicleForm.fxml", parentStage);
	}

	public void setVehicleService(VehicleService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		tableColumnChassis.setCellValueFactory(new PropertyValueFactory<>("chassis"));
		tableColumnModel.setCellValueFactory(new PropertyValueFactory<>("model"));
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("vehicleStatus"));
		tableColumnDateEntry.setCellValueFactory(new PropertyValueFactory<>("dateEntry"));
		tableColumnExitDate.setCellValueFactory(new PropertyValueFactory<>("ExitDate"));
		Utils.formatTableColumnDate(tableColumnDateEntry, "dd/MM/yyyy");
		Utils.formatTableColumnDate(tableColumnExitDate, "dd/MM/yyyy");
		
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
		initEditButtons();
		initRemoveButtons();

	}

	private void createDialogForm(Vehicle obj, String absoluteName, Stage parentStege) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			VehicleFormController controller = loader.getController();
			controller.setVehicle(obj);
			controller.setServices(new VehicleService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Vehicle data:");
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

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Vehicle, Vehicle>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Vehicle obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/VehicleForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Vehicle, Vehicle>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Vehicle obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Vehicle obj) {
		Optional<ButtonType> result =  Alerts.showConfirmation("Confirmation", "Are you sure to delete? ");
		
		if(result.get() == ButtonType.OK) {
			
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch(DbIntegrityException e ){
				Alerts.showAlert("Error removing", null, e.getMessage(), AlertType.ERROR);
				
			}
		}
		
	}

}
