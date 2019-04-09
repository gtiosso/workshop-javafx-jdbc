package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.entities.Seller;

public class SellerFormController implements Initializable {
	
	private Seller seller;

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
	private TextField txtDepartment;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Label labelErrorDepartment;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}

	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
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

	public void updateFormData() {
		if (seller == null) {
			throw new IllegalStateException("Seller was null!");
		}
		txtId.setText(String.valueOf(seller.getId()));
		txtName.setText(String.valueOf(seller.getName()));
		txtEmail.setText(String.valueOf(seller.getEmail()));
		txtBirthDate.setText(String.valueOf(seller.getBirthDate()));
		txtBaseSalary.setText(String.valueOf(seller.getBaseSalary()));
		txtDepartment.setText(String.valueOf(seller.getDepartment()));
	}
}
