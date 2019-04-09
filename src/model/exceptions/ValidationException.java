package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// Declarando um atributo do tipo Map para armazenar
	// todos os tipos de erro possiveis para os campos do TextField
	// Exemplo: Map<Name, ErroX>
	private Map<String, String> errors = new HashMap<String, String>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors(){
		return errors;
	}
	
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
