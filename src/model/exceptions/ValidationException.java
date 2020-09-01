package model.exceptions;

import java.util.HashMap;
import java.util.Map;

//EXCEÇÃO PARA VALIDAR UM FORMULÁRIO
//CARREGARA MENSAGENS DE ERROS CASO EXISTÃO

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// COLEÇÃO DE TODOS OS ERROS POSSÍVEIS
	// 1ª STRING INFORMARA NOME DO CAMPO
	// 2ª STRING INFORMARA MENSAGEM DE ERRO
	private Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	// METODO GET DOS ERROS
	public Map<String, String> getErrors(){
		return errors;
	}
	
	// ADICIONAR OS ERRO NA COLEÇÃO DE ERROS CONTENDO NOME DO CAMPO E MENSAGEM DE ERRO
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
