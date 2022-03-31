package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class Main extends Application {
	
	// Initialise Program
	@Override
	public void start(Stage stage) {
		try {
			AnchorPane root = new AnchorPane();
			Scene scene = new Scene(root, 1600, 900);
			
			stage.setScene(scene);
			stage.setTitle("AI Pathfinder");
			stage.setResizable(false);
			stage.setFullScreen(false);
			stage.show();
			
			new Controller(root, scene, stage);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
