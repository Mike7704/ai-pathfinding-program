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
	boolean isStartPos, isEndPos, isWall, isVisited;
	
	public Cell(int xPos, int yPos, double size, Color color, boolean isStartPos, boolean isEndPos, boolean isWall, boolean isVisited) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.size = size;
		this.color = color;
		this.isStartPos = isStartPos;
		this.isEndPos = isEndPos;
		this.isWall = isWall;
		this.isVisited = isVisited;
		
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
	
	public void setStartPos(boolean value) {
		this.isStartPos = value;
	}
	
	public void setEndPos(boolean value) {
		this.isEndPos = value;
	}

	public void setWall(boolean value) {
		this.isWall = value;
	}
	
	public void setVisited(boolean value) {
		this.isVisited = value;
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
	
	public boolean getIsStartPos() {
		return isStartPos;
	}
	
	public boolean getIsEndPos() {
		return isEndPos;
	}
	
	public boolean getIsWall() {
		return isWall;
	}
	
	public boolean getIsVisitied() {
		return isVisited;
	}
}
