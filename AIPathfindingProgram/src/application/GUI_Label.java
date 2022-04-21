package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GUI_Label extends Label {
	
	public GUI_Label(String text, double xPos, double yPos, double width, double height, double fontSize, Color color, Color colorBK, Pos pos, boolean visible) {
		setText(text);
		setFont(Font.font("Arial", FontWeight.NORMAL, fontSize));
		setTextFill(color);
		setBackground(new Background(new BackgroundFill(colorBK, null, null)));
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
		setWrapText(true);
		setVisible(visible);
	}
}

