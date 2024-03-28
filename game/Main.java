

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;


public class Main extends Application {
	
	private GridPane pane = new GridPane();		// Main pane for game
	private Pane pane2 = new Pane();			// Other panes
	private Pane pane3 = new Pane();
	private Pane pane4 = new Pane();
	
	private Circle ball = new Circle();			// Circle for ball
	private Rectangle rect = new Rectangle();	// Rectangle for an image
	
	// Buttons
	private Button nextLevelButton = new Button("Next Level");
	private Button playAgainButton = new Button("Play Again");
	private Button restartButton = new Button("Restart Level");
	private Button playButton = new Button("Play");
	
	// Path for ball's movement
	private Path path = new Path();
	
	// Texts
	private Text numberOfMoveText;
	private Text numberOfLevelText;
	private Text recordNumberText;
	private Text recordText; 
	
	// Some arrays
	private Boolean[] isEmpty = new Boolean[17];	
	private ImageView[] blocksImageViews = new ImageView[17];		
	private int[] records = new int[51];
	
	// Some variables
	public int numberOfMoves = 0;
	private int numberOfLevel = 1;
	private boolean finished = false;
	private String levelPath = "CSE1242_spring2022_project_level1.txt";
	 
	
	// All images
	private Image empty = new Image("Empty.png");	
	private Image noPipe = new Image("No pipe.png");
	private Image pipe = new Image("Pipe.png");
	private Image staticPipe = new Image("Static pipe.png");
	private Image start = new Image("Start.png");
	private Image end = new Image("End.png");
	private Image curvedPipe = new Image("Curved pipe.png");
	private Image staticCurvedPipe = new Image("Static curved pipe.png");
	private Image ballImage = new Image("Ball.png");
	private Image levelCompletedBackgroundImage = new Image("Level Completed background.png");
	private Image rightSideBackground = new Image("Right Side Background.png");
	private Image levelCompletedImage = new Image("Level Completed.png");
	private Image menuImage = new Image("Menu.png");
	
	// Some imageviews
	private ImageView levelCompletedBackgroundImgView = new ImageView(levelCompletedBackgroundImage);
	private ImageView levelCompletedImgView = new ImageView(levelCompletedImage);
	private ImageView rightSideBackgroundImgView = new ImageView(rightSideBackground);
	private ImageView menuImgView = new ImageView(menuImage);
	
	// The file that store the record datas for record system
	File recordFile = new File("Records.txt");
	Scanner scanner;
	PrintWriter recordWriter;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {	
		
		
		for (int i = 0 ; i<17 ; i++) {	// Set all isEmpty values true because there is no block
			isEmpty[i] = true;		
		}
		
		for (int i = 0 ; i<4 ; i++) {			// Fill game area with empty blocks
			for (int j = 0 ; j<4 ; j++) {
				
				addBlock(empty, i, j, 0);		
			}
		}
		
	
		scanner = new Scanner(recordFile);		// Scanner for read records
		
    	int k = 1;
		while (scanner.hasNext()) {
			
			records[k] = scanner.nextInt();		//	Take all records to records array
			k++;
		}
	
		
		// Some image edits
		rightSideBackgroundImgView.setFitHeight(600);		
		rightSideBackgroundImgView.setFitWidth(200);
		rightSideBackgroundImgView.setX(0);
		rightSideBackgroundImgView.setY(0);
		GridPane.setConstraints(pane3, 4 , 0);
		pane3.getChildren().add(rightSideBackgroundImgView);
		pane.getChildren().add(pane3);
		levelCompletedBackgroundImgView.toBack();
		
		
		menuImgView.setFitHeight(600);
		menuImgView.setFitWidth(600);
		menuImgView.setTranslateY(-150);
		menuImgView.setTranslateX(-600);
		
		GridPane.setConstraints(pane4, 4 ,1);
		pane4.getChildren().add(menuImgView);
		pane.getChildren().addAll(pane4 , playButton);
		pane4.toFront();
		
		
		
		// Texts
		Text movesText = new Text (1000, 2000, "Moves:");
		movesText.setId("fancytext");
		movesText.setTranslateY(225);
		movesText.setTranslateX(660);
		movesText.setScaleX(2.5);
		movesText.setScaleY(2.5);
		movesText.setStyle("-fx-font-weight: bold");
		
		numberOfMoveText = new Text ();
		numberOfMoveText.setTranslateX(750);
		numberOfMoveText.setTranslateY(225);
		numberOfMoveText.setScaleX(2.5);
		numberOfMoveText.setScaleY(2.5);
		numberOfMoveText.setStyle("-fx-font-weight: bold");
	
		
		
		Text levelText = new Text (1000, 2000, "Level");
		levelText.setTranslateX(660);
		levelText.setTranslateY(95);
		levelText.setScaleX(3);
		levelText.setScaleY(3);
		levelText.setStyle("-fx-font-weight: bold");
		
		
		numberOfLevelText = new Text (1000, 2000, numberOfLevel+"");
		numberOfLevelText.setTranslateX(733);
		numberOfLevelText.setTranslateY(95);
		numberOfLevelText.setScaleX(3);
		numberOfLevelText.setScaleY(3);
		numberOfLevelText.setStyle("-fx-font-weight: bold");
		
		
		recordText = new Text (1000, 2000, "Record:");
		recordText.setTranslateX(662);
		recordText.setTranslateY(280);
		recordText.setScaleX(2.5);
		recordText.setScaleY(2.5);
		recordText.setStyle("-fx-font-weight: bold");
		
		recordNumberText = new Text (1000, 2000, records[1]+"");
		recordNumberText.setTranslateX(750);
		recordNumberText.setTranslateY(280);
		recordNumberText.setScaleX(2.5);
		recordNumberText.setScaleY(2.5);
		recordNumberText.setStyle("-fx-font-weight: bold");
		
		
		Text gameFisishedText = new Text (1000, 2000, "GAME FINISHED");
		gameFisishedText.setTranslateX(250);
		gameFisishedText.setTranslateY(260);
		gameFisishedText.setScaleX(4);
		gameFisishedText.setScaleY(4);
		gameFisishedText.setStyle("-fx-font-weight: bold");
		
		
		
		
    	
		// Button for going  to next level
		nextLevelButton.setPrefSize(100, 100);
		nextLevelButton.setTranslateX(40);
		
		
		// Button for playing  again the same level
		playAgainButton.setPrefSize(100, 100);
		playAgainButton.setTranslateX(40);	
		
		
		
		nextLevelButton.setOnAction(e -> {	// Button's action
			if (finished) {
				
				
				// This part updates the records.txt if record beaten
				if (records[numberOfLevel]>numberOfMoves || records[numberOfLevel] == 0) {	
					try {
						recordWriter = new PrintWriter(recordFile);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} 
					recordFile.delete();
					for (int i = 1 ; i<numberOfLevel ; i++) {
					recordWriter.println(records[i]);
					}
					recordWriter.println(numberOfMoves);
					for (int i = numberOfLevel+1 ; i<=50 ; i++) {
						recordWriter.println(records[i]);
					}
					records[numberOfLevel] = numberOfMoves;
					recordWriter.close();
					
				}
				for (int i = 1 ; i<17 ; i++) {
					pane.getChildren().remove(blocksImageViews[i]);	// Empty array because level finished, these are useless now
				}
				
				for (int i = 0 ; i<17 ; i++) {	
					isEmpty[i] = true;			
				}
				
				for (int i = 0 ; i<4 ; i++) {			
					for (int j = 0 ; j<4 ; j++) {
						
						addBlock(empty, i, j, 0);		
					}
				}
				numberOfLevel++;	// Increase level number
				
				numberOfLevelText.setText(numberOfLevel+"");	// Update level number text
				
				levelPath = "CSE1242_spring2022_project_level" + numberOfLevel + ".txt";	// Increase the levelPath for next level
				
				
				if (records[numberOfLevel] == 0) {	// If new level has no record (0) set recordNumberText to "No Record"
					recordNumberText.setText("No Record");
					recordNumberText.setTranslateX(670);
					try {
						pane.getChildren().remove(recordText);
					} catch (Exception ex) {}
				}
				else {
				recordNumberText.setText(""+records[numberOfLevel]);
				recordNumberText.setTranslateX(750);
				try{
					pane.getChildren().add(recordText);
				}
				catch (Exception ex){}
				}
				
				
				
				// Remove the completed level's  elements
				pane2.getChildren().remove(rect);
				pane2.getChildren().remove(levelCompletedBackgroundImgView);
				pane2.getChildren().remove(levelCompletedImgView);
				pane.getChildren().remove(playAgainButton);
				pane.getChildren().remove(nextLevelButton);
				
				try {
					generateMapData(levelPath);	// Generate map
					ball.toFront();
					pane2.toBack();
					
				} catch (Exception e1) {	// If there is an error, this must be because of the fact that there is no levelPath for next level and the game is finished
					pane4.getChildren().remove(playButton);	// Remove game elements and add finish scene
					pane4.getChildren().add(gameFisishedText);
					pane4.toFront();
					menuImgView.toBack();
					gameFisishedText.toFront();
					pane.getChildren().remove(movesText);
					pane.getChildren().remove(numberOfMoveText);
					pane.getChildren().remove(levelText);
					pane.getChildren().remove(numberOfLevelText);
					pane.getChildren().remove(restartButton);
					try{
						pane.getChildren().remove(recordText);
					}catch (Exception ex) {}
					pane.getChildren().remove(recordNumberText);
					pane.getChildren().add(gameFisishedText);
					
				}
				
				
				finished = false;  // Set "finished" variable as false for new level
				
			}
			
			
		});
		
		// Some adjustments
		restartButton.setPrefSize(100, 40);
		restartButton.setTranslateX(50);
		GridPane.setConstraints(restartButton, 4, 3);
		
		
		
		levelCompletedBackgroundImgView.setFitHeight(500);		
		levelCompletedBackgroundImgView.setFitWidth(380);  
		
		levelCompletedBackgroundImgView.setX(125);
		levelCompletedBackgroundImgView.setY(25);
		
		
		levelCompletedImgView.setFitHeight(400);		
		levelCompletedImgView.setFitWidth(500);  
		
		levelCompletedImgView.setX(125);
		levelCompletedImgView.setY(25);
		
		
		
		
		rect.setX(125);
		rect.setY(25);
		rect.setWidth(380);
		rect.setHeight(500);
		rect.setArcWidth(20);
		rect.setArcHeight(20);
		
		
		
		
		
		playAgainButton.setOnAction(e -> {	// Same as nextLevelButton but this generates the same map again
			
			
			
			if (records[numberOfLevel]>numberOfMoves || records[numberOfLevel] == 0) {
					try {
						recordWriter = new PrintWriter(recordFile);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} 
					recordFile.delete();
					for (int i = 1 ; i<numberOfLevel ; i++) {
					recordWriter.println(records[i]);
					}
					recordWriter.println(numberOfMoves);
					for (int i = numberOfLevel+1 ; i<=50 ; i++) {
						recordWriter.println(records[i]);
					}
					records[numberOfLevel] = numberOfMoves;
					
					if (records[numberOfLevel] == 0) {
						recordNumberText.setText("No Record");
						recordNumberText.setTranslateX(670);
						try {
							pane.getChildren().remove(recordText);
						} catch (Exception ex) {}
					}
					else {
					recordNumberText.setText(""+records[numberOfLevel]);
					recordNumberText.setTranslateX(750);
					try{
						pane.getChildren().add(recordText);
					}
					catch (Exception ex){}
					}
					
					recordWriter.close();
				}
				
				
			for (int i = 0 ; i<17 ; i++) {	
				isEmpty[i] = true;			
			}
			
			for (int i = 0 ; i<4 ; i++) {			
				for (int j = 0 ; j<4 ; j++) {
					
					addBlock(empty, i, j, 0);		
				}
			}
			
		
			
			try {
				generateMapData(levelPath);
				ball.toFront();
				pane2.toBack();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
			
			
			finished = false;
			
			pane2.getChildren().remove(rect);
			pane2.getChildren().remove(levelCompletedBackgroundImgView);
			pane2.getChildren().remove(levelCompletedImgView);
			pane.getChildren().remove(playAgainButton);
			pane.getChildren().remove(nextLevelButton);
		});
		
		
		restartButton.setOnAction(e -> {	// Same as playAgainButton but this doesnt check if the record has been beaten or not, because that level is not finished
			
			
			
			if (finished) {
				
				if (records[numberOfLevel] == 0) {
					recordNumberText.setText("No Record");
					recordNumberText.setTranslateX(670);
					try {
						pane.getChildren().remove(recordText);
					}
				catch (Exception ex) {}}
				
				else {
				recordNumberText.setText(""+records[numberOfLevel]);
				recordNumberText.setTranslateX(750);
				pane.getChildren().add(recordText);
				}
				
				if (records[numberOfLevel]>numberOfMoves || records[numberOfLevel] == 0) {
					try {
						recordWriter = new PrintWriter(recordFile);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} 
					recordFile.delete();
					for (int i = 1 ; i<numberOfLevel ; i++) {
					recordWriter.println(records[i]);
					}
					recordWriter.println(numberOfMoves);
					for (int i = numberOfLevel+1 ; i<=50 ; i++) {
						recordWriter.println(records[i]);
					}
					records[numberOfLevel] = numberOfMoves;
					recordWriter.close();
				}
				}
			pane.getChildren().remove(restartButton);
			for (int i = 0 ; i<17 ; i++) {	
				isEmpty[i] = true;			
			}
			
			for (int i = 0 ; i<4 ; i++) {			
				for (int j = 0 ; j<4 ; j++) {
					
					addBlock(empty, i, j, 0);		
				}
			}
			
			
			
			try {
				generateMapData(levelPath);
				ball.toFront();
				pane2.toBack();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
			pane2.getChildren().remove(rect);
			pane2.getChildren().remove(levelCompletedBackgroundImgView);
			pane2.getChildren().remove(levelCompletedImgView);
			pane.getChildren().remove(playAgainButton);
			pane.getChildren().remove(nextLevelButton);
			
		});
		
		
		
		
		ball.setRadius(16);
		ball.setFill(new ImagePattern(ballImage));
		pane.getChildren().add(ball);
		
		
		
		pane.getChildren().add(pane2);
		pane2.toBack();
		
		pane4.toFront();
		playButton.toFront();
		playButton.setPrefSize(300, 100);
		playButton.setTranslateX(227);
		playButton.setTranslateY(315);
	
		playButton.setOnAction(e -> {	// Same as nextLevelButton: just creates the first level and throws you back to the starting part
			try {
				if (records[numberOfLevel] == 0) {
					recordNumberText.setText("No Record");
					recordNumberText.setTranslateX(670);
					pane.getChildren().remove(recordText);
				}
				else {
				recordNumberText.setText(""+records[numberOfLevel]);
				recordNumberText.setTranslateX(750);
				pane.getChildren().add(recordText);
				}
				pane4.toBack();
				playButton.toBack();
				pane.toFront();
				generateMapData(levelPath);
				pane.getChildren().add(movesText);
				pane.getChildren().add(numberOfMoveText);
				pane.getChildren().add(levelText);
				pane.getChildren().add(numberOfLevelText);
				
				pane.getChildren().add(recordNumberText);
				ball.toFront();
				pane2.toBack();
				pane3.toBack();
			} catch (Exception e1) {
				System.out.println("There is no next level.");
				e1.printStackTrace();
			}
		});
		
		
		pane2.setPrefSize(0,0);
		GridPane.setConstraints(pane2, 0, 0);
		Scene scene = new Scene(pane, 800 ,600);		
		primaryStage.setTitle("Project Game");			
		primaryStage.setScene(scene);					
		primaryStage.show();							
		
		
		
	}
	
	
	
	
	  
	public void generateMapData(String inputFilePath) throws FileNotFoundException {	// Generating map by reading the text files and adding blocks

		//To reset the number of moves as zero in the start of the level.
		numberOfMoves = 0;	
		numberOfMoveText.setText("0");
		pane.getChildren().add(restartButton);
		
		
		//Scanner object to read the text file in order to build the level.
		File levelFile = new File(inputFilePath);
    	Scanner scanner = new Scanner(levelFile);
	        
	        try {
	        	

				while ( (scanner.hasNext()) ) {
					try {
	                	
	                	//the addBlock() method requires 3 parameters
	                	//the two of the three are the rotational degree value and the name of the block which are defined below
	                	double resultTemporaryDegree = 0; //placeholder value;
	     	            Image resultTemporaryName = null; //placeholder value;
	     	          
	        			//To read each line as a single string and remove the commas
	                    String line = scanner.nextLine();
	        			line = line.replaceAll(",", " ");
	        			Scanner lineScanner = new Scanner(line);
						
						//the third parameter for the addBlock() method
	        			String temporaryTile = null; //placeholder value
	        			
	        			//to dismiss reading the empty lines
	        			if(!line.equals("")) { 
	        				temporaryTile = lineScanner.next();	
	        			
	        			//to set the x and y coordinates of the tiles from the String in the text input
	        			int y = ((Integer.parseInt(temporaryTile))-1)/4;
	        			int x = ((Integer.parseInt(temporaryTile))-1)%4;
	        			String temporaryName = lineScanner.next();
	        			String temporaryDegree = lineScanner.next();
	                    
	                	
	                	//setting the name of the block with a "switch" statement as there are multiple possibilities according to the degree values
	                    switch(temporaryName){	
	                       
	                    	case "Empty":
	                            if(temporaryDegree.equals("none")){
	                                 resultTemporaryName = noPipe;  
	                            } else {
	                                 resultTemporaryName = empty; 
	                            }
	                            break; 
	                            
	                        case "Pipe":
	                            if (temporaryDegree.equals("Vertical") || temporaryDegree.equals("Horizontal")){
	                                 resultTemporaryName = pipe;
	                            } else {
	                                 resultTemporaryName = curvedPipe;
	                            }
	                            break;
	                      
	                        case "PipeStatic":
	                        	 if (temporaryDegree.equals("Vertical") || temporaryDegree.equals("Horizontal")){
	                             resultTemporaryName = staticPipe;
	                        	 }else {
	                        		 resultTemporaryName = staticCurvedPipe;
	                        	 }
	                             break;
	                      
	                        case "Starter":
	                             resultTemporaryName = start;
	                             break;
	                      
	                        case "End":
	                             resultTemporaryName = end;
	                             break;
	                    }


						//setting the degree of the block with a "switch" statement as there are multiple possibilities according to the type of block
	                    switch(temporaryDegree){

	                        case "Vertical":
	                        	
	                        	if(resultTemporaryName.equals(start)) {
	                        		resultTemporaryDegree = 0;
	                        	} else if (resultTemporaryName.equals(end)) {
	                        		resultTemporaryDegree = 270;
	                        	} else if (resultTemporaryName.equals(staticPipe)) {
	                        		resultTemporaryDegree = 90;
	                        	} else if (resultTemporaryName.equals(pipe)) {
	                        		resultTemporaryDegree = 0;
	                        	} 
	                     
	                            break;

	                        case "Horizontal":
	                        	
	                        	if(resultTemporaryName.equals(start)) {
	                        		resultTemporaryDegree = 90;
	                        	} else if (resultTemporaryName.equals(end)) {
	                        		resultTemporaryDegree = 0;
	                        	} else if (resultTemporaryName.equals(staticPipe)) {
	                        		resultTemporaryDegree = 0;
	                        	} else if (resultTemporaryName.equals(pipe)) {
	                        		resultTemporaryDegree = 270;
	                        	} 
	                     
	                            break;
	                            
	                        case "01":
	                            resultTemporaryDegree = 0;
	                            break;
	                        case "00":
	                            resultTemporaryDegree = 270;
	                            break;
	                        case "10":
	                            resultTemporaryDegree = 180;
	                            break;
	                        case "11":
	                            resultTemporaryDegree = 90;
	                            break;
	                        case "none":
	                            resultTemporaryDegree = 0;
	                            break;
	                        case "empty":
	                            resultTemporaryDegree = 0;
	                            break;
	                    }
	                    if (!resultTemporaryName.equals(empty)) {
	                	addBlock(resultTemporaryName, x, y, resultTemporaryDegree);
	                    }
	                    
	                    lineScanner.close();
	                }
				}
	                catch (NumberFormatException e) {
	                    //If an error happens while converting id to integer, print error to debug.
	                    System.out.printf("Error at some line...");
	                    //Print error message to debug.
	                    System.out.println(e.getMessage());
	                }
				}
			}
	        
	        
	        finally {
	            if (scanner.hasNext() == false) {
	                //close BufferedReader whatever happens.
	            	scanner.close();	
	            }         
	        }
	    }
		
	
		
	public void addBlock (Image img, int x, int y, double degree) {		// Adding the blocks with the given values
																		
		
	
		
		if (img.equals(empty)) {	
			
			ImageView imgView = new ImageView(img);		
			
			imgView.setFitHeight(150);	// Set the image as (150,150) for game 
			imgView.setFitWidth(150);
			
			GridPane.setConstraints(imgView, x, y);	// Set the block with given coordinates on GridPane
			blocksImageViews[1+x+(4*y)] = imgView;	// Save the imageView just in case it's needed in level
			pane.getChildren().add(imgView);			
		}											
		
		if (img.equals(start)) {
			
			ImageView imgView = new ImageView(img); 	
			imgView.setFitHeight(150);
			imgView.setFitWidth(150);			
			imgView.setRotate(degree);			
			
			isEmpty[1+x+4*y] = false;	// This block is no longer empty, because of this the "isEmpty" variable of it is set as false
			GridPane.setConstraints(imgView, x, y); 
			blocksImageViews[1+x+(4*y)] = imgView;
			pane.getChildren().add(imgView);
			
			
			
			ball.setTranslateX(((150*x)+75-16));
			ball.setTranslateY((150*y));
		}
		
		
		// Non-draggable tiles
		if (img.equals(staticPipe) || img.equals(end) || img.equals(staticCurvedPipe)) {		
		
			ImageView imgView = new ImageView(img); 	
			imgView.setFitHeight(150);
			imgView.setFitWidth(150);			
			imgView.setRotate(degree);			
			
			isEmpty[1+x+4*y] = false;				
			GridPane.setConstraints(imgView, x, y); 
			blocksImageViews[1+x+(4*y)] = imgView;
			pane.getChildren().add(imgView);	
		}
		
		
		// Draggable tiles
		if (img.equals(curvedPipe) || img.equals(noPipe) || img.equals(pipe)) {		// These blocks can move
			
			ImageView imgView = new ImageView(img); 
			imgView.setFitHeight(150);
			imgView.setFitWidth(150);
			imgView.setRotate(degree);
			
			isEmpty[1+x+4*y] = false;
			
		
			imgView.setOnMouseDragged(e -> {int X = GridPane.getColumnIndex(imgView)*150+75; int Y = GridPane.getRowIndex(imgView)*150+75; 	// Middle positions of blocks
				
				if (e.getSceneX()>X+75) {	// If mouses coordinate is 75 more than the middle point of block, this means it goes to the place of the other block on the right and moves that block on the right to the left.
					
					if (GridPane.getColumnIndex(imgView)!=3) {
						if (isEmpty[1+(GridPane.getColumnIndex(imgView)+1)+4*GridPane.getRowIndex(imgView)] == true) { 	// Changing  the "isEmpty" values
							
							isEmpty[1+(GridPane.getColumnIndex(imgView)+1)+4*GridPane.getRowIndex(imgView)] = false; 	
							isEmpty[1+(GridPane.getColumnIndex(imgView))+4*GridPane.getRowIndex(imgView)] = true; 	
							
							blocksImageViews[1+GridPane.getColumnIndex(imgView)+(4*GridPane.getRowIndex(imgView))] = new ImageView(empty);
							
							GridPane.setConstraints(imgView, GridPane.getColumnIndex(imgView)+1, GridPane.getRowIndex(imgView)); 	
							blocksImageViews[1+GridPane.getColumnIndex(imgView)+(4*GridPane.getRowIndex(imgView))] = imgView;
							checkLevelCompleted();
							numberOfMoves++; 	// Increase the move number
							
							numberOfMoveText.setText(""+numberOfMoves);	// Update the "move number" text
							
							}}} 
				
				else if (e.getSceneX()<X-75) {	// Same code but for the other directions
					if (GridPane.getColumnIndex(imgView)!=0) {
						if (isEmpty[1+(GridPane.getColumnIndex(imgView)-1)+4*GridPane.getRowIndex(imgView)] == true) {

							isEmpty[1+(GridPane.getColumnIndex(imgView)-1)+4*GridPane.getRowIndex(imgView)]=false; 
							isEmpty[1+(GridPane.getColumnIndex(imgView))+4*GridPane.getRowIndex(imgView)] = true; 
							
							blocksImageViews[1+GridPane.getColumnIndex(imgView)+(4*GridPane.getRowIndex(imgView))] = new ImageView(empty);
							
							GridPane.setConstraints(imgView, GridPane.getColumnIndex(imgView)-1, GridPane.getRowIndex(imgView));
							blocksImageViews[1+GridPane.getColumnIndex(imgView)+(4*GridPane.getRowIndex(imgView))] = imgView;
							checkLevelCompleted();
							numberOfMoves++;
							
							numberOfMoveText.setText(""+numberOfMoves);
							
							}}}
				
				else if (e.getSceneY()>Y+75) {	// Same code but for the other directions
					if (GridPane.getRowIndex(imgView)!=3) {
						if (isEmpty[1+(GridPane.getColumnIndex(imgView))+4*(GridPane.getRowIndex(imgView)+1)]==true) {
						
							isEmpty[1+(GridPane.getColumnIndex(imgView))+4*(GridPane.getRowIndex(imgView)+1)]=false; 
							isEmpty[1+(GridPane.getColumnIndex(imgView))+4*GridPane.getRowIndex(imgView)] = true; 
							
							blocksImageViews[1+GridPane.getColumnIndex(imgView)+(4*GridPane.getRowIndex(imgView))] = new ImageView(empty);
							
							GridPane.setConstraints(imgView, GridPane.getColumnIndex(imgView), GridPane.getRowIndex(imgView)+1);
							
							blocksImageViews[1+GridPane.getColumnIndex(imgView)+(4*GridPane.getRowIndex(imgView))] = imgView;
							checkLevelCompleted();
							numberOfMoves++;
							
							numberOfMoveText.setText(""+numberOfMoves);
						
						}}} 
				
				else if (e.getSceneY()<Y-75) { 	// Same code but for the other directions
						if (GridPane.getRowIndex(imgView)!=0) {
							if (isEmpty[1+(GridPane.getColumnIndex(imgView))+4*(GridPane.getRowIndex(imgView)-1)]==true) {
							
							isEmpty[1+(GridPane.getColumnIndex(imgView))+4*(GridPane.getRowIndex(imgView)-1)] = false; 
							isEmpty[1+(GridPane.getColumnIndex(imgView))+4*GridPane.getRowIndex(imgView)] = true; 
							
							blocksImageViews[1+GridPane.getColumnIndex(imgView)+(4*GridPane.getRowIndex(imgView))] = new ImageView(empty);
							
							GridPane.setConstraints(imgView, GridPane.getColumnIndex(imgView), GridPane.getRowIndex(imgView)-1);
							
							blocksImageViews[1+GridPane.getColumnIndex(imgView)+(4*GridPane.getRowIndex(imgView))] = imgView;
							checkLevelCompleted();
							numberOfMoves++;
							
							numberOfMoveText.setText(""+numberOfMoves);
							
							}}}
				
				
			});
					
			GridPane.setConstraints(imgView, x, y);	
			blocksImageViews[1+x+(4*y)] = imgView;
			pane.getChildren().add(imgView);		
			
		}
		
	}


	
	public void checkLevelCompleted() {	// Checks if the level is finished
		
		
		int startPosition = 0;	// The start position variable
		finished = false;	
		
		
		for (int i = 1 ; i<17 ; i++) {	// Get the startPosition of the ball image, within the block number 1-16
			if (blocksImageViews[i].getImage().equals(start)) {
				startPosition = i; break;
			}
		}
		
		int a = 0; int b = 0; // Some needed integers
		String direction = "";	// String variable for direction
		
		
		try {
		switch ((int)(blocksImageViews[startPosition].getRotate())) {	// Determine the direction with starter's rotation value
		
		case 0:	direction = "down"; 
			break;
		
		case 90:  direction = "left"; 
			break;
		}
		
		boolean loopController = true;
				while (loopController) { 
					
					
					switch (direction) {	// According to the direction, it checks the next block; for example if the path is going down, then we have to add 4 to the startPosition value for checking the following block.
					
					case "down": b = 4; 
						
						break;
					
					case "up": b = -4; 
						
						break;
				
					case "left":  b = -1; 
						
						break;
				
					case "right":  b = 1; 
						
						break;
						
				}	
					if (startPosition+a+b<=16 && startPosition+a+b>0) {		
					// If "position" value goes out of bounds then there is no need for check
					
					if (blocksImageViews[startPosition+a+b].getImage().equals(empty)) {
					// If next block on the path is an "empty" block, then there is no need for check

						break;
					}
					else if (blocksImageViews[startPosition+a+b].getImage().equals(end)) {
						// If the next block is "end" block, then it means the level is completed
						
						if ((blocksImageViews[startPosition+a+b].getRotate() == 0 && direction == "right") ||  (blocksImageViews[startPosition+a+b].getRotate() == 270 && direction == "up")) {
							// Checks if the "end" block's direction and the path's direction is same

						loopController = false; // Break the loop for check
						finished = true;  // Set the "finished" value as true
						
						
						if (records[numberOfLevel]>numberOfMoves || records[numberOfLevel] == 0) {
							// When the level is finished, if the "number of moves" value is less than the record then update the record with the current "number of moves" value

							recordNumberText.setText(""+(numberOfMoves+1));
							recordNumberText.setTranslateX(750);
							try{
								pane.getChildren().add(recordText);
							}
							catch (Exception ex){}
							}
						
						
						pane.getChildren().remove(restartButton);
						// Remove the restart button when the level is finished. This button is for restarting the level while the level hasn't been finished yet.

						pane2.toFront();
							// Showing "pane2"; this is the pane that shows the  "Level completed!" screen.
						
						delay(3500, () ->pane2.getChildren().add(rect) );
						// Ball animation waits for 3.5 seconds and to show the level complete screen after the ball's animation finished.
						
						delay(3501, () ->pane.add(nextLevelButton, 2, 2) );
						delay(3502, () ->pane.add(playAgainButton, 1, 2) );
						delay(3503, () ->pane2.getChildren().add(levelCompletedBackgroundImgView) );
						delay(3504, () ->pane2.getChildren().add(levelCompletedImgView) );
						
						
						
					
						for (int i = 1; i<17 ; i++) { 
						//Set the blocks that can't move.
							
							blocksImageViews[i].setOnMouseDragged(null);
							
						}
						
						ballAnimation();
						//Starts the ball animation.
					}
						else {
							loopController = false;
						}
					}
					

					else if (((((direction == "down" || direction == "up") && (blocksImageViews[startPosition+a+b].getRotate() == 0)) || ((direction == "right" || direction == "left") && (blocksImageViews[startPosition+a+b].getRotate() == 270))) && (blocksImageViews[startPosition+a+b].getImage().equals(pipe))) || ((((direction == "down" || direction == "up") && ((blocksImageViews[startPosition+a+b].getRotate() == 270) || blocksImageViews[startPosition+a+b].getRotate() == 90)) || ((direction == "right" || direction == "left") && (blocksImageViews[startPosition+a+b].getRotate() == 0))) && (blocksImageViews[startPosition+a+b].getImage().equals(staticPipe)))) {
								a = a+b;
					// If next block is a straight pipe block ("Static Pipe" or "Pipe"), set a = a+b. 
					//"a" refers to the position where the control is, the added b changes a to go to the location of the next block
								
						}		
					
							
					else if (blocksImageViews[startPosition+a+b].getImage().equals(curvedPipe) || blocksImageViews[startPosition+a+b].getImage().equals(staticCurvedPipe)) { 
						switch ((int)(blocksImageViews[startPosition+a+b].getRotate())) {
						// "Curved pipe"s change their directions according to current direction
						
						case 0: if (direction == "down") {direction = "right";} else if (direction == "left") {direction = "up";} else {loopController = false;} break; 
						
						case 90: if (direction == "up") {direction = "right";} else if (direction == "left") {direction = "down";} else {loopController = false;} break;
						
						case 180: if (direction == "right") {direction = "down";} else if (direction == "up") {direction = "left";} else {loopController = false;} break;
						
						case 270: if (direction == "right") {direction = "up";} else if (direction == "down") {direction = "left";} else {loopController = false;} break;
						 
						} a = a+b;
					}
					else break;
				}
					else break;
				}
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void ballAnimation() {	// Animation for ball, same system with checkLevelCompleted part. Looks at the next blocks, forms a path for the ball according to the tile (that's in the way)'s type and direction
		
		int startPosition = 0;
		String direction = "";
		
		boolean finished = false;
		
		
		for (int i = 1 ; i<17 ; i++) {
			if (blocksImageViews[i].getImage().equals(start)) {
				startPosition = i; break;
			}
		}
		
		int a = 0; int b = 0;
		double x = ball.getTranslateX(); //Ball's x coordinate
		double y = ball.getTranslateY(); //Ball's y coordinate
		
		 path.getElements().add (new MoveTo(x, y));
		
		switch ((int)(blocksImageViews[startPosition].getRotate())) {
		
		case 0:	direction = "down"; path.getElements().add (new CubicCurveTo(x, y, x, y+75, x, y+75)); y = y+75;
		// Balls default position is the "start" block's middle part. Because of this, the ball should first move to the out of the start block.
		// And to do that, it adds 75 down or 75 left movement to the path while updating its y coordinate as y=y+75.
			break;
		
		case 90:  direction = "left"; path.getElements().add (new CubicCurveTo(x, y, x-75, y, x-75, y)); x = x-75;
			break;
		}
		
		
		while (!finished) { 
		// Same system as the one in the "checkLevelCompleted" method
			
			
			switch (direction) {
			
			case "down": b = 4; 
				
				break;
			
			case "up": b = -4; 
				
				break;
		
			case "left":  b = -1; 
				
				break;
		
			case "right":  b = 1; 
				
				break;
				
		}
			
			if (blocksImageViews[startPosition+a+b].getImage().equals(end)) {
			// When it comes to an end, it moves 75 pixels right or up to go to the middle of the "end" block
				
				switch (direction) {
						
				case "right": path.getElements().add (new CubicCurveTo(x, y, x+75, y, x+75, y));
				//Add 75 pixels on the x value to move to the right on path.

					break;
					
				case "up": path.getElements().add (new CubicCurveTo(x, y, x, y-75, x, y-75));
				//Add 75 pixels on the y value to move to the up on path.
				
					break;
				
				}
				
				finished = true; 
				
				
			}
			
			//Checks if the block has a straight going pipe on it.
			else if (((((direction == "down" || direction == "up") && (blocksImageViews[startPosition+a+b].getRotate() == 0)) || ((direction == "right" || direction == "left") && (blocksImageViews[startPosition+a+b].getRotate() == 270))) && (blocksImageViews[startPosition+a+b].getImage().equals(pipe))) || ((((direction == "down" || direction == "up") && ((blocksImageViews[startPosition+a+b].getRotate() == 270) || blocksImageViews[startPosition+a+b].getRotate() == 90)) || ((direction == "right" || direction == "left") && (blocksImageViews[startPosition+a+b].getRotate() == 0))) && (blocksImageViews[startPosition+a+b].getImage().equals(staticPipe)))) {
						a = a+b;
						switch (direction) {
						case "right": path.getElements().add (new CubicCurveTo(x, y, x+150, y, x+150, y)); x = x+150;
						// Add 150 pixels on x for moving right on path and update the x value to x+150
						break;
						
						case "up": path.getElements().add (new CubicCurveTo(x, y, x, y-150, x, y-150)); y = y-150;
						//Same principle for movements on different ways
						break;
						
						case "left": path.getElements().add (new CubicCurveTo(x, y, x-150, y, x-150, y)); x = x-150;
						//Same principle for movements on different ways
						break;
						
						case "down": path.getElements().add (new CubicCurveTo(x, y, x, y+150, x, y+150)); y = y+150;
						//Same principle for movements on different ways
						break;
						
						}
				}		
			
					//Checks if the block has a "Curved pipe" on it
			else if (blocksImageViews[startPosition+a+b].getImage().equals(curvedPipe) || blocksImageViews[startPosition+a+b].getImage().equals(staticCurvedPipe)) { 
				switch ((int)(blocksImageViews[startPosition+a+b].getRotate())) {
				
				case 0: if (direction == "down") {
						
						direction = "right"; 
						path.getElements().add (new CubicCurveTo(x, y ,x ,y+75 , x+75,y+75)); y = y+75;  x = x+75;
						// Uses the same principle as in the "block with straight pipes" if statement.
						// But, in this if statement, we do not add 150 pixels of movement in one direction, instead these add 75 pixels of movement on the path in the y-axis direction and  75 pixels in the x-axis direction, according to the direction value and curved pipes rotate value.
				}
						else { 
							
							direction = "up";
							path.getElements().add (new CubicCurveTo(x, y ,x-75 ,y , x-75,y-75)); y = y-75;  x = x-75;
						}
				break;
				
				//Same principle for the other directions.
				
				case 90: if (direction == "up") {
					direction = "right"; 
						path.getElements().add (new CubicCurveTo(x, y ,x ,y-75 , x+75,y-75)); y = y-75;  x = x+75;
				}
						else {
							direction = "down"; 
							path.getElements().add (new CubicCurveTo(x, y ,x-75 ,y , x-75,y+75)); y = y+75;  x = x-75;
							
						}
				break;
				
				case 180: if (direction == "right") {
						direction = "down";
						path.getElements().add (new CubicCurveTo(x, y ,x+75 ,y , x+75,y+75)); y = y+75;  x = x+75;
				} 
					else {
					
						direction = "left";
						path.getElements().add (new CubicCurveTo(x, y ,x ,y-75 , x-75,y-75)); y = y-75;  x = x-75;
					
					}
				break;
				
				case 270: if (direction == "right") {
						direction = "up";
						path.getElements().add (new CubicCurveTo(x, y ,x+75 ,y , x+75,y-75)); y = y-75;  x = x+75;
				} 
				else {
					direction = "left";
					path.getElements().add (new CubicCurveTo(x, y ,x ,y+75 , x-75,y+75)); y = y+75;  x = x-75;
				}
				break;
				
				} a = a+b;
			}
			else break;
		}
		
		// Finally create a path transition with that created path
	     PathTransition pathTransition = new PathTransition();
	     pathTransition.setDuration(Duration.seconds(3));
	     pathTransition.setNode(ball);
	     pathTransition.setPath(path);
	     pathTransition.setOrientation(OrientationType.NONE);
	     pathTransition.setAutoReverse(false);
	 
	     pathTransition.play();	// And play it
	     path.getElements().clear();
		
}
	
	
	public static void delay(long millis, Runnable continuation) {		// The method for delaying a command. 
	      Task<Void> sleeper = new Task<Void>() {
	          @Override
	          protected Void call() throws Exception {
	              try { Thread.sleep(millis); }
	              catch (InterruptedException e) { }
	              return null;
	          }
	      };
	      sleeper.setOnSucceeded(event -> continuation.run());
	      new Thread(sleeper).start();
	    }
	
	
	public static void main(String[] args) {	
		launch(args);

	}
	
	
	
}
