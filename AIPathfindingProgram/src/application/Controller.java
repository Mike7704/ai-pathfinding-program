package application;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Controller {
	
	private Scene scene;
	private int gridSize = 800;
	private int gridRowsColumns = 17;
	private int cellSize = gridSize / gridRowsColumns;
	private int speed;
	private boolean running = false;
	
	private char direction;
	private int goalX;
	private int goalY;
	
	private Rectangle rectBackground = new Rectangle(860, 55, 710, 803);
	private GridPane grid = new GridPane();
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	
	private Slider gridSizeSlider = new Slider(5, 30, 17);
	private Slider speedSlider = new Slider(0, 4, 1);
	
	// Buttons - (text, xPos, yPos, width, height, text align, visible?)
	private GUI_Button[] button = {
		new GUI_Button("Start", 29, 10, 134, 45, Pos.CENTER, true),
		new GUI_Button("Stop", 163, 10, 134, 45, Pos.CENTER, true),
		new GUI_Button("Reset", 297, 10, 133, 45, Pos.CENTER, true),
		new GUI_Button("Set Start Position", 430, 10, 133, 45, Pos.CENTER, true),
		new GUI_Button("Set End Position", 563, 10, 134, 45, Pos.CENTER, true),
		new GUI_Button("Clear Grid", 697, 10, 134, 45, Pos.CENTER, true),
		
		new GUI_Button("Depth First Search", 880, 90, 134, 45, Pos.CENTER, true),
		new GUI_Button("Breadth First Search", 1014, 90, 134, 45, Pos.CENTER, true),
		new GUI_Button("Best First Search", 1148, 90, 134, 45, Pos.CENTER, true),
		new GUI_Button("Dijkstra", 1282, 90, 134, 45, Pos.CENTER, true),
		new GUI_Button("A* Search", 1416, 90, 134, 45, Pos.CENTER, true),
	};
	// Label - (text, xPos, yPos, width, height, font size, colour, text align, visible?)
	private Label[] label = {
		new GUI_Label("AI PathFinder", 1215, 30, 380, 45, 30, Color.BLACK, Pos.CENTER, true),
		new GUI_Label("Size: 17x17", 40, 865, 180, 30, 14, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Algorithm:", 880, 65, 380, 30, 15, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Algorithm Details:", 880, 150, 380, 30, 15, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Time:", 880, 260, 380, 30, 15, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Cells Explored:", 880, 280, 380, 30, 15, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Previous Results:", 880, 320, 380, 30, 15, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Information:", 880, 780, 380, 30, 15, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Left click to place wall", 880, 800, 380, 30, 15, Color.BLACK, Pos.TOP_LEFT, true),
		new GUI_Label("Right click to remove wall", 880, 820, 380, 30, 15, Color.BLACK, Pos.TOP_LEFT, true)
		
	};
		
	public Controller(AnchorPane root, Scene scene, Stage stage) {
		this.scene = scene;
		
		buttonFunctions();
		generateEnvironemt();
		generateEnvironmentSizeSlider();
		//generateSpeedSlider();
		
		rectBackground.setFill(Color.LIGHTGREY);
		
		root.getChildren().add(rectBackground);
		root.getChildren().addAll(button);
		root.getChildren().addAll(label);
		root.getChildren().add(grid);
        root.getChildren().add(gridSizeSlider);
        //root.getChildren().add(speedSlider);
	}
	
	private void generateEnvironemt() {
		// Remove all cells
		cells.clear();
		grid.getChildren().clear();

		grid.setLayoutX(30);
		grid.setLayoutY(56);
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
			environmentDrawer(cells.get(i));
		}
		
		// Set top right as destination
		cells.get(gridRowsColumns-1).setGoal(true);
		
		// Set bottom left as start position
		cells.get((gridRowsColumns*gridRowsColumns)-gridRowsColumns).setAgent(true);
	}
	
	private void buttonFunctions() {
		button[0].setOnAction(event -> System.out.println("hh"));
	}
	
	// User controls agent for testing
	private void userAgentControl() {
		
	scene.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
		if (key.getCode() == KeyCode.UP) {
			direction = 'U';
		}
		if (key.getCode() == KeyCode.DOWN) {
			direction = 'D';
		}
		if (key.getCode() == KeyCode.LEFT) {
			direction = 'L';
		}
		if (key.getCode() == KeyCode.RIGHT) {
			direction = 'R';
		}
	});
		
	switch (direction) {
		case 'U':
			break;
		case 'D':
			break;
		case 'L':
			break;
		case 'R':
			break;
		}
	}
	
	private void environmentDrawer(Cell cell) {		
		
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
		gridSizeSlider.setPrefSize(600, 30);
		gridSizeSlider.setLayoutX(130);
		gridSizeSlider.setLayoutY(860);
		gridSizeSlider.setShowTickLabels(true);
		gridSizeSlider.setMajorTickUnit(1);
		//gridSizeSlider.setMinorTickCount(5);
		gridSizeSlider.setBlockIncrement(1);
		gridSizeSlider.setSnapToTicks(true);
        
		gridSizeSlider.setOnMouseDragged(event -> {
			gridRowsColumns = (int) gridSizeSlider.getValue();
			cellSize = gridSize/gridRowsColumns;
        	label[1].setText("Size: " + gridRowsColumns + "x" + gridRowsColumns);
        	generateEnvironemt();
		});
		
		gridSizeSlider.setOnMouseReleased(event -> {
			gridRowsColumns = (int) gridSizeSlider.getValue();
			gridSizeSlider.setValue(gridRowsColumns);
			cellSize = gridSize/gridRowsColumns;
        	label[1].setText("Size: " + gridRowsColumns + "x" + gridRowsColumns);
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
