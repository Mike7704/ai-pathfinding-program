package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GUI_Label extends Label {
	
	public GUI_Label(String text, double xPos, double yPos, double width, double height, int fontSize, Color color, Pos pos, boolean visible) {
		setText(text);
		setFont(Font.font("Verdana", FontWeight.MEDIUM, fontSize));
		setTextFill(color);
		if (pos == Pos.CENTER) {
			setLayoutX(xPos-(width/2));
			setLayoutY(yPos-(height/2));
		}
		else {
			setLayoutX(xPos);
			setLayoutY(yPos);
		}
		setPrefWidth(width);
		setPrefHeight(height);
		setAlignment(pos);
		
		//setStyle("-fx-font-size: 30;");
		//setPadding(new Insets(40,40,40,40));
		setWrapText(true);
		//initializeButtonListeners();
		setVisible(visible);
	}
}
