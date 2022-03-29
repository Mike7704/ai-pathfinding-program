package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class GUI_Button extends Button {
	
	public GUI_Button(String text, double xPos, double yPos, double width, double height, Pos pos, boolean visible) {
		setText(text);
		// Position from center
		setLayoutX(xPos-(width/2));
		setLayoutY(yPos-(height/2));
		setPrefWidth(width);
		setPrefHeight(height);
		setAlignment(Pos.CENTER);
		setVisible(visible);
		setFocusTraversable(false); // player has to click, disable pressing space/enter
		//initializeButtonListeners();
		//setPadding(new Insets(40,40,0,0));

	}
}
