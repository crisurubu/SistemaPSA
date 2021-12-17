package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Appointments;
import model.entities.Production;
import model.exceptions.ValidationException;
import model.services.AppointmentsService;

public class AppointmentsFormController implements Initializable{
	
	
	private Production production;
	
	private Integer quantityRows;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtIdProduction;
	
	@FXML
	private TextArea txtAppointments;
	
	@FXML
	private TableView<Appointments> tableViewAppointments;
	
	@FXML
	private TableColumn<Appointments, String> tableColumnAppointment;
	
	@FXML
	private TableColumn<Appointments, Integer> tableColumnProduction_Id;
	
	@FXML
	private TableColumn<Appointments, String> tableColumnStatus;
	
	@FXML
	private TableColumn<Appointments, Appointments> tableColumnADD;
	
	
	
	@FXML
	private Button add;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	ObservableList<Appointments> obsList = FXCollections.observableArrayList();
	
  	

	public void setProduction(Production production) {
		this.production = production;
	}
	
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		
		try {
			
			getItemsTableView();
		  	notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
			Alerts.showAlert("Error Selection", null, e.getMessage(), AlertType.ERROR);
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

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onBtAddAction(ActionEvent event) {
		
		Appointments appointments = new Appointments();
		appointments.setAppointments(txtAppointments.getText());
		appointments.setProduction_Id(Utils.tryParseToInt(txtIdProduction.getText()));
		appointments.setStatus("OPEN");
		obsList.add(appointments);
		txtAppointments.setText("");
		txtAppointments.requestFocus();
		
		
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnAppointment.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointments"));
		tableColumnProduction_Id.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("production_Id"));
		tableColumnStatus.setCellValueFactory(new PropertyValueFactory<Appointments, String>("status"));
		tableViewAppointments.setItems(obsList);
		initializeNodes();
		initEditButtons();
		

	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtIdProduction);
		
		
	}

	
	 public void  updateFormData() {
		
		 txtIdProduction.setText(String.valueOf(production.getId()));
		 
	 }
	 
	 private List<Appointments> getItemsTableView() {
		    ObservableList<Appointments> list = FXCollections.observableArrayList();
			tableViewAppointments.getItems().addAll(list);
			list.addAll(tableViewAppointments.getItems());
			
			
			int quantityRows = list.size();
			
			List<Appointments> appointmentsList = new ArrayList<>();
			
			Appointments obj = new Appointments();
			obj.setProduction_Id(Utils.tryParseToInt(txtIdProduction.getText()));
			obj.setStatus("OPEN");
			
			Integer production_Id = obj.getProduction_Id();
			String status = obj.getStatus();
			
			for(Appointments app : list) {
				
				String appoint = app.getAppointments();
				appointmentsList.add(new Appointments(null, appoint, production_Id, status));
							
			}
			this.quantityRows = quantityRows;
			System.out.println("Quantia de linhas " +this.quantityRows);
			
			
			return appointmentsList;
		
	 }
	 
	 
	 private void createDialogForm(Appointments obj, String absoluteName, Stage parentStege) {
		
					
		
		 try {
			 if (tableViewAppointments.getSelectionModel().getSelectedItem() == null) {
					throw new NullPointerException();
				}
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
				Pane pane = loader.load();
				

				AppointmentsFormAddController controller = loader.getController();
				controller.setAppointments(obj);
				controller.setServices(new AppointmentsService());
				controller.updateFormData();
				CleanDataInTable();//limpa dados selecionados na tabela
						
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Enter Employee data:");
				dialogStage.setScene(new Scene(pane));
				dialogStage.setResizable(false);
				dialogStage.initOwner(parentStege);
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.showAndWait();
		 }	
			 
		 	catch (IOException e) {
				e.printStackTrace();
				
			}
		 	catch (NullPointerException e) {
		 		Alerts.showAlert("NullPointerEcxeption", "Erro Selected Data in Table", e.getMessage(), AlertType.ERROR);
				
			}
		}
	 
	 
	 private void initEditButtons() {
			tableColumnADD.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnADD.setCellFactory(param -> new TableCell<Appointments, Appointments>() {
				private final Button button = new Button("Save In BD");
				
				@Override
				protected void updateItem(Appointments obj, boolean empty) {
					
					super.updateItem(obj, empty);
					
					if (obj == null) {
						setGraphic(null);
						return;
					}
					setGraphic(button);
								
					button.setOnAction(
							event -> createDialogForm(obj, "/gui/AppointmentsFormAdd.fxml", Utils.currentStage(event)));
					
					
				}
			});
		}
	 	public  void CleanDataInTable()
	    {
	 		obsList.remove(tableViewAppointments.getSelectionModel().getSelectedItem());
	 				 	
		 		
	    }
	
	
}
	