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
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}
	
	// Metodo para adicionar novas Views dentro da Cena Principal
	// Synchronized = Garante que todo o processamento abaixo
	// não será interrompido durante o multithread
	@FXML
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			// FXMLLoader = carrega o arquivo da View.fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// Carrego a nova view em um atributo
			VBox newVBox = loader.load();
			
			// Resgato a Cena Principal da View Principal do Programa
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			// Resgato os itens necessários para a apresentação da nova tela dentro da Cena Principal
			// Resgato a Barra de Menu (Principal)
			Node mainMenu = mainVBox.getChildren().get(0);
			// Removo todos os itens da Cena Principal
			mainVBox.getChildren().clear();
			// Adiciono a Barra de Menu resgatada para a nova Tela
			mainVBox.getChildren().add(mainMenu);
			// Adiciono todos os novos itens a nova tela
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch (IOException e) {
			Alerts.showAlert("IOException", "Error Load View", e.getMessage(), AlertType.ERROR);
		}	
	}
}
