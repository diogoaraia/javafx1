package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;



public class Main extends Application {
	
	private static Scene mainScene; //GUARDAR A REFERENCIA DO SCENE

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			
			mainScene = new Scene(scrollPane);
			
			// PALCO
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFX application"); //Titulo da página
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// METODO PARA PEGAR A REFERENCIA DA SCENE 
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
