package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.exceptions.ValidationException;
import model.services.DepartmentService;

//CLASSE QUE EMITE O EVENTO (SUBJECT)

public class DepartmentFormController implements Initializable {

	// DEPENDENCIA PARA O DEPARTAMENT
	private Department entity;
	
	// DEPENDENCIA PARA DEPARTMENTSERVICE
	private DepartmentService service;
	
	// LISTA DE DATA CHANGE LISTENER PERMITINDO OUTROS OBJETOS SE INSCREVEREM NA LISTA E RECEBEREM O EVENTO
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
		
		
	//DECLARACAO DOS COMPONENTES DA TELA
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;

	//ERRO EM CASO DE ERRO NO PREECHIMENTO DO NOME
	@FXML
	private Label labelErrorName;

	
	
	//CONTROLE DOS BOTOES
	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;
	
	
	// DECLARACAO DO METODO SET PARA DEPARTAMENT
	public void setDepartment(Department entity) {
		this.entity = entity;
	}

	// DECLARACAO DO METODO SET PARA DEPARTAMENTSERVICE	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	
	//DECLARACAO DOS METODOS DOS BOTOES
	@FXML
	private void onBtSaveAction(ActionEvent event) {
		if (entity == null) {		
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}	
		try {
			// PEGA OS DADOS DO FORMULARIO E INSTANCIAR UM DEPARTAMENTO
			entity = getFormData();
			// SALVA NO BANCO DE DADOS
			service.saveOrUpdate(entity);
			// NOTIFICAÇÃO DOS DATA CHANGE LISTENERS
			notifyDataChangeListeners();
			// FECHAR A JANELA DO FORMULARIO APOS SALVAR
			Utils.currentStage(event).close();
    	}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	// EMISSÃO DO EVENTO
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}	
	}

	// METODO PARA PEGAR OS DADOS DO FORMULARIO E INSTANCIAR UM DEPARTAMENTO
	private Department getFormData() {
		Department obj = new Department();
		
		// EXCEÇÃO
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		// MENSAGEM DE ERRO EM CASO DO CAMPO ESTIVER VAZIO
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		// SE NA COLEÇÃO TIVER AO MENOS 1 ERRO
		// É LANÇADO A EXCEÇÃO
		if (exception.getErrors().size() > 0 ) {
			throw exception;
		}
		return obj;
	}

	//DECLARACAO DOS METODOS DOS BOTOES
	@FXML
	private void onBtCancelAction(ActionEvent event) {
		// FECHAR A JANELA DO FORMULARIO APOS SALVAR
		Utils.currentStage(event).close();
		
	}
	
	
	// INICIALIZACAO DO FORMULARIO
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// REGRAS DE INSERCAO DE DADOS
		initializeNodes();	
	}
	
	private void initializeNodes( ) {
		// ACEITAR APENAS NUMERO INTEIRO NO ID
		Constraints.setTextFieldInteger(txtId);
		// NUMERO MAXIMO DE CARACTERES NO TEXTO PARA NOME DO DEPARTAMENTO
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	// INSERIR OS DADOS NO BANCO
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		// CONVERSAO DO ID PARA STRING ANTES DE IR PARA O BANCO
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());		
	}
	
	// EXIBIR OS POSSÍVEIS ERROS
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
}
