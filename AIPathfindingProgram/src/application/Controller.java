package application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller {
	
	// Grid setup
	private Rectangle rectBackground = new Rectangle(860, 55, 710, 803);
	private GridPane grid = new GridPane();
	private int gridSize = 800;
	private int gridRowsColumns = 15;
	private int cellSize = gridSize / gridRowsColumns;
	private String selectedAlgorithm = "Random Search";
	private boolean running = false;
	private Timeline simTimer;
	private int simSpeed = 100;
	private int weightValue = 1;
	private boolean setStartPosition = false;
	private boolean setEndPosition = false;
	// Cell properties
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	private ArrayList<Cell> currentCellNeighbours = new ArrayList<Cell>();
	private Cell startCell;
	private Cell endCell;
	private Cell currentCell;
	private Cell upNeighbour, downNeighbour, leftNeighbour, rightNeighbour;
	// Counts
	private int numberOfCells = 225;
	private int numberOfVisitedCells = 0;
	private int numberOfIterations = 0;
	private int numberOfWalls = 0;
	private int pathLength = 0;
	
	private Slider gridSizeSlider = new Slider(5, 30, 15);	
	private FlowPane resultsTable = new FlowPane();
	private ScrollPane resultsTableScroll = new ScrollPane();

	// Buttons - (text, xPos, yPos, width, height, text align, visible?)
	private GUI_Button[] button = {
		new GUI_Button("Start", 29, 10, 134, 45, Pos.CENTER, true),
		new GUI_Button("Stop", 163, 10, 134, 45, Pos.CENTER, true),
		new GUI_Button("Speed: 1", 297, 10, 133, 45, Pos.CENTER, true),
		new GUI_Button("Set Start Position", 430, 10, 133, 45, Pos.CENTER, true),
		new GUI_Button("Set End Position", 563, 10, 134, 45, Pos.CENTER, true),
		new GUI_Button("Reset Grid", 697, 10, 134, 45, Pos.CENTER, true),
		
		new GUI_Button("Random Search", 880, 95, 134, 45, Pos.CENTER, true),
		new GUI_Button("Depth First Search", 1014, 95, 134, 45, Pos.CENTER, true),
		new GUI_Button("Breadth First Search", 1148, 95, 134, 45, Pos.CENTER, true),
		new GUI_Button("Dijkstra", 1282, 95, 134, 45, Pos.CENTER, true),
		new GUI_Button("A* Search", 1416, 95, 134, 45, Pos.CENTER, true),
		
		new GUI_Button("Remove Last Result", 1416, 753, 134, 25, Pos.CENTER, true),
	};
	// Label - (text, xPos, yPos, width, height, font size, colour, colorBK, text align, visible?)
	private Label[] label = {
		new GUI_Label("AI Pathfinder", 1215, 30, 380, 45, 30, Color.BLACK, Color.TRANSPARENT, Pos.CENTER, true),
		new GUI_Label("Size: 15x15", 30, 863, 80, 25, 14, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Cells: 225", 750, 863, 80, 25, 14, Color.BLACK, Color.TRANSPARENT, Pos.TOP_RIGHT, true),
		new GUI_Label("Algorithm: " + selectedAlgorithm, 881, 70, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("The Random Search algorithm involves moving to a neighbouring cell at random. It doesn't guarantee the shortest path and is not efficient due to its randomness which often results in moving away from the end cell or revisiting cells needlessly.", 881, 150, 668, 85, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Time: 00:00:000", 881, 250, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Cells Visited: " + numberOfVisitedCells + " / " + numberOfCells, 881, 290, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Walls: " + numberOfWalls, 881, 310, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Path Length: " + pathLength, 881, 230, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Iterations: " + numberOfIterations, 881, 270, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Previous Results:", 881, 350, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Show Cell Labels:", 1216, 230, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("[F = Bottom] [G = Top Left] [H = Top Right]", 1216, 310, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("Information:", 881, 770, 380, 25, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
		new GUI_Label("This program allows you to run and monitor various AI pathfinding algorithms in a grid environment.\nThe AI will find a path between the starting position (red cell) and end position (green cell).\nInput: [Left Click = Place Wall] [Mouse Wheel = Add Weight] [Right Click = Remove Wall/Weight].", 881, 790, 670, 90, 15, Color.BLACK, Color.TRANSPARENT, Pos.TOP_LEFT, true),
	};
	// Results table, needs to be dynamic as we will be adding entries
	private ArrayList<GUI_Label> resultsTableLabel = new ArrayList<GUI_Label>();
	
	private CheckBox[] costCheckBoxes = {
		new CheckBox("F Cost - Total cost of G and H"),
		new CheckBox("G Cost - Distance from start to current cell"),
		new CheckBox("H Cost - Distance from current cell to end")
	};
	
	// Timer
	String totalTime;
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
	        long elapsedMillis = elapsedNanos / (simSpeed*10000);
	        timeMillis.set(elapsedMillis);
	        totalTime = new SimpleDateFormat("mm:ss.SSS").format(elapsedMillis);
	        label[5].setText("Time: " + totalTime);
	        
	        // Disable buttons when running
 			for(int i = 0; i <= button.length-2; i++) {
 				if(i != 1) {
 					button[i].setDisable(true);
 				}
 			}
 			button[1].setDisable(false);
 			gridSizeSlider.setDisable(true);
 			grid.setDisable(true);
 			running = true;
	    }
	    @Override
	    public void stop() {
    		startTime = stopped ;
	        super.stop();
	    	
	    	// Enable buttons when stopped
			for(int i = 0; i <= button.length-2; i++) {
				button[i].setDisable(false);
			}
			button[1].setDisable(true);
			gridSizeSlider.setDisable(false);
			grid.setDisable(false);
			running = false;
	    }
	};
	
	// Setup GUI
	public Controller(AnchorPane root, Scene scene, Stage stage) {
		
		userAgentControl(scene);
		gridButtonFunctions();
		algorithmButtonFunctions();
		generateCheckBoxes();
		generateGrid();
		generateGridSizeSlider();
		generateResultsTable();
		
		rectBackground.setFill(Color.LIGHTGREY);
		grid.setLayoutX(30);
		grid.setLayoutY(56);
		grid.setPrefSize(gridSize, gridSize);
		
		root.getChildren().add(rectBackground);
		root.getChildren().addAll(button);
		root.getChildren().addAll(label);
        root.getChildren().addAll(costCheckBoxes);
		root.getChildren().add(grid);
        root.getChildren().add(gridSizeSlider);
        root.getChildren().add(resultsTableScroll);
	}
	
	private void generateGrid() {
		// Remove/reset all cells
		cells.clear();
		grid.getChildren().clear();
		numberOfWalls = 0;
		label[7].setText("Walls: " + numberOfWalls);
		
		// Generate array to store each cell in the environment
		for(int y = 0; y < gridRowsColumns; y++) {
			for(int x = 0; x < gridRowsColumns; x++) {
				// xPos, yPos, size, colour, isStartPos, isEndpos , isWall, isVisited, isWeight, previous cell, weightValue, f cost, g cost, h cost
				cells.add(new Cell(x, y, cellSize, Color.LIGHTGREY, false, false, false, false, false, null, 1, 0, 1, 0));
			}
		}
		
		// Set top right as end position
		endCell = cells.get(gridRowsColumns-1);
		setEndPosition(cells.get(gridRowsColumns-1));
		// Set bottom left as start position
		startCell = cells.get((numberOfCells)-gridRowsColumns);
		setStartPosition(cells.get((numberOfCells)-gridRowsColumns));
				
		for(int i = 0; i < cells.size(); i++) {
			// Calculate H cost of each cell
			calculateCostH(cells.get(i));
			// Add each cell to the grid and give it input functions
			grid.add(cells.get(i), cells.get(i).getXPos(), cells.get(i).getYPos());
			gridDrawer(cells.get(i));
		}	
		showCellCostLabels();
	}
	
	// Calculate h cost/distance of cell
	private void calculateCostH(Cell cell) {
		cell.setCostH(getDistance(cell, endCell));
	}	
	// Manhattan (up, down, left, right)
	private int getDistance(Cell from, Cell to) {
		return Math.abs(from.getYPos() - to.getYPos()) + Math.abs(from.getXPos() - to.getXPos());
	}
	
	private void clearVisitedCells() {
		// Clear visited cells and path from last run
		for(int i = 0; i < cells.size(); i++) {
			cells.get(i).setVisited(false);
			cells.get(i).setPreviousCell(null);
			if (!(cells.get(i).getIsStartPos() || cells.get(i).getIsEndPos() || cells.get(i).getIsWall())) {
				cells.get(i).setColor(Color.LIGHTGREY);
			}
			if (!cells.get(i).getIsWeight() && !(cells.get(i) == startCell)) {
				cells.get(i).setCostG(1);
			}
			if (cells.get(i).getIsWeight()) {
				cells.get(i).setCostG(cells.get(i).getWeightValue());
				cells.get(i).setColor(Color.web("#FFBE72"));
			}
		}
		numberOfVisitedCells = 0;
		pathLength = 0;
		numberOfIterations = 0;
	}
	
	// Get surrounding cells from the current cell
	private void getNeighbours(Cell cell, int xPos, int yPos) {
		upNeighbour = cell;
		downNeighbour = cell;
		leftNeighbour = cell;
		rightNeighbour = cell;
		if (cell.getYPos()>0) {
			upNeighbour = cells.get(yPos * gridRowsColumns + xPos - gridRowsColumns);
		}
		if (cell.getYPos()<gridRowsColumns-1) {
			downNeighbour = cells.get(yPos * gridRowsColumns + xPos + gridRowsColumns);
		}
		if (cell.getXPos()>0) {
			leftNeighbour = cells.get(yPos * gridRowsColumns + xPos - 1);
		}
		if (cell.getXPos()<gridRowsColumns-1) {
			rightNeighbour = cells.get(yPos * gridRowsColumns + xPos + 1);
		}
		currentCellNeighbours.clear();
		currentCellNeighbours.add(upNeighbour);
		currentCellNeighbours.add(downNeighbour);
		currentCellNeighbours.add(leftNeighbour);
		currentCellNeighbours.add(rightNeighbour);
	}
	
	private void getPath() {
		// Draw path from end to start by following previous/parent cells
		while(currentCell != startCell) {
			currentCell.setColor(Color.YELLOW);
			currentCell = currentCell.getPreviousCell();
			pathLength++;
			
			// Account for weight
			if(selectedAlgorithm.equals("Dijkstra") || selectedAlgorithm.equals("A* Search")) {
				pathLength = endCell.getCostG();
			}		
			label[8].setText("Path Length: " + pathLength);
		}
	}
	
	// Checks cell properties
	private void searchCell(Cell cell) {
				
		// Track number of visited cells
		if (!cell.getIsVisited()) {
			cell.setColor(Color.DEEPSKYBLUE);	
			cell.setVisited(true);
			cell.setPreviousCell(currentCell);
			numberOfVisitedCells++;
			label[6].setText("Cells Visited: " + numberOfVisitedCells + " / " + numberOfCells);
		}
		
		// Reached the end so stop and record the results to the table
		if (cell == endCell) {
			cell.setColor(Color.GREEN);
			pathLength++;
			getPath();
			stopwatch.stop();
			simTimer.stop();
			updateResultsTable();
		}
		
		// Keep current cell visible
		cell.setColor(Color.ORANGE);
		currentCell.setColor(Color.SKYBLUE);
		currentCell = cell;
		// Keep start and end cell visible
		startCell.setColor(Color.RED);
		endCell.setColor(Color.GREEN);
		
		// Track number of iterations
		numberOfIterations += 1;
		label[9].setText("Iterations: " + numberOfIterations);
	}

	// AI will randomly move around the grid
	private void searchRandom() {	
		
		simTimer = new Timeline(new KeyFrame(Duration.millis(simSpeed), event -> {
    		getNeighbours(currentCell, currentCell.getXPos(), currentCell.getYPos());
			
			// Get random direction
			int randomInt = (int) (Math.random() * 4);
			if(randomInt == 0 && currentCell.getYPos()>0 && !upNeighbour.getIsWall()) {
				searchCell(upNeighbour);
			}
			else if(randomInt == 1 && currentCell.getYPos()<gridRowsColumns-1 && !downNeighbour.getIsWall()) {
				searchCell(downNeighbour);
			}
			else if(randomInt == 2 && currentCell.getXPos()>0 && !leftNeighbour.getIsWall()) {
				searchCell(leftNeighbour);
			}
			else if(randomInt == 3 && currentCell.getXPos()<gridRowsColumns-1 && !rightNeighbour.getIsWall()) {
				searchCell(rightNeighbour);
			}
			
		}));
		simTimer.setCycleCount(-1);
		simTimer.play();
	}
	
	private void searchDepthFirst() {
		
		// Uses a stack to track encountered but unvisited cells
		Stack<Cell> stack = new Stack<Cell>(); 
		// Add starting cell to stack
		stack.push(startCell);
		
		simTimer = new Timeline(new KeyFrame(Duration.millis(simSpeed), event -> {	
			
			// Are there any cells to search
			if(!stack.isEmpty()) {				
				
				currentCell = stack.pop();
				currentCell.setColor(Color.SKYBLUE);
				
				// Reached the end so stop and record the results to the table
				if(currentCell == endCell) {
					endCell.setColor(Color.GREEN);					
					getPath();					
					stopwatch.stop();
					simTimer.stop();
					updateResultsTable();
				}
				// Search algorithm
				if(!currentCell.getIsVisited()) {
					currentCell.setVisited(true);
					// Track number of visited cells
					numberOfVisitedCells++;
					label[6].setText("Cells Visited: " + numberOfVisitedCells + " / " + numberOfCells);
					
					// Get surrounding cells
					getNeighbours(currentCell, currentCell.getXPos(), currentCell.getYPos());
					// Search each neighbour
					for (int i = 0; i < currentCellNeighbours.size(); i++) {
						// Add neighbouring cell to the queue if not visited or a wall and store previous cell
						if(!currentCellNeighbours.get(i).getIsVisited() && !currentCellNeighbours.get(i).getIsWall()) {
							currentCellNeighbours.get(i).setPreviousCell(currentCell);
							currentCellNeighbours.get(i).setColor(Color.DEEPSKYBLUE);
							stack.push(currentCellNeighbours.get(i));
						}
					}
				}
				// Highlight current cell
				if(!stack.isEmpty() && running) {
					stack.lastElement().setColor(Color.ORANGE);
				}	
				// Keep the start and end cell visible
				startCell.setColor(Color.RED);
				endCell.setColor(Color.GREEN);
				
				// Track number of iterations
				numberOfIterations += 1;
				label[9].setText("Iterations: " + numberOfIterations);
			}
			// No cells to search so stop
			else {
				stopwatch.stop();
				simTimer.stop();
				label[8].setText("Path Length: No Path Found");
			}
			
		}));
		simTimer.setCycleCount(-1);
		simTimer.play();
	}
	
	private void searchBreadthFirst() {
		
		// Uses a queue to track encountered but unvisited cells
		Queue<Cell> queue = new LinkedList<Cell>(); 
		// Add starting cell to queue
		queue.add(startCell);
		
		simTimer = new Timeline(new KeyFrame(Duration.millis(simSpeed), event -> {	
			
			// Are there any cells to search
			if(!queue.isEmpty()) {				
				
				currentCell = queue.remove();
				currentCell.setColor(Color.SKYBLUE);
				
				// Reached the end so stop and record the results to the table
				if (currentCell == endCell) {
					endCell.setColor(Color.GREEN);					
					getPath();					
					stopwatch.stop();
					simTimer.stop();
					updateResultsTable();
				}
				
				// Get surrounding cells
				getNeighbours(currentCell, currentCell.getXPos(), currentCell.getYPos());
				// Search each neighbour
				for (int i = 0; i < currentCellNeighbours.size(); i++) {
					// Add neighbouring cell to the queue if not visited or a wall and store previous cell
					if(!currentCellNeighbours.get(i).getIsVisited() && !currentCellNeighbours.get(i).getIsWall() && !(currentCellNeighbours.get(i)==startCell)) {
						currentCellNeighbours.get(i).setVisited(true);
						currentCellNeighbours.get(i).setPreviousCell(currentCell);
						currentCellNeighbours.get(i).setColor(Color.DEEPSKYBLUE);
						queue.add(currentCellNeighbours.get(i));
						
						// Track number of visited cells
						numberOfVisitedCells++;
						label[6].setText("Cells Visited: " + numberOfVisitedCells + " / " + numberOfCells);	
					}
				}
				
				// Highlight current cell
				if(!queue.isEmpty() && running) {
					queue.peek().setColor(Color.ORANGE);
				}
				// Keep the start and end cell visible
				startCell.setColor(Color.RED);
				endCell.setColor(Color.GREEN);
				
				// Track number of iterations
				numberOfIterations += 1;
				label[9].setText("Iterations: " + numberOfIterations);
			}
			// No cells to search so stop
			else {
				stopwatch.stop();
				simTimer.stop();
				label[8].setText("Path Length: No Path Found");
			}		
		}));
		simTimer.setCycleCount(-1);
		simTimer.play();
	}
	
	private void searchDijkstra() {
		
		// Uses a priority queue to follow path with best cost by comparing costs of cells
		PriorityQueue<Cell> queue = new PriorityQueue<Cell>(numberOfCells, (cellA,cellB) -> cellA.getCostG() - cellB.getCostG());
		// Add starting cell to queue
		queue.add(startCell);
		
		simTimer = new Timeline(new KeyFrame(Duration.millis(simSpeed), event -> {	
			
			// Are there any cells to search
			if(!queue.isEmpty()) {				
				
				currentCell = queue.peek();
				currentCell.setColor(Color.SKYBLUE);

				// Reached the end so stop and record the results to the table
				if (currentCell == endCell) {
					endCell.setColor(Color.GREEN);					
					getPath();					
					stopwatch.stop();
					simTimer.stop();
					updateResultsTable();
				}
				
				// Get surrounding cells
				getNeighbours(currentCell, currentCell.getXPos(), currentCell.getYPos());
				// Search each neighbour
				for (int i = 0; i < currentCellNeighbours.size(); i++) {
					// Add neighbouring cell to the queue if not visited or a wall and store previous cell
					if(!currentCellNeighbours.get(i).getIsVisited() && !currentCellNeighbours.get(i).getIsWall()&& !(currentCellNeighbours.get(i)==startCell)) {
						
						currentCellNeighbours.get(i).setVisited(true);
						currentCellNeighbours.get(i).setColor(Color.DEEPSKYBLUE);
						currentCellNeighbours.get(i).setPreviousCell(currentCell);
						currentCellNeighbours.get(i).setCostG(currentCell.getCostG() + currentCellNeighbours.get(i).getCostG());
						queue.add(currentCellNeighbours.get(i));

						// Show weighted cells
						if(currentCellNeighbours.get(i).getIsWeight()) {
							currentCellNeighbours.get(i).setColor(Color.ROYALBLUE);
						}
						// Track number of visited cells
						numberOfVisitedCells++;
						label[6].setText("Cells Visited: " + numberOfVisitedCells + " / " + numberOfCells);						
					}
					queue.remove(currentCell);
				}
				// Highlight current cell
				if(!queue.isEmpty()) {
					queue.peek().setColor(Color.ORANGE);
				}
				// Keep the start cell visible
				startCell.setColor(Color.RED);
				endCell.setColor(Color.GREEN);
				
				// Track number of iterations
				numberOfIterations += 1;
				label[9].setText("Iterations: " + numberOfIterations);
			}
			// No cells to search so stop
			else {
				stopwatch.stop();
				simTimer.stop();
				label[8].setText("Path Length: No Path Found");
			}
			
		}));
		simTimer.setCycleCount(-1);
		simTimer.play();
	}
	
	private void searchAStar() {

		// Uses a priority queue to follow path with best cost by comparing costs of cells
		PriorityQueue<Cell> openList = new PriorityQueue<Cell>(numberOfCells, (cellA,cellB) -> cellA.getCostF() - cellB.getCostF());
		// Cells visited
		PriorityQueue<Cell> closedList = new PriorityQueue<Cell>(numberOfCells, (cellA,cellB) -> cellA.getCostF() - cellB.getCostF());
		
		// Add starting cell to queue
		openList.add(startCell);
		
		simTimer = new Timeline(new KeyFrame(Duration.millis(simSpeed), event -> {	
			
			// Are there any cells to search
			if(!openList.isEmpty()) {				
				
				currentCell = openList.peek();
				currentCell.setColor(Color.SKYBLUE);

				// Reached the end so stop and record the results to the table
				if (currentCell == endCell) {
					endCell.setColor(Color.GREEN);					
					getPath();					
					stopwatch.stop();
					simTimer.stop();
					updateResultsTable();
				}

				// Get surrounding cells
				getNeighbours(currentCell, currentCell.getXPos(), currentCell.getYPos());
				// Search each neighbour
				for (int i = 0; i < currentCellNeighbours.size(); i++) {
					// Add neighbouring cell to the queue if not visited or a wall and store previous cell
					if(!currentCellNeighbours.get(i).getIsVisited() && !currentCellNeighbours.get(i).getIsWall() && !(currentCellNeighbours.get(i)==startCell)) {
								
						if(!openList.contains(currentCellNeighbours.get(i)) && !closedList.contains(currentCellNeighbours.get(i))) {
							currentCellNeighbours.get(i).setVisited(true);
							currentCellNeighbours.get(i).setColor(Color.DEEPSKYBLUE);
							currentCellNeighbours.get(i).setPreviousCell(currentCell);
							currentCellNeighbours.get(i).setCostG(currentCell.getCostG() + currentCellNeighbours.get(i).getCostG());
							openList.add(currentCellNeighbours.get(i));
						}
						
						else {
							if(currentCellNeighbours.get(i).getCostG() > currentCell.getCostG() + currentCellNeighbours.get(i).getCostG()) {
								currentCellNeighbours.get(i).setVisited(true);
								currentCellNeighbours.get(i).setColor(Color.BLUE);
								currentCellNeighbours.get(i).setPreviousCell(currentCell);
								currentCellNeighbours.get(i).setCostG(currentCell.getCostG() + currentCellNeighbours.get(i).getCostG());
								
								if(closedList.contains(currentCellNeighbours.get(i))) {
									closedList.remove(currentCellNeighbours.get(i));
									openList.add(currentCellNeighbours.get(i));
								}
							}
						}
						
						// Show weighted cells
						if(currentCellNeighbours.get(i).getIsWeight()) {
							currentCellNeighbours.get(i).setColor(Color.ROYALBLUE);
						}
						// Track number of visited cells
						numberOfVisitedCells++;
						label[6].setText("Cells Visited: " + numberOfVisitedCells + " / " + numberOfCells);						
					}
					openList.remove(currentCell);
			        closedList.add(currentCell);
				}
				// Highlight current cell
				if(!openList.isEmpty()) {
					openList.peek().setColor(Color.ORANGE);
				}
				// Keep the start cell visible
				startCell.setColor(Color.RED);
				endCell.setColor(Color.GREEN);
				
				// Track number of iterations
				numberOfIterations += 1;
				label[9].setText("Iterations: " + numberOfIterations);
			}
			// No cells to search so stop
			else {
				stopwatch.stop();
				simTimer.stop();
				label[8].setText("Path Length: No Path Found");
			}
			
		}));
		simTimer.setCycleCount(-1);
		simTimer.play();
	}
	
	private void gridButtonFunctions() {	
		// Start 
		button[0].setOnAction(event -> {
			running = true;
			currentCell = startCell;
			clearVisitedCells();
			stopwatch.start();
			label[6].setText("Cells Visited: " + numberOfVisitedCells + " / " + numberOfCells);
			label[8].setText("Path Length: " + pathLength);
			label[9].setText("Iterations: " + numberOfIterations);
			// Run the selected algorithm
			if(selectedAlgorithm.equals("Random Search")) {
				searchRandom();
			}
			else if(selectedAlgorithm.equals("Depth First Search")) {
				searchDepthFirst();
			}
			else if(selectedAlgorithm.equals("Breadth First Search")) {
				searchBreadthFirst();
			}
			else if(selectedAlgorithm.equals("Dijkstra")) {
				searchDijkstra();
			}
			else if(selectedAlgorithm.equals("A* Search")) {
				searchAStar();
			}
		
		});
		// Stop
		button[1].setDisable(true);
		button[1].setOnAction(event -> {
			stopwatch.stop();
			simTimer.stop();
		});
		// Sim Speed
		button[2].setOnAction(event -> {
			if(simSpeed == 25) {
				button[2].setText("Speed: 0.25");
				simSpeed = 400;
			}
			else if(simSpeed == 400) {
				button[2].setText("Speed: 0.5");
				simSpeed = 200;
			}
			else if(simSpeed == 200) {
				button[2].setText("Speed: 1");
				simSpeed = 100;
			}
			else if(simSpeed == 100) {
				button[2].setText("Speed: 2");
				simSpeed = 50;
			}
			else if(simSpeed == 50) {
				button[2].setText("Speed: 4");
				simSpeed = 25;
			}
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
		// Reset grid
		button[5].setOnAction(event -> {		
			generateGrid();
		});
	}
	
	private void algorithmButtonFunctions() {
		// Depth First Search
		button[6].setOnAction(event -> {
			selectedAlgorithm = "Random Search";
			label[3].setText("Algorithm: " + selectedAlgorithm);
			label[4].setText("The Random Search algorithm involves moving to a neighbouring cell at random. It doesn't guarantee the shortest path and is not efficient due to its randomness which often results in moving away from the end cell or revisiting cells needlessly.");
		});
		// Depth First Search
		button[7].setOnAction(event -> {
			selectedAlgorithm = "Depth First Search";
			label[3].setText("Algorithm: " + selectedAlgorithm);
			label[4].setText("The Depth First Search algorithm uses a backtracking technique by following an unexplored path until it reaches a dead end - a common technique used by humans to solve a maze. A stack is used to track the current path but doesn't guarantee the shortest path and backtracking hinders efficiency.");
		});
		// Breadth First Search
		button[8].setOnAction(event -> {
			selectedAlgorithm = "Breadth First Search";
			label[3].setText("Algorithm: " + selectedAlgorithm);
			label[4].setText("The Breadth First Search algorithm explores the grid equally in all directions from the starting cell. It will visit each cell only once by using a queue to keep track of encountered yet unvisited cells. It guarantees the shortest path but the efficiency is hindered by exploring in every direction equally.");
		});
		// Dijkstra
		button[9].setOnAction(event -> {
			selectedAlgorithm = "Dijkstra";
			label[3].setText("Algorithm: " + selectedAlgorithm);
			label[4].setText("Dijkstra's algorithm is similar to Breadth First Search in the way it explores in all directions but considers weight (distance) by utilizing a priority queue. This means it will explore cells with lower weights first and return the shortest and lowest cost path.");
		});
		// A* Search
		button[10].setOnAction(event -> {
			selectedAlgorithm = "A* Search";
			label[3].setText("Algorithm: " + selectedAlgorithm);
			label[4].setText("The A* Search algorithm is similar to Dijkstra's algorithm by also using a priority queue. However it is informed of the environment meaning it can calculate the distance not only from the start to current cell, but also between the current and end cell - letting it estimate the lowest cost path to search.");
		});
	}
	
	// User controls agent movement for testing
	private void userAgentControl(Scene scene) {
		
	scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {			
		if (running) {
			getNeighbours(currentCell, currentCell.getXPos(), currentCell.getYPos());
			
			if (key.getCode() == KeyCode.UP && currentCell.getYPos()>0 && !upNeighbour.getIsWall()) {
				searchCell(upNeighbour);
			}
			if (key.getCode() == KeyCode.DOWN && currentCell.getYPos()<gridRowsColumns-1 && !downNeighbour.getIsWall()) {
				searchCell(downNeighbour);
			}
			if (key.getCode() == KeyCode.LEFT && currentCell.getXPos()>0 && !leftNeighbour.getIsWall()) {
				searchCell(leftNeighbour);
			}
			if (key.getCode() == KeyCode.RIGHT && currentCell.getXPos()<gridRowsColumns-1 && !rightNeighbour.getIsWall()) {
				searchCell(rightNeighbour);
			}
		}
	});
	}
	
	private void setStartPosition(Cell cell) {
		// Remove previous start position
		startCell.setStartPos(false);
		startCell.setVisited(false);
		startCell.setCostG(1);
		startCell.setColor(Color.LIGHTGREY);
		
		// Set new start position
		cell.setStartPos(true);
		cell.setVisited(true);
		cell.setWeight(false);
		cell.setCostG(0);
		cell.setColor(Color.RED);
		startCell = cell;
		setStartPosition = false;
	}
	
	private void setEndPosition(Cell cell) {
		// Remove previous end position
		endCell.setEndPos(false); 
		endCell.setColor(Color.LIGHTGREY);
		
		// Set new end position
		cell.setEndPos(true);
		cell.setColor(Color.GREEN);
		endCell = cell;
		setEndPosition = false;
	}
	
	// Handles user input when drawing to the grid
	private void gridDrawer(Cell cell) {		
		
		grid.setOnMouseEntered(event -> {
			grid.setCursor(Cursor.HAND);
		});
		grid.setOnMouseExited(event -> {
			grid.setCursor(Cursor.DEFAULT);
		});
		
		// Set weight value with mouse wheel
		cell.setOnScroll(event -> {
			// Clear last run visited cells
			if(!(numberOfVisitedCells == 0)) {
				clearVisitedCells();
			}
						
			if(!cell.getIsStartPos() && !cell.getIsEndPos()  && !cell.getIsWall()) {
				// Scroll up or down changes weight value
				if(event.getDeltaY() >= 1 && cell.getCostG() < 100) {
					cell.setCostG(cell.getCostG()+1);
					cell.setWeight(true);
					cell.setColor(Color.web("#FFBE72"));
				}
				if(event.getDeltaY() <= -1 && cell.getCostG() > 1) {
					cell.setCostG(cell.getCostG()-1);
				}
				if(cell.getCostG()==1) {
					cell.setWeight(false);
					cell.setColor(Color.LIGHTGREY);
				}
				cell.setWeightValue(cell.getCostG());
				weightValue = cell.getCostG();
			}
		});
		
		cell.setOnMousePressed(event -> {
			// Clear last run visited cells
			if(!(numberOfVisitedCells == 0)) {
				clearVisitedCells();
			}
			
			// Left mouse click draws to the grid
			if(event.isPrimaryButtonDown() && !cell.getIsStartPos() && !cell.getIsEndPos()  && !cell.getIsWall()) {
				// Place start position
				if(setStartPosition) {
					setStartPosition(cell);
					for(int i = 0; i < cells.size(); i++) {
						calculateCostH(cells.get(i));
					}
				}
				// Place end position
				else if(setEndPosition) {
					setEndPosition(cell);
					for(int i = 0; i < cells.size(); i++) {
						calculateCostH(cells.get(i));
					}
				}
				// Place wall
				else {
					cell.setColor(Color.GREY);
					cell.setWall(true);
					cell.setWeight(false);
					cell.setCostG(1);
					numberOfWalls += 1;
					label[7].setText("Walls: " + numberOfWalls);
				}
			};
			// Right mouse click removes walls and resets weight
			if(event.isSecondaryButtonDown() && !cell.getIsStartPos() && !cell.getIsEndPos()) {
				cell.setColor(Color.LIGHTGREY);
				cell.setCostG(1);
				cell.setWeight(false);
				if(cell.getIsWall()) {
					numberOfWalls -= 1;
					label[7].setText("Walls: " + numberOfWalls);
					cell.setWall(false);
				}
			};
			// Add weight
			if(event.isMiddleButtonDown() && !cell.getIsStartPos() && !cell.getIsEndPos()) {
				if(weightValue == 1) {
					cell.setCostG(2);
					weightValue = cell.getCostG();
				}
				else {
					cell.setCostG(weightValue);
				}
				cell.setWeight(true);
				cell.setWeightValue(cell.getCostG());
				cell.setColor(Color.web("#FFBE72"));
			}
		});
		
		// Allow user to drag the mouse to draw on the grid
		cell.setOnDragDetected(mouseEvent -> cell.startFullDrag());
		cell.setOnMouseDragEntered(event -> {
			// Add wall
			if(event.isPrimaryButtonDown() && !cell.getIsStartPos() && !cell.getIsEndPos()  && !cell.getIsWall()) {
				cell.setColor(Color.GREY);
				cell.setWall(true);
				cell.setCostG(1);
				cell.setWeight(false);
				numberOfWalls += 1;
				label[7].setText("Walls: " + numberOfWalls);
			};
			// Remove wall/reset weight
			if(event.isSecondaryButtonDown() && !cell.getIsStartPos() && !cell.getIsEndPos()) {
				cell.setColor(Color.LIGHTGREY);
				cell.setCostG(1);
				cell.setWeight(false);
				if(cell.getIsWall()) {
					numberOfWalls -= 1;
					label[7].setText("Walls: " + numberOfWalls);
					cell.setWall(false);
				}
			};
			// Add weight
			if(event.isMiddleButtonDown() && !cell.getIsStartPos() && !cell.getIsEndPos()) {
				if(weightValue == 1) {
					cell.setCostG(2);
					weightValue = cell.getCostG();
				}
				else {
					cell.setCostG(weightValue);
				}
				cell.setWeight(true);
				cell.setWeightValue(cell.getCostG());
				cell.setColor(Color.web("#FFBE72"));
			}
		});
		
	}
	
	// When end point is reached, we add the result to the results table
	private void updateResultsTable() {
		resultsTable.getChildren().clear();
		// New set of results
		resultsTableLabel.add(new GUI_Label(selectedAlgorithm, 0, 0, 168, 0, 14, Color.BLACK, Color.LIGHTGREY, Pos.CENTER, true));

		resultsTableLabel.add(new GUI_Label(pathLength + "", 0, 0, 100, 0, 14, Color.BLACK, Color.LIGHTGREY, Pos.CENTER, true));
		resultsTableLabel.add(new GUI_Label(totalTime + "", 0, 0, 100, 0, 14, Color.BLACK, Color.LIGHTGREY, Pos.CENTER, true));
		resultsTableLabel.add(new GUI_Label(numberOfIterations + "", 0, 0, 100, 0, 14, Color.BLACK, Color.LIGHTGREY, Pos.CENTER, true));
		resultsTableLabel.add(new GUI_Label(numberOfVisitedCells + " / " + numberOfCells, 0, 0, 100, 0, 14, Color.BLACK, Color.LIGHTGREY, Pos.CENTER, true));
		resultsTableLabel.add(new GUI_Label(numberOfWalls + "", 0, 0, 100, 0, 14, Color.BLACK, Color.LIGHTGREY, Pos.CENTER, true));
		resultsTable.getChildren().addAll(resultsTableLabel);
	}
	
	// Format and style the results table
	private void generateResultsTable() {
		// Headers
		resultsTableLabel.add(new GUI_Label("Algorithm", 0, 0, 168, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true));
		resultsTableLabel.add(new GUI_Label("Path Length", 0, 0, 100, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true));
		resultsTableLabel.add(new GUI_Label("Time", 0, 0, 100, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true));
		resultsTableLabel.add(new GUI_Label("Iterations", 0, 0, 100, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true));
		resultsTableLabel.add(new GUI_Label("Cells Visited", 0, 0, 100, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true));
		resultsTableLabel.add(new GUI_Label("Walls", 0, 0, 100, 0, 15, Color.BLACK, Color.DARKGREY, Pos.CENTER, true));
		
		resultsTable.setPrefSize(668, 380);
		resultsTable.setVgap(3);
		resultsTable.setFocusTraversable(false); // Disable selecting buttons using keys
		resultsTable.getChildren().addAll(resultsTableLabel);
		
		resultsTableScroll.setLayoutX(881);
		resultsTableScroll.setLayoutY(374);
		resultsTableScroll.setPrefSize(668, 380);
		resultsTableScroll.setFocusTraversable(false); // Disable selecting buttons using keys
		resultsTableScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;-fx-padding: 0;-fx-background-insets: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;-fx-border-color:darkgrey");
		resultsTableScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		resultsTableScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		resultsTableScroll.setContent(resultsTable);
		
		// Remove last result button
		button[11].setOnAction(event -> {
			if(resultsTableLabel.size() > 6) {
				for(int i = 0; i < 6; i++) {
					resultsTableLabel.remove(resultsTableLabel.size()-1);
					resultsTable.getChildren().clear();
					resultsTable.getChildren().addAll(resultsTableLabel);
				}
			}
		});
	}
	
	// Format and style the slider to change the grid size
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
        
		// Make the slider function and update the grid size
		gridSizeSlider.setOnMouseDragged(event -> {
			gridRowsColumns = (int) gridSizeSlider.getValue();
			cellSize = gridSize/gridRowsColumns;
			numberOfCells = gridRowsColumns * gridRowsColumns;
        	label[1].setText("Size: " + gridRowsColumns + "x" + gridRowsColumns);
        	label[2].setText("Cells: " + numberOfCells);
        	//label[6].setText("Cells Visited: " + numberOfVisitedCells + " / " + numberOfCells);
        	generateGrid();
		});
		
		gridSizeSlider.setOnMouseReleased(event -> {
			gridRowsColumns = (int) gridSizeSlider.getValue();
			gridSizeSlider.setValue(gridRowsColumns);
			cellSize = gridSize/gridRowsColumns;
			numberOfCells = gridRowsColumns * gridRowsColumns;
        	label[1].setText("Size: " + gridRowsColumns + "x" + gridRowsColumns);
        	label[2].setText("Cells: " + numberOfCells);
        	//label[6].setText("Cells Visited: " + numberOfVisitedCells + " / " + numberOfCells);
        	generateGrid();
		});
	}
	
	// Position check boxes and give functions to show/hide cell cost labels
	private void generateCheckBoxes() {
		
		for(int i = 0; i < costCheckBoxes.length; i++) {
			costCheckBoxes[i].setPrefWidth(334);
			costCheckBoxes[i].setFont(Font.font("Arial", FontWeight.NORMAL, 15));
			costCheckBoxes[i].setTextFill(Color.BLACK);
			costCheckBoxes[i].setAlignment(Pos.TOP_RIGHT);
			costCheckBoxes[i].setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT); // Check box on right
			costCheckBoxes[i].setFocusTraversable(false); // Disable selecting buttons using keys
			costCheckBoxes[i].setSelected(true);
			costCheckBoxes[i].setOnMouseClicked(event -> {
				showCellCostLabels();
			});
		}
		costCheckBoxes[0].setLayoutX(1216);
		costCheckBoxes[0].setLayoutY(250);			
		costCheckBoxes[1].setLayoutX(1216);
		costCheckBoxes[1].setLayoutY(270);			
		costCheckBoxes[2].setLayoutX(1216);
		costCheckBoxes[2].setLayoutY(290);			
	}
	
	// Show/hide cell cost labels
	private void showCellCostLabels() {
		for(int i = 0; i < cells.size(); i++) {
			cells.get(i).showCostF(costCheckBoxes[0].isSelected());
			cells.get(i).showCostG(costCheckBoxes[1].isSelected());
			cells.get(i).showCostH(costCheckBoxes[2].isSelected());
		}
	}
}
