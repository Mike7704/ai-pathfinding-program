package application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Controller {
	
	private int gridSize = 800;
	private int gridRowsColumns = 17;
	private int cellSize = gridSize / gridRowsColumns;
	private int speed;
	private boolean running = false;
	
	private boolean setStartPosition = false;
	private boolean setEndPosition = false;
	
	// Directions
	private char direction, up, down, left, right;
	
	private Rectangle rectBackground = new Rectangle(860, 55, 710, 803);
	private GridPane grid = new GridPane();
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	private Cell startCell;
	private Cell endCell;
	private Cell currentCell;
	private Cell visitedCell;
	private Cell upNeighbour, downNeighbour, leftNeighbour, rightNeighbour;
	private int numberOfCells = 289;
	
	private Slider gridSizeSlider = new Slider(5, 30, 17);
	private Slider speedSlider = new Slider(0, 4, 1);
	
	private FlowPane resultsTable = new FlowPane();
	private ScrollPane resultsTableScroll = new ScrollPane();

	// Buttons - (text, xPos, yPos, width, height, text align, visible?)
	private GUI_Button[] button = {
		new GUI_Button("Start", 29, 10, 134, 45, Pos.CENTER, true),
		new GUI_Button("Stop", 163, 10, 134, 45, Pos.CENTER, true),
		new GUI_Button("Reset SPEED", 297, 10, 133, 45, Pos.CENTER, true),
		new GUI_Button("Set Start Position", 430, 10, 133, 45, Pos.CENTER, true),
		new GUI_Button("Set End Position", 563, 10, 134, 45, Pos.CENTER, true),
		new GUI_Button("Reset Grid", 697, 10, 134, 45, Pos.CENTER, true),
		
		new GUI_Button("Depth First Search", 880, 90, 134, 45, Pos.CENTER, true),
		new GUI_Button("Breadth First Search", 1014, 90, 134, 45, Pos.CENTER, true),
		new GUI_Button("Best First Search", 1148, 90, 134, 45, Pos.CENTER, true),
		new GUI_Button("Dijkstra", 1282, 90, 134, 45, Pos.CENTER, true),
		new GUI_Button("A* Search", 1416, 90, 134, 45, Pos.CENTER, true),
	};
	// Label - (text, xPos, yPos, width, height, font size, colour, colorBK, text align, visible?)
	private Label[] label = {
		new GUI_Label("AI Pathfinder", 1215, 30, 380, 45, 30, Color.BLACK, Color.TRANSPARENT, Pos.CENTER, true),
		new GUI_Label("Size: 17x17", 30, 865, 80, 25, 14, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Cells: 289", 750, 865, 80, 25, 14, Color.BLACK, Color.TRANSPARENT, Pos.TOP_RIGHT, true),
		new GUI_Label("Algorithm: Depth First Search", 881, 65, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Algorithm Details: Uses a ", 881, 145, 668, 85, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Time: 00:00:000", 881, 260, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Cells Visited: 0 / 289", 881, 280, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Previous Results:", 881, 320, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Information:", 881, 775, 670, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("This program allows you to run various AI pathfinding algorithms in a grid environment.\nThe AI will find the shortest path between the starting position (red cell) and end position (green cell). You can place and remove walls using left and right mouse buttons on the grid.", 881, 795, 670, 90, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
	};
	
	private Label[] resultsTableLabel = {
		new GUI_Label("Algorithm", 0, 0, 200, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true),
		new GUI_Label("Time", 0, 0, 156, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true),
		new GUI_Label("Cells Visited", 0, 0, 156, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true),
		new GUI_Label("Grid Size", 0, 0, 156, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true),
		new GUI_Label("Depth First Search", 0, 0, 200, 0, 14, Color.BLACK, Color.LIGHTGREY, Pos.CENTER, true)
	};
	
	// Timer
	private boolean resetStopwatch;
	private LongProperty timeMillis = new SimpleLongProperty(0);
	private AnimationTimer stopwatch = new AnimationTimer() {
	    private long stopped = -1 ;
	    private long startTime = stopped ;

	    @Override
	    public void handle(long timestamp) {
	        if(startTime == stopped) {
	            startTime = timestamp ;
	        }
	        long elapsedNanos = timestamp - startTime ;
	        long elapsedMillis = elapsedNanos / 1_000_000 ;
	        timeMillis.set(elapsedMillis);
	        label[5].setText("Time: " + new SimpleDateFormat("mm:ss.SSS").format(elapsedMillis));  
	        
	        // Disable buttons when running
 			for(int i = 0; i <= 10; i++) {
 				if(i != 1) {
 					button[i].setDisable(true);
 				}
 			}
 			gridSizeSlider.setDisable(true);
 			grid.setDisable(true);
 			running = true;
	    }
	    @Override
	    public void stop() {
	    	// Pause
	    	if(!resetStopwatch) {
	    		super.stop();
	    	}
	    	// Reset
	    	else {
	    		startTime = stopped ;
		        super.stop();
		        label[5].setText("Time: 00:00:000");
	    	}
	    	
	    	// Enable buttons when stopped
			for(int i = 0; i <= 10; i++) {
				button[i].setDisable(false);
			}
			gridSizeSlider.setDisable(false);
			grid.setDisable(false);
			running = false;
	    }
	};
	
	public Controller(AnchorPane root, Scene scene, Stage stage) {
		
		userAgentControl(scene);
		gridButtonFunctions();
		algorithmButtonFunctions();
		generateGrid();
		generateGridSizeSlider();
		//generateSpeedSlider();
		generateResultsTable();
		
		rectBackground.setFill(Color.LIGHTGREY);
		grid.setLayoutX(30);
		grid.setLayoutY(56);
		grid.setPrefSize(gridSize, gridSize);
		
		root.getChildren().add(rectBackground);
		root.getChildren().addAll(button);
		root.getChildren().addAll(label);
		root.getChildren().add(grid);
        root.getChildren().add(gridSizeSlider);
        //root.getChildren().add(speedSlider);
        root.getChildren().add(resultsTableScroll);
	}
	
	private void generateGrid() {
		// Remove all cells
		cells.clear();
		grid.getChildren().clear();
		
		// Generate array to store each cell in the environment
		for(int y = 0; y < gridRowsColumns; y++) {
			for(int x = 0; x < gridRowsColumns; x++) {
				// xPos, yPos, size, color, isStartPos, isEndpos , isWall, isVisited
				cells.add(new Cell(x, y, cellSize, Color.LIGHTGREY, false, false, false, false));
			}
		}
		// Add each cell to the grid and give it functions
		for(int i = 0; i < cells.size(); i++) {
			grid.add(cells.get(i), cells.get(i).getXPos(), cells.get(i).getYPos());
			gridDrawer(cells.get(i));
		}
		
		// Set top right as end position
		endCell = cells.get(gridRowsColumns-1);
		setEndPosition(cells.get(gridRowsColumns-1));
		// Set bottom left as start position
		startCell = cells.get((numberOfCells)-gridRowsColumns);
		setStartPosition(cells.get((numberOfCells)-gridRowsColumns));
	}
	
	private void getNeighbours(int xPos, int yPos) {
		if (currentCell.getYPos()>0) {
			upNeighbour = cells.get(yPos * gridRowsColumns + xPos - gridRowsColumns);
		}
		if (currentCell.getYPos()<gridRowsColumns-1) {
			downNeighbour = cells.get(yPos * gridRowsColumns + xPos + gridRowsColumns);
		}
		if (currentCell.getXPos()>0) {
			leftNeighbour = cells.get(yPos * gridRowsColumns + xPos - 1);
		}
		if (currentCell.getXPos()<gridRowsColumns-1) {
			rightNeighbour = cells.get(yPos * gridRowsColumns + xPos + 1);
		}
	}
	
	private void searchBreadthFirst() {
		
		//https://medium.com/analytics-vidhya/part-i-breadth-first-search-using-grid-dc41a5f41663
		
		/*
		// Mark all the vertices as not visited(By default
	       // set as false)
	       boolean visited[] = new boolean[V];

	       // Create a queue for BFS
	       LinkedList<Cell> queue = new LinkedList<Cell>();

	       // Mark the current node as visited and enqueue it
	       visited[currentCell.getYPos() * gridRowsColumns + currentCell.getXPos()]=true;
	       queue.add(currentCell.getYPos() * gridRowsColumns + currentCell.getXPos(),currentCell);

	       while (queue.size() != 0)
	       {
	           // Dequeue a vertex from queue and print it
	    	   currentCell = queue.poll();
	           System.out.print(currentCell+" ");

	           // Get all adjacent vertices of the dequeued vertex s
	           // If a adjacent has not been visited, then mark it
	           // visited and enqueue it
	           Iterator<Integer> i = adjacent_List[currentCell].listIterator();
	           while (i.hasNext())
	           {
	               int n = i.next();
	               if (!visited[n])
	               {
	                   visited[n] = true;
	                   queue.add(n);
	               }
	           }
	       }
	       */
	}
	
	private void searchCell() {
		
		currentCell.setColor(Color.DODGERBLUE);	
		currentCell.setVisited(true);
		
		if (currentCell == startCell) {
			currentCell.setColor(Color.RED);
		}
		if (currentCell == endCell) {
			currentCell.setColor(Color.GREEN);	
			stopwatch.stop();
		}
		
		if (!visitedCell.isStartPos) {
			visitedCell.setColor(Color.SKYBLUE);
			visitedCell.setStartPos(false);
		}
	}
	
	private void gridButtonFunctions() {
		
		// Start 
		button[0].setOnAction(event -> {
			currentCell = startCell;

			searchBreadthFirst();
			resetStopwatch = false;
			stopwatch.start();
		});
		// Stop
		button[1].setOnAction(event -> {
			resetStopwatch = false;
			stopwatch.stop();
		});
		// Reset
		button[2].setOnAction(event -> {
			resetStopwatch = true;
			stopwatch.stop();
		});	
		// Set start position
		button[3].setOnAction(event -> {
			setStartPosition = true;
			setEndPosition = false;
		});
		// Set end position
		button[4].setOnAction(event -> {
			setStartPosition = false;
			setEndPosition = true;
		});
		// Clear grid
		button[5].setOnAction(event -> generateGrid());
	}
	
	private void algorithmButtonFunctions() {
		// Depth First Search
		button[6].setOnAction(event -> {
			label[3].setText("Algorithm: Depth First Search");
		});
		// Breadth First Search
		button[7].setOnAction(event -> {
			label[3].setText("Algorithm: Breadth First Search");
		});
		// Best First Search
		button[8].setOnAction(event -> {
			label[3].setText("Algorithm: Best First Search");
		});
		// Dijkstra
		button[9].setOnAction(event -> {
			label[3].setText("Algorithm: Dijkstra");
		});
		// A* Search
		button[10].setOnAction(event -> {
			label[3].setText("Algorithm: A* Search");
		});
	}
	
	// User controls agent for testing
	private void userAgentControl(Scene scene) {
		
	scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {			
		if (running) {
			getNeighbours(currentCell.getXPos(), currentCell.getYPos());
			
			if (key.getCode() == KeyCode.UP && currentCell.getYPos()>0 && !upNeighbour.isWall) {
				visitedCell = currentCell;
				currentCell = upNeighbour;
				searchCell();
			}
			if (key.getCode() == KeyCode.DOWN && currentCell.getYPos()<gridRowsColumns-1 && !downNeighbour.isWall) {
				visitedCell = currentCell;
				currentCell = downNeighbour;
				searchCell();
			}
			if (key.getCode() == KeyCode.LEFT && currentCell.getXPos()>0 && !leftNeighbour.isWall) {
				visitedCell = currentCell;
				currentCell = leftNeighbour;
				searchCell();
			}
			if (key.getCode() == KeyCode.RIGHT && currentCell.getXPos()<gridRowsColumns-1 && !rightNeighbour.isWall) {
				visitedCell = currentCell;
				currentCell = rightNeighbour;
				searchCell();
			}
		}
	});
	
	/*
	switch (direction) {
		case 'U':
			System.out.println("UP");
			break;
		case 'D':
			break;
		case 'L':
			break;
		case 'R':
			break;
		}*/
	}
	
	private void setStartPosition(Cell cell) {
		// Remove previous start position
		/*
		for(int i = 0; i < cells.size(); i++) {
			if(cells.get(i).getIsStartPos()) {
				cells.get(i).setColor(Color.LIGHTGREY);
				cells.get(i).setStartPos(false);
				break;
			}
		}
		*/
		startCell.setStartPos(false);
		startCell.setColor(Color.LIGHTGREY);
		
		// Set new start position
		cell.setStartPos(true);
		cell.setColor(Color.RED);	
		startCell = cell;
		setStartPosition = false;
	}
	
	private void setEndPosition(Cell cell) {
		// Remove previous end position
		/*
		for(int i = 0; i < cells.size(); i++) {
			if(cells.get(i).getIsEndPos()) {
				cells.get(i).setColor(Color.LIGHTGREY);
				cells.get(i).setEndPos(false);
				break;
			}
		}
		*/
		endCell.setEndPos(false); 
		endCell.setColor(Color.LIGHTGREY);
		
		// Set new end position
		cell.setEndPos(true);
		cell.setColor(Color.GREEN);
		endCell = cell;
		setEndPosition = false;
	}
	
	private void gridDrawer(Cell cell) {		
		
		grid.setOnMouseEntered(event -> {
			grid.setCursor(Cursor.HAND);
		});
		grid.setOnMouseExited(event -> {
			grid.setCursor(Cursor.DEFAULT);
		});
		
		cell.setOnMousePressed(event -> {
			cell.setOnDragDetected(mouseEvent -> cell.startFullDrag());
			if(event.isPrimaryButtonDown() && !cell.isStartPos && !cell.isEndPos) {
				if(setStartPosition) {
					setStartPosition(cell);
				}
				else if(setEndPosition) {
					setEndPosition(cell);
				}
				else {
					cell.setColor(Color.GREY);
					cell.setWall(true);
				}
			};
			if(event.isSecondaryButtonDown() && !cell.isStartPos && !cell.isEndPos) {
				cell.setColor(Color.LIGHTGREY);
				cell.setWall(false);
			};
		});
		cell.setOnMouseDragEntered(event -> {
			if(event.isPrimaryButtonDown() && !cell.isStartPos && !cell.isEndPos) {
				cell.setColor(Color.GREY);
				cell.setWall(true);
			};
			if(event.isSecondaryButtonDown() && !cell.isStartPos && !cell.isEndPos) {
				cell.setColor(Color.LIGHTGREY);
				cell.setWall(false);
			};
		});
		
	}

	private void generateResultsTable() {

		resultsTable.setPrefSize(668, 150);
		resultsTable.setVgap(3);
		resultsTable.setFocusTraversable(false); // Disable selecting buttons using keys
		resultsTable.getChildren().addAll(resultsTableLabel);
		
		resultsTableScroll.setLayoutX(881);
		resultsTableScroll.setLayoutY(344);
		resultsTableScroll.setPrefSize(668, 160);
		resultsTableScroll.setFocusTraversable(false); // Disable selecting buttons using keys
		resultsTableScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;-fx-padding: 0;-fx-background-insets: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;-fx-border-color:darkgrey");
		resultsTableScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		resultsTableScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		resultsTableScroll.setContent(resultsTable);
	}
	
	private void generateGridSizeSlider() {
		gridSizeSlider.setPrefSize(600, 30);
		gridSizeSlider.setLayoutX(130);
		gridSizeSlider.setLayoutY(860);
		gridSizeSlider.setShowTickLabels(true);
		gridSizeSlider.setMajorTickUnit(1);
		//gridSizeSlider.setMinorTickCount(5);
		gridSizeSlider.setBlockIncrement(1);
		gridSizeSlider.setSnapToTicks(true);
		gridSizeSlider.setFocusTraversable(false); // Disable selecting buttons using keys
        
		gridSizeSlider.setOnMouseDragged(event -> {
			gridRowsColumns = (int) gridSizeSlider.getValue();
			cellSize = gridSize/gridRowsColumns;
			numberOfCells = gridRowsColumns * gridRowsColumns;
        	label[1].setText("Size: " + gridRowsColumns + "x" + gridRowsColumns);
        	label[2].setText("Cells: " + numberOfCells);
        	label[6].setText("Cells Visited: 0 / " + numberOfCells);
        	generateGrid();
		});
		
		gridSizeSlider.setOnMouseReleased(event -> {
			gridRowsColumns = (int) gridSizeSlider.getValue();
			gridSizeSlider.setValue(gridRowsColumns);
			cellSize = gridSize/gridRowsColumns;
			numberOfCells = gridRowsColumns * gridRowsColumns;
        	label[1].setText("Size: " + gridRowsColumns + "x" + gridRowsColumns);
        	label[2].setText("Cells: " + numberOfCells);
        	label[6].setText("Cells Visited: 0 / " + numberOfCells);
        	generateGrid();
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
		speedSlider.setFocusTraversable(false); // Disable selecting buttons using keys
        
		speedSlider.setOnMouseReleased(event -> {
        	//label[0].setText("Size: " + (int) speedSlider.getValue());
		});
	}
}
