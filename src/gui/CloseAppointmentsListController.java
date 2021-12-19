package gui;

import java.io.IOException;
import java.net.URL;
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
import model.entities.Appointments;
import model.services.AppointmentsService;

public class CloseAppointmentsListController implements Initializable, DataChangeListener{

	
	
	private AppointmentsService service;
	
	@FXML
	private TableView<Appointments> tableViewAppointments;
	
	@FXML
	private TableColumn<Appointments, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Appointments, String> tableColumnAppointments;
	
	@FXML
	private TableColumn<Appointments, Integer> tableColumnProduction_Id;

	@FXML
	private TableColumn<Appointments, String> tableColumnStatus;
	
	@FXML
	private TableColumn<Appointments, Appointments> tableColumnEDIT;

	@FXML
	private TableColumn<Appointments, Appointments> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Appointments> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = gui.util.Utils.currentStage(event);
		Appointments obj = new Appointments();
		
		createDialogForm(obj, "/gui/CloseAppointmentsForm.fxml", parentStage);
	
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
		tableColumnAppointments.setCellValueFactory(new PropertyValueFactory<>("appointments"));
		tableColumnProduction_Id.setCellValueFactory(new PropertyValueFactory<>("production_Id"));
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
				
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAppointments.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Appointments> list = service.findAllAppointments();
		obsList = FXCollections.observableArrayList(list);
		tableViewAppointments.setItems(obsList);
		initEditButtons();
		initRemoveButtons();

	}

	private void createDialogForm(Appointments obj, String absoluteName, Stage parentStege) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CloseAppointmentsFormController controller = loader.getController();
			controller.setAppointments(obj);
			controller.setServices(new AppointmentsService());
			
			
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

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Appointments, Appointments>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Appointments obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CloseAppointmentsForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Appointments, Appointments>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Appointments obj, boolean empty) {
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

	private void  removeEntity(Appointments obj) {
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
