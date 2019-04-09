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
import javafx.beans.property.ReadOnlyStringWrapper;
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
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service;
	
	private ObservableList<Seller> obsList;
	
	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	
	@FXML
	private TableColumn<Seller, String> tableColumnDepartment;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm("/gui/SellerForm.fxml", parentStage, obj);
	}
	
	// Metodo para iniciar o comportamento das colunas da TableView
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		tableColumnDepartment.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDepartment().getName()));

		
		// Pegando a referencia do Stage Principal,
		// atraves de um Downcasting da SuperClasse Window para a Classe Stage
		Stage stage = (Stage) Main.getMainScene().getWindow();

		// Redimensionando tamanho da TableView para acompnhar a tela principal
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void updateTableView() {
		// Garantindo a instanciação do Serviço
		if (service == null) {
			throw new IllegalStateException("Service was null!");
		}

		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(String absoluteName, Stage parentStage, Seller obj) {
		// Instanciando a janela de dialogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.setSellerService(service);
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			// Instanciando o novo Palco para a nova Janela
			Stage dialogStage = new Stage();
			// Declarando o Titulo da nova Janela
			dialogStage.setTitle("Enter Seller Data");
			// Instanciando a nova Cena
			dialogStage.setScene(new Scene(pane));
			// Declarando que a nova janela não poderá ser redimensionada
			dialogStage.setResizable(false);
			// Declarando quem é o Palco "Pai" da nova janela
			dialogStage.initOwner(parentStage);
			// Declarando que a janela é do tipo Modal, ou seja
			// enquanto esta não for fechada, não poderá interagir
			// nas demais janelas
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("Exception", "Error Load View", e.getMessage(), AlertType.ERROR);
		}
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm("/gui/SellerForm.fxml", Utils.currentStage(event), obj));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
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
	
	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete ?");
		
		if(result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				service.removeSeller(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error Removing Data", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
