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
	private int f, g, h; // (A*) f = total cost; g = actual cost; h(heuristic) = estimates cost
	
	public Cell(int xPos, int yPos, double size, Color color, boolean isStartPos, boolean isEndPos, boolean isWall, boolean isVisited, int f, int g, int h) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.size = size;
		this.color = color;
		this.isStartPos = isStartPos;
		this.isEndPos = isEndPos;
		this.isWall = isWall;
		this.isVisited = isVisited;
		this.f = f;
		this.g = g;
		this.h = h;
		
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
	
	// A*
	public void setF(int f) {
		this.f = f;
	}
	public void setG(int g) {
		this.g = g;
	}
	public void setH(int h) {
		this.h = h;
	}
	// A*
	public int getF() {
		return f;
	}
	public int getG() {
		return g;
	}
	public int getH() {
		return h;
	}
}
