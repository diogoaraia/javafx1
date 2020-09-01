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


public class MainViewController implements Initializable {

	//NOME DOS MENUS
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	
	
	
	// AÇÕES OU EVENTOS DOS MENUS AO SER CLICADOS
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}

	// ACAO DE ABRIR A PAGINA DEPARTMENTLIST
	@FXML
	public void onMenuItemDepartmentAction() {
		// MOCK DE TESTE DE ACESSO A DADOS NA TABELA 
		// loadView2("/gui/DepartmentList.fxml");
		// FUNCAO LAMBDA PARA INICIALIZAR O CONTROLADOR = , (Department...
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			//CHAMAR A INICIALIZACAO DO CONTROLLER DepartmentListController
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});		
	}
	
	
	
	// ACAO DE ABRIR A PAGINA ABOUT
	@FXML
	public void onMenuItemAboutAction() {
		// x QUE LEVA A NADA, POIS SERÁ SÓ TEXTO FIXO, SEM OPERACÕES.
		loadView("/gui/About.fxml", x -> {});
	}
	
	
		
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
	
	
	// UMA FUNÇÃO APENAS PARA ABRIR OUTRA TELA SEM QUE O PROCESSAMENTO SEJA INTERROMPIDO (synchronized)
	// AÇÃO DE INICIALIZACAO GERAL
	// loadView É UMA FUNCAO DO TIPO <T>
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {	
			// CARREGAR UMA TELA
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			// PEGAR A REFERENCIA DO SCENE 
			Scene mainScene = Main.getMainScene();
			
			// COLOCAR O VBOX DO ABOUT DENTRO DO VBOX DA JANELA PRINCIPAL (MainView)
			// ACESSO AO CONTEUDO DO ScrollPane EM SEGUIDA DO VBox DA MainView
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			// REFERENCIA DO MENU
			Node mainMenu = mainVBox.getChildren().get(0);
			
			// LIMPA OS FILHO DO MainVBox
			mainVBox.getChildren().clear();
						
			// AO CLICAR EM HELP > ABOUT > ABRE A PAGINA ABOUT MANTENDO O MENU
			// ORDENAGEM DO MENU EM SEGUIDA CONTEUDO DA PAGINA QUE ESTIVER SENDO ABERTO
			// ADICIONA NO MainVBox OS FILHOS DE mainMenu E DO newVBox
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			
			// PARA ATIVAR AS FUNCÕES DE INICIALIZACAO
			/* INICIALIZA A FUNCAO CONTROLLER DE DepartmentListController
			PARA INICIALIZAR OUTRAS FUNCOES PARA OUTRAS TELAS FAZER O MESMO 
			SUBSTITUINDO O NOME controller PELO NOME DA OUTRA FUNCAO.  */
			T controller = loader.getController();		
			initializingAction.accept(controller);
			
			
		}
		catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}

		// MOCK DE TESTE DE ACESSO A DADOS NA TABELA
		/* private synchronized void loadView2(String absoluteName) {
			try {	
				// CARREGAR UMA TELA
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
				VBox newVBox = loader.load();
				
				// PEGAR A REFERENCIA DO SCENE 
				Scene mainScene = Main.getMainScene();
				
				// COLOCAR O VBOX DO ABOUT DENTRO DO VBOX DA JANELA PRINCIPAL (MainView)
				// ACESSO AO CONTEUDO DO ScrollPane EM SEGUIDA DO VBox DA MainView
				VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
				
				// REFERENCIA DO MENU
				Node mainMenu = mainVBox.getChildren().get(0);
				
				// LIMPA OS FILHO DO MainVBox
				mainVBox.getChildren().clear();
				
				// AO CLICAR EM HELP > ABOUT > ABRE A PAGINA ABOUT MANTENDO O MENU
				// ORDENAGEM DO MENU EM SEGUIDA CONTEUDO DA PAGINA QUE ESTIVER SENDO ABERTO
				// ADICIONA NO MainVBox OS FILHOS DE mainMenu E DO newVBox
				mainVBox.getChildren().add(mainMenu);
				mainVBox.getChildren().addAll(newVBox.getChildren());
				
				
				// REFERENCIA PARA ACESSAR A VIEW E O CONTROLLER
				DepartmentListController controller = loader.getController();
				// CHAMAR AS OPERACOES INJETANDO DEPENDENCIA
				controller.setDepartmentService(new DepartmentService());
				// ATUALIZAR A TABELA COM OS DADOS
				controller.updateTableView();
				
			}
			catch (IOException e) {
				Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
			} */
	