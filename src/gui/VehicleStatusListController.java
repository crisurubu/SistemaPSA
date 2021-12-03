package gui;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.VehicleStatus;
import model.services.VehicleStatusService;

public class VehicleStatusListController implements Initializable, DataChangeListener {

	private VehicleStatusService service;

	@FXML
	private TableView<VehicleStatus> tableViewVehicleStatus;

	@FXML
	private TableColumn<VehicleStatus, Integer> tableColumnId;

	@FXML
	private TableColumn<VehicleStatus, String> tableColumnStatus;

	private ObservableList<VehicleStatus> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {

		Stage parentStage = gui.util.Utils.currentStage(event);
		VehicleStatus obj = new VehicleStatus();

		createDialogForm(obj, "/gui/VehicleStatusForm.fxml", parentStage);
	}

	public void setVehicleStatusService(VehicleStatusService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVehicleStatus.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<VehicleStatus> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewVehicleStatus.setItems(obsList);
		
		

	}

	private void createDialogForm(VehicleStatus obj, String absoluteName, Stage parentStege) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			VehicleStatusFormController controller = loader.getController();
			controller.setVehicleStatus(obj);
			controller.setVehicleStatusService(new VehicleStatusService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter VehicleStatus data:");
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


}
