package gui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	// Metodo para converter o campo ID do Formulario para Inteiro
	// Caso não seja especificado um numero no campo ele retornara nulo
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Double tryParseToDouble(String str) {
		try {
			return Double.parseDouble(str);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Date tryParseToDate(String str) {
		try {
			return sdf.parse(str);
		}
		catch (ParseException e) {
			return null;
		}
	}
}
