package application;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Cell extends StackPane {
	
	private Rectangle cell = new Rectangle();
	private int xPos, yPos;
	private double size;
	private Color color;
	boolean isAgent, isGoal, isWall;
	
	public Cell(int xPos, int yPos, double size, Color color, boolean isAgent, boolean isGoal, boolean isWall) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.size = size;
		this.color = color;
		this.isAgent = isAgent;
		this.isGoal = isGoal;
		this.isWall = isWall;
		
		cell.setWidth(size);
		cell.setHeight(size);
		cell.setFill(color);
		// Add outline to each cell of equal thickness
		cell.setStroke(Color.BLACK);
		cell.setStrokeType(StrokeType.OUTSIDE);
		
		// Add min size to stack pane so grid pane stays same size
		setMinSize(size, size);
		getChildren().addAll(cell);
		
	}
	
	public void setAgent(boolean value) {
		this.isAgent = value;
		this.setColor(Color.YELLOW);
	}
	
	public void setGoal(boolean value) {
		this.isGoal = value;
		this.setColor(Color.GREEN);
	}

	public void setWall(boolean value) {
		this.isWall = value;
	}
	
	public void setColor(Color color) {
		cell.setFill(color);
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public Color getColor() {
		return color;
	}
	
	public boolean getIsAgent() {
		return isAgent;
	}
	
	public boolean getIsGoal() {
		return isGoal;
	}
	
	public boolean getIsWall() {
		return isWall;
	}
}
