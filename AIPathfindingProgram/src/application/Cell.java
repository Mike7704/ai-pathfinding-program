package application;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Cell extends StackPane {
	
	private Rectangle cell = new Rectangle();
	private int xPos, yPos;
	private double size;
	private Color color;
	public boolean isStartPos, isEndPos, isWall, isWeight, isVisited;
	private Cell previousCell;
	private int weightValue, costF, costG, costH; // (A*) f = g + h; g = distance from start to current cell; h = distance from current cell to goal
	private GUI_Label costFLabel, costGLabel, costHLabel;
		
	public Cell(int xPos, int yPos, double size, Color color, boolean isStartPos, boolean isEndPos, boolean isWall, boolean isWeight, boolean isVisited, Cell previousCell, int weightValue, int costF, int costG, int costH) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.size = size;
		this.color = color;
		this.isStartPos = isStartPos;
		this.isEndPos = isEndPos;
		this.isWall = isWall;
		this.isWeight = isWeight;
		this.isVisited = isVisited;
		this.previousCell = previousCell;
		this.weightValue = weightValue;
		this.costF = costF;
		this.costG = costG;
		this.costH = costH;
		
		cell.setWidth(size);
		cell.setHeight(size);
		cell.setFill(color);
		// Add outline to each cell of equal thickness
		cell.setStroke(Color.BLACK);
		cell.setStrokeType(StrokeType.OUTSIDE);
		
		// Add min size to stack pane so grid pane stays same size
		setMinSize(size, size);
		getChildren().addAll(cell);
		
		costGLabel = new GUI_Label(costG + "", 0, 0, size*0.8, size*0.8, size*0.25, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true);
		costHLabel = new GUI_Label(costH + "", 0, 0, size*0.8, size*0.8, size*0.25, Color.BLACK, Color.TRANSPARENT, Pos.TOP_RIGHT, true);
		costFLabel = new GUI_Label(costF + "", 0, 0, size*0.8, size*0.8, size*0.25, Color.BLACK, Color.TRANSPARENT, Pos.BOTTOM_CENTER, true);	
		costGLabel.setOpacity(0.5);
		costHLabel.setOpacity(0.5);
		costFLabel.setOpacity(0.5);
		//getChildren().addAll(costGLabel,costHLabel,costFLabel);
		
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
	
	public void setPreviousCell(Cell cell) {
		this.previousCell = cell;
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
	
	public Cell getPreviousCell() {
		return previousCell;
	}
	
	// Weight
	public void setWeightValue(int weightValue) {
		this.weightValue = weightValue;
	}
	
	public int getWeightValue() {
		return weightValue;
	}
	
	public void setWeight(boolean value) {
		this.isWeight = value;
	}
	
	public boolean getIsWeight() {
		return isWeight;
	}
	
	// A* cost values
	public void setCostF() {
		this.costF = costG + costH;
		costFLabel.setText(costF + "");
	}
	public void setCostG(int costG) {
		this.costG = costG;
		costGLabel.setText(costG + "");
		setCostF();
	}
	public void setCostH(int costH) {
		this.costH = costH;
		costHLabel.setText(costH + "");
		setCostF();
	}
	// A*
	public int getCostF() {
		return costF;
	}
	public int getCostG() {
		return costG;
	}
	public int getCostH() {
		return costH;
	}
	
	// Show/hide costs
	public void showCostF(boolean show) {
		getChildren().remove(costFLabel);
		if(show) {
			getChildren().add(costFLabel);
		}
	}
	public void showCostG(boolean show) {
		getChildren().remove(costGLabel);
		if(show) {
			getChildren().add(costGLabel);
		}
	}
	public void showCostH(boolean show) {
		getChildren().remove(costHLabel);
		if(show) {
			getChildren().add(costHLabel);
		}
	}
}
