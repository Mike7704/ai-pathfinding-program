package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GUI_Button extends Button {
	
	public GUI_Button(String text, double xPos, double yPos, double width, double height, Pos pos, boolean visible) {
		setText(text);
		setFont(Font.font("Arial", FontWeight.NORMAL, 12));
		setLayoutX(xPos);
		setLayoutY(yPos);
		setPrefWidth(width);
		setPrefHeight(height);
		setAlignment(pos);
		setVisible(visible);
		setFocusTraversable(false); // Disable selecting buttons using keys
		
		
		//setStyle("-fx-background-color: linear-gradient(white, lightgrey);-fx-border-color:lightgrey;-fx-border-radius: 0.5;");

		setOnMousePressed(event -> {
			//setStyle("-fx-background-color: linear-gradient(white, lightgrey);-fx-border-color:black;-fx-border-radius: 1;");
		});
		
	}
}