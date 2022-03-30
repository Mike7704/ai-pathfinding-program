package application;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller {
	
	private int gridSize = 800;
	private int gridRowsColumns = 20;
	private int cellSize = gridSize / gridRowsColumns;
	private int speed;
	private boolean running = false;
	
	private char direction;
	private int goalX;
	private int goalY;
	
	private GridPane grid = new GridPane();
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	
	private Slider gridSizeSlider = new Slider(5, 30, 15);
	private Slider speedSlider = new Slider(0, 4, 1);
	
	// Buttons - (text, xPos, yPos, width, height, text align, visible?)
	private GUI_Button[] button = {
		new GUI_Button("Start", 930, 100, 140, 30, Pos.CENTER, true),
		new GUI_Button("Stop", 930, 130, 140, 30, Pos.CENTER, true),
		new GUI_Button("Reset", 930, 160, 140, 30, Pos.CENTER, true),
		
		new GUI_Button("Depth First Search", 930, 300, 140, 30, Pos.CENTER, true),
		new GUI_Button("Breadth First Search", 930, 330, 140, 30, Pos.CENTER, true),
		new GUI_Button("Best First Search", 930, 360, 140, 30, Pos.CENTER, true),
		new GUI_Button("Dijkstra", 930, 390, 140, 30, Pos.CENTER, true),
		new GUI_Button("A* Search", 930, 420, 140, 30, Pos.CENTER, true),
	};
	// Label - (text, xPos, yPos, width, height, font size, colour, text align, visible?)
	private Label[] label = {
		new GUI_Label("Size: 15x15", 910, 762, 180, 30, 20, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Left click to place wall", 910, 560, 180, 30, 20, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Right click to remove wall", 910, 590, 180, 30, 20, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Double left click to place goal", 910, 620, 180, 30, 20, Color.BLACK, Pos.TOP_LEFT, true)
		
	};
		
	public Controller(AnchorPane root, Scene scene, Stage stage) {
		
		generateEnvironemt();
		generateEnvironmentSizeSlider();
		generateSpeedSlider();
		
		root.getChildren().addAll(button);
		root.getChildren().addAll(label);
		root.getChildren().add(grid);
        root.getChildren().add(gridSizeSlider);
        root.getChildren().add(speedSlider);
	}
	
	private void generateEnvironemt() {
		// Remove all cells
		cells.clear();
		grid.getChildren().clear();

		grid.setLayoutX(40);
		grid.setLayoutY(40);
		grid.setPrefSize(gridSize, gridSize);
		
		// Generate array to store each cell in the environment
		for(int y = 0; y < gridRowsColumns; y++) {
			for(int x = 0; x < gridRowsColumns; x++) {
				// xPos, yPos, size, color, isGoal, isWall
				cells.add(new Cell(x, y, cellSize, Color.DARKGREY, false, false, false));
			}
		}
		// Add each cell to the grid and give it functions
		for(int i = 0; i < cells.size(); i++) {
			grid.add(cells.get(i), cells.get(i).getXPos(), cells.get(i).getYPos());
			inputEventsManager(cells.get(i));
		}
		
		// Set top right as destination
		cells.get(gridRowsColumns-1).setGoal(true);
		
		// Set top right as destination
		cells.get(gridRowsColumns*gridRowsColumns-gridRowsColumns).setAgent(true);
	}
	
	private void movementManager() {
		
	}
	
	private void inputEventsManager(Cell cell) {		
		
		grid.setOnMouseEntered(event -> {
			grid.setCursor(Cursor.HAND);
		});
		grid.setOnMouseExited(event -> {
			grid.setCursor(Cursor.DEFAULT);
		});
		
		cell.setOnMousePressed(event -> {
			cell.setOnDragDetected(mouseEvent -> cell.startFullDrag());
			if(event.isPrimaryButtonDown() && !cell.isGoal && !cell.isAgent) {
				cell.setColor(Color.RED);
				cell.setWall(true);
			};
			if(event.isSecondaryButtonDown() && !cell.isGoal && !cell.isAgent) {
				cell.setColor(Color.DARKGREY);
				cell.setWall(false);
			};
		});
		cell.setOnMouseDragEntered(event -> {
			if(event.isPrimaryButtonDown() && !cell.isGoal && !cell.isAgent) {
				cell.setColor(Color.RED);
				cell.setWall(true);
			};
			if(event.isSecondaryButtonDown() && !cell.isGoal && !cell.isAgent) {
				cell.setColor(Color.DARKGREY);
				cell.setWall(false);
			};
		});
		
	}

	private void generateEnvironmentSizeSlider() {
		gridSizeSlider.setPrefSize(400, 30);
		gridSizeSlider.setLayoutX(990);
		gridSizeSlider.setLayoutY(762);
		gridSizeSlider.setShowTickLabels(true);
		gridSizeSlider.setMajorTickUnit(5);
		gridSizeSlider.setMinorTickCount(5);
		gridSizeSlider.setBlockIncrement(5);
		gridSizeSlider.setSnapToTicks(true);
        
		gridSizeSlider.setOnMouseDragged(event -> {
			gridRowsColumns = (int) gridSizeSlider.getValue();
			cellSize = gridSize/gridRowsColumns;
        	label[0].setText("Size: " + gridRowsColumns + "x" + gridRowsColumns);
        	generateEnvironemt();
		});
		
		gridSizeSlider.setOnMouseReleased(event -> {
			gridRowsColumns = (int) gridSizeSlider.getValue();
			cellSize = gridSize/gridRowsColumns;
        	label[0].setText("Size: " + gridRowsColumns + "x" + gridRowsColumns);
        	generateEnvironemt();
		});
	}
	
	private void generateSpeedSlider() {
		speedSlider.setPrefSize(400, 30);
		speedSlider.setLayoutX(1080);
		speedSlider.setLayoutY(192);
		speedSlider.setShowTickLabels(true);
		speedSlider.setMajorTickUnit(0.5);
		speedSlider.setMinorTickCount(0);
		speedSlider.setBlockIncrement(0.5);
		speedSlider.setSnapToTicks(true);
        
		speedSlider.setOnMouseReleased(event -> {
        	//label[0].setText("Size: " + (int) speedSlider.getValue());
		});
	}
}
