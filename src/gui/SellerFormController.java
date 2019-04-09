package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import model.entities.Department;
import model.entities.Seller;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller seller;
	
	private SellerService service;

	private List<DataChangeListener> dataChangeListener = new ArrayList<DataChangeListener>();
	
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private TextField txtDepartmentId;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Label labelErrorDepartmentId;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (seller == null) {
			throw new IllegalStateException("Department was null!");
		}
		if (service == null) {
			throw new IllegalStateException("DepartmentService was null!");
		}
		try {
			seller = getFormData();
			service.saveOrUpdate(seller);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Error Saving Data", null, e.getMessage(), AlertType.ERROR);
		}
		//catch (ValidationException e) {
			//setErrorMessages(e.getErrors());
		//}
		
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldEmail(txtEmail);
		Constraints.setTextFieldDouble(txtBaseSalary);

	}
	
	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}

	public void updateFormData() {
		if (seller == null) {
			throw new IllegalStateException("Seller was null!");
		}
		txtId.setText(String.valueOf(seller.getId()));
		txtName.setText(String.valueOf(seller.getName()));
		txtEmail.setText(String.valueOf(seller.getEmail()));
		txtBirthDate.setText(String.valueOf(seller.getBirthDate()));
		txtBaseSalary.setText(String.valueOf(seller.getBaseSalary()));
		txtDepartmentId.setText(String.valueOf(seller.getDepartment()));
	}
	
	private Seller getFormData() {
		Seller seller = new Seller();
		
		// Instanciando a exceção
		//ValidationException exception = new ValidationException("Validation Error");
		
		seller.setId(Utils.tryParseToInt(txtId.getText()));
		
		//if (txtName.getText() == null || txtName.getText().trim().equals("")){
		//	exception.addError("name", "Field can not be empty");
		//}
		seller.setName(txtName.getText());
		
		//if (txtEmail.getText() == null || txtName.getText().trim().equals("")){
		//	exception.addError("email", "Field can not be empty");
		//}
		seller.setEmail(txtEmail.getText());
		
		seller.setBirthDate(Utils.tryParseToDate(txtBirthDate.getText()));
		
		seller.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		seller.setDepartment(new Department(Utils.tryParseToInt(txtDepartmentId.getText()), null));
		
		// Lançando a exceção
		//if(exception.getErrors().size() > 0) {
		//	throw exception;
		//}
		
		return seller;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
		
	}
}
