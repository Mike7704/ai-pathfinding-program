package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Controller {
	
	private GridPane environmentGrid = new GridPane();
	
	// Buttons - (text, xPos, yPos, width, height, text align, visible?)
	private Buttons[] buttons = {
		new Buttons("Button 1", 960, 430, 100, 30, Pos.CENTER, true),
		new Buttons("Button 2", 960, 460, 100, 30, Pos.CENTER, true),
		new Buttons("Button 3", 960, 490, 100, 30, Pos.CENTER, true),
		new Buttons("Button 4", 960, 520, 100, 30, Pos.CENTER, true)
	};
		
	public Controller(AnchorPane root, Scene scene, Stage stage) {
		//scene.setFill(Color.BLACK);
		
		environmentGrid.setLayoutX(40);
		environmentGrid.setLayoutY(40);
		//environmentGrid.setPrefSize(500, 500);
		//environmentGrid.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide
		//environmentGrid.getColumnConstraints().add(new ColumnConstraints(200)); // column 1 is 200 wide
		
		for(int y = 0; y < 20; y++) {
			for(int x = 0; x < 20; x++) {
				environmentGrid.add(new Rectangle(40,40,Color.DARKGREY), x, y);
			}
		}
		
		environmentGrid.setGridLinesVisible(true);
		//environmentGrid.setHgap(2);
		//environmentGrid.setVgap(2);
		
		environmentGrid.getChildren().get(5);
		
		root.getChildren().addAll(buttons);
		root.getChildren().add(environmentGrid);
	}
	
	
}
