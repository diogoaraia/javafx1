package model.exceptions;

import java.util.HashMap;
import java.util.Map;

//EXCE��O PARA VALIDAR UM FORMUL�RIO
//CARREGARA MENSAGENS DE ERROS CASO EXIST�O

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// COLE��O DE TODOS OS ERROS POSS�VEIS
	// 1� STRING INFORMARA NOME DO CAMPO
	// 2� STRING INFORMARA MENSAGEM DE ERRO
	private Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	// METODO GET DOS ERROS
	public Map<String, String> getErrors(){
		return errors;
	}
	
	// ADICIONAR OS ERRO NA COLE��O DE ERROS CONTENDO NOME DO CAMPO E MENSAGEM DE ERRO
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
