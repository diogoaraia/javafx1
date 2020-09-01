package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	// DECLARACAO DE DEPENDENCIA DOS DADOS EXISTENTES EM DEPARTAMENTOS
	// NO FINAL INJETA A DEPENDENCIA VIA METODO set
	private DepartmentService service;

	// CRIACAO DA TABELA
	@FXML
	private TableView<Department> tableViewDepartment;

	// CRIACAO COLUNA DOS ID'S
	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	// CRIACAO COLUNA DOS DEPARTMENT'S
	@FXML
	private TableColumn<Department, String> tableColumnName;

	// BOTAO DE EDIÇÃO DE DEPARTAMENTO
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	// BOTAO DE DELETE DE DEPARTAMENTO
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	// CRIACAO BOTAO PARA ABRIR A LISTA DE DEPARTAMENTOS
	@FXML
	private Button btNew;

	// METODO PARA APARECER NA tableViewDepartment
	// CARREGANDO OS DADOS DE DepartmentService NA ObservableList
	// DECLARANDO A ObservableList E NO FINAL O METODO PARA CARREGAR OS DADOS
	private ObservableList<Department> obsList;

	// METODO DE ACAO DO BOTAO
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);

		// DEPARTAMENT INICIAR VAZIO
		// DECLARA-SE O DEPARTAMENT VAZIO
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	// METODO DE INJECAO DE DEPENDENCIA DOS DADOS EXISTENTES EM DepartmentService
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	// METODO PARA INICIAR ALGUM COMPONENTE DA TELA
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	// METODO PARA INICIAR O COMPORTAMENTO DAS COLUNAS DA TABELA
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// TABELA TER A MESMA LARGURA E ALTURA DA TELA
		// PEGANDO UMA REFERENCIA DA TELA MainScene
		Stage stage = (Stage) Main.getMainScene().getWindow();
		// A PAGINA DE DEPARTMENT ACOMPANHAR AS MEDIDAS DA TELA ATE SE REDIMENSIONAR
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	// METODO PARA ACESSAR O SERVICO, CARREGAR OS DEPARTAMENTOS E JOGAR OS DADOS NA
	// ObservableList
	public void updateTableView() {
		// CASO O SERVICO SEJA NULO E LANCADO UMA EXCECAO
		if (service == null) {
			throw new IllegalStateException("Service was null!");
		}

		// OBTEM OS DADOS DO SERVICO DepartmentService
		List<Department> list = service.findAll();

		// CARREGAR OS DADOS NA ObservableList
		obsList = FXCollections.observableArrayList(list);

		// EXIBE OS DADOS DA ObservableList NA tableViewDepartment
		tableViewDepartment.setItems(obsList);

		// ACRESCENTA O BOTÃO EDIT EM CADA LINHA DA TABELA COM DEPARTAMENTO
		initEditButtons();
		// ACRESCENTA O BOTÃO DELETE EM CADA LINHA DA TABELA COM DEPARTAMENTO
		initRemoveButtons();
	}

	// AÇÃO DE ABRIR TELA DO FORMULARIO DE ACRESCENTAR NOVO DEPARTAMENTO ACIONADO
	// PELO NEW
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			// PEGAR REFERENCIA PARA O CONTROLADOR
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			// INJECAO DE DEPENDENCIA
			controller.setDepartmentService(new DepartmentService());
			// INSCRIÇÃO PARA RECEBER O EVENTO DE ATUALIZAÇÃO DE DADOS
			controller.subscribeDataChangeListener(this);
			// CARREGAR OS DADOS NO FORMULARIO
			controller.updateFormData();

			// UMA TELA NA FRENTE DE OUTRA
			Stage dialogStage = new Stage();
			// INFORMATIVO PARA DIGITAR DADOS DO DEPARTAMENTO
			dialogStage.setTitle("Enter Department data");
			// O Scene será pane com o formulário
			dialogStage.setScene(new Scene(pane));
			// A TELA NÃO PODE SER REDIMENSIONADA
			dialogStage.setResizable(false);
			// PAI DO Stage
			dialogStage.initOwner(parentStage);
			// ENQUANTO NÃO FECHAR O FORMULARIO NÃO SERÁ POSSÍVEL FECHAR A JANELA ANTERIOR
			// (POR TRAS)
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	// QUANDO O EVENTO DE DADOS ATUALIZADOS
	// CHAMARA O MÉTODO UPDATETABLEVIEW
	@Override
	public void onDataChanged() {
		updateTableView();
	}

	// METODO PARA ACRESCENTA O BOTÃO EDIT EM CADA LINHA DA TABELA COM DEPARTAMENTO
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// METODO PARA ACRESCENTAR O BOTÃO DELETE EM CADA LINHA DA TABELA COM
	// DEPARTAMENTO
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
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

	private void removeEntity(Department obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}