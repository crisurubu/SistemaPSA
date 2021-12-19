package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.AppointmentsService;
import model.services.EmployeeService;
import model.services.OccupationService;
import model.services.ProductionService;
import model.services.VehicleService;
import model.services.VehicleStatusService;


public class MainViewController implements Initializable {
	@FXML
	private MenuItem menuItemEmployee;

	@FXML
	private MenuItem menuItemOccupation;
	
	@FXML
	private MenuItem menuItemVehicle;
	
	@FXML
	private MenuItem menuItemVehicleStatus;
	
	@FXML
	private MenuItem menuItemProduction;
	
	@FXML
	private MenuItem menuItemAppointments;
	
	@FXML
	private MenuItem menuItemQuality;
	

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemEmployeeAction() {
		loadView("/gui/EmployeeList.fxml", (EmployeeListController controller) -> {
			controller.setEmployeeService(new EmployeeService());
			controller.updateTableView();
			
		});

	}

	@FXML
	public void onMenuItemOccupationAction() {
		loadView("/gui/OccupationList.fxml", (OccupationListController controller) -> {
			controller.setOccupationService(new OccupationService());
			controller.updateTableView();
			
		});

	}
	
	@FXML
	public void onMenuItemVehicleAction() {
		loadView("/gui/VehicleList.fxml", (VehicleListController controller) -> {
			controller.setVehicleService(new VehicleService());
			controller.updateTableView();
			System.out.println("onMenuItemVehicleAction()");
		});

	}
	

	@FXML
	public void onMenuItemVehicleStatusAction() {
		loadView("/gui/VehicleStatusList.fxml", (VehicleStatusListController controller) -> {
			controller.setVehicleStatusService(new VehicleStatusService());
			controller.updateTableView();
			System.out.println("onMenuItemVehicleStatusAction()");
		});

	}
	
	@FXML
	public void onMenuItemProductionAction() {
		loadView("/gui/ProductionList.fxml", (ProductionListController controller) -> {
			controller.setProductionService(new ProductionService());
			controller.updateTableView();
			
		});
		

	}
	
	@FXML
	public void onMenuItemAppointmentsAction() {
		loadView("/gui/AppointmentsList.fxml", (AppointmentsListController controller) -> {
			controller.setAppointmentsService(new AppointmentsService());
			controller.updateTableView();
			
		});
		

	}
	@FXML
	public void onMenuItemQuality() {
		loadView("/gui/CloseAppointmentsList.fxml", (CloseAppointmentsListController controller) -> {
			controller.setAppointmentsService(new AppointmentsService());
			controller.updateTableView();
			
		});
		

	}


	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction ) {
		try {
		    FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		    VBox newVBox = loader.load();
		    Scene mainScene = Main.getMainScene();
		    VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
		    
		    Node mainMenu = mainVBox.getChildren().get(0);
		    mainVBox.getChildren().clear();
		    mainVBox.getChildren().add(mainMenu);
		    mainVBox.getChildren().addAll(newVBox.getChildren());
		    
		    T controller = loader.getController();
		    initializingAction.accept(controller);
		   
		}
		catch(IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}

	}
}	
	