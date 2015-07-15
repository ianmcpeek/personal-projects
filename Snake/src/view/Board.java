package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Compass;
import model.SnakeData;

public class Board extends Application{
	
	public static final int PIXEL_SIZE = 20;
	public static final int BOARD_SIZE = 30;
	public static final int CANVAS_SIZE = PIXEL_SIZE * BOARD_SIZE;
	private static final int SPEED_BASE = 100;//SPEED BOOST NOT WORKING
	private static final int SPEED_MULT = 20;
	private static boolean inpDisabled;
	private SnakeData snake;
	private Python python;
	private Compass usrDir = Compass.NORTH;
	private Timeline timeline;
	private int speedup;
	
	//Paint Assets
	ArrayList<String> colors;
	String fColor;
	
    @Override
    public void start(Stage primaryStage) {
    	try {
			colors = Python.generateColors();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	fColor = "AQUA";
    	speedup = 0;
    	python = new Python();
    	
    	primaryStage.setTitle("Snake Eat Snake");
    	Scene scene = new Scene(new VBox(), CANVAS_SIZE, CANVAS_SIZE+50);
    	
    	MenuBar menubar = new MenuBar();
    	Menu menu = new Menu("Menu");
    	menubar.getMenus().add(menu);
    	Menu graphics = new Menu("Graphics");
    	MenuItem max = new MenuItem("1920 x 1080");
    	graphics.getItems().add(max);
    	menubar.getMenus().add(graphics);
    	((VBox) scene.getRoot()).getChildren().addAll(menubar);
    	
    	BorderPane border = new BorderPane();
    	Canvas snakeLayer = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        GraphicsContext gcSnake = snakeLayer.getGraphicsContext2D();
        Canvas fruitLayer = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        GraphicsContext gcFruit = fruitLayer.getGraphicsContext2D();
        border.setCenter(new Pane(fruitLayer,snakeLayer));
        //Button btn = new Button();
        //btn.setText("Draw like a PIIUMP!!");
        //border.setBottom(btn);
        placeSnake(gcSnake, new Point2D((BOARD_SIZE/2)*PIXEL_SIZE, 
        		CANVAS_SIZE-(PIXEL_SIZE*4)));
        placeFruit(gcFruit);
//        btn.setOnAction(new EventHandler<ActionEvent>() {
// 
//            @Override
//            public void handle(ActionEvent event) {
//            	int rx = (int)(Math.random()*CANVAS_SIZE);
//            	int ry = (int)(Math.random()*CANVAS_SIZE);
//            	paintSplatter(gcFruit, "DARKBLUE", new Point2D(rx, ry));
//            }
//        });
        
        //TIMER
        
        timeline = new Timeline(new KeyFrame(
    	        Duration.millis(SPEED_BASE),
    	        ae -> advanceSnake(gcSnake, gcFruit, snake.getSnake().get(0))));//paintSplatter(gcFruit, colors, new Point2D(30, 30))));
    	timeline.setCycleCount(Animation.INDEFINITE);
    	timeline.play();
        
        ((VBox) scene.getRoot()).getChildren().add(border);
        //((VBox) scene.getRoot()).getChildren().add(btn);
        
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	if(!inpDisabled) {   	
	                switch (event.getCode()) {
		                case UP:
		                	if(Compass.NORTH!=Compass.opposite(snake.getDirection())) {
		                		usrDir = Compass.NORTH;
		                	}
		                	break;
		                case DOWN:
		                	if(Compass.SOUTH!=Compass.opposite(snake.getDirection())) {
		                		usrDir = Compass.SOUTH;
		                	}
		                	break;
		                case LEFT:
		                	if(Compass.WEST!=Compass.opposite(snake.getDirection())) {
		                		usrDir = Compass.WEST;
		                	}
		                	break;
		                case RIGHT:
		                	if(Compass.EAST!=Compass.opposite(snake.getDirection())) {
		                		usrDir = Compass.EAST;
		                	}
		                	break;
	                }
            	}
            }
        });
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
 public static void main(String[] args) {
        launch(args);
    }
 
 
 //snake animations
 public void placeSnake(GraphicsContext gc, Point2D spwPos) {
	 //gc.setFill(javafx.scene.paint.Paint.valueOf("DARKGREEN"));
	 //Initialize snake
	 snake = new SnakeData(spwPos);
	 
	 //draw snake head, body, tail
	 python.drawHead(gc, snake.getDirection(), snake.getSnake().get(0));
	 python.drawBody(gc, snake.getSnake().get(1));
	 python.drawTail(gc, snake.getDirection(), snake.getSnake().get(2));
 }
 
 public void advanceSnake(GraphicsContext gc, GraphicsContext gcFruit, Point2D currPos) {
	 inpDisabled = true;
	 snake.setDirection(usrDir);
	 
	 Point2D nextPos = SnakeData.nextPosition(snake.getDirection(), currPos);
	 //check for collision
	 	eatFruit(gcFruit, fColor, currPos);
	 	eatSelf(nextPos);
	 	//if collision draw animation
	 //advance snake in current direction
	 	//decrement delay if > 0
	 
	 	//-draw new head
	 	//-draw body over old head
	 	//-erase old tail
	 	//-draw new tail over old body
	 	python.drawHead(gc, snake.getDirection(), nextPos);
	 	gc.clearRect(snake.getSnake().get(0).getX(), snake.getSnake().get(0).getY(), 
	 			PIXEL_SIZE, PIXEL_SIZE);
	 	python.drawBody(gc, snake.getSnake().get(0));
	 	
	 	if(snake.getDelay()==0) {
	 		gc.clearRect(snake.getSnake().get(snake.getSnake().size()-1).getX(), 
		 			snake.getSnake().get(snake.getSnake().size()-1).getY(), 
		 			PIXEL_SIZE, PIXEL_SIZE);

		 	//move snake
		 	snake.moveSnake(nextPos);
		 	gc.clearRect(snake.getSnake().get(snake.getSnake().size()-1).getX(), 
		 			snake.getSnake().get(snake.getSnake().size()-1).getY(), 
		 			PIXEL_SIZE, PIXEL_SIZE);
		 	python.drawTail(gc, snake.getDirection(), snake.getSnake().get(snake.getSnake().size()-1));
		 	//increment snake bitboard
	 	} else {
	 		snake.moveSnake(nextPos);
	 	}
	 	
	 
	 inpDisabled = false;
 }
 
 public void placeFruit(GraphicsContext gc) {
	 Point2D fruitPos = snake.dropFruit();
	 Python.drawFruit(gc, new Point2D(fruitPos.getX(), fruitPos.getY()), fColor);
 }
 
 public void eatFruit(GraphicsContext gc, String color, Point2D pos) {
	 if(snake.detectSnakeEatFruit(pos)) {
		 //eat the fruit yo
		 snake.snakeEatFruit(pos);
		 gc.clearRect(pos.getX(), pos.getY(), PIXEL_SIZE, PIXEL_SIZE);
		 Python.paintSplatter(gc, color, pos);
		 fColor = Python.pickFruitColor(colors, fColor);
		 placeFruit(gc);
		 //speedBoost();
	 }
 }
 
 public void eatSelf(Point2D pos) {
	 //Oroboros
	 if(snake.detectSnakeEatSnake(pos)) {
		 timeline.stop();
		 Alert alert = new Alert(AlertType.INFORMATION);
		 alert.setTitle("Snake Eater");
		 alert.setHeaderText("Game Over!");
		 String s ="I HAVE HAD IT WITH THESE MOTHERFUCKING SNAKES ON THIS MOTHERFUCKING PLANE!";
		 alert.setContentText(s);
		 alert.show();
		 
		 String musicFile = "enough.wav";

		 Media sound = new Media(new File(musicFile).toURI().toString());
		 MediaPlayer mediaPlayer = new MediaPlayer(sound);
		 mediaPlayer.play();

	 }
 }
 
// public void speedBoost() {
//	 if((speedup+1)*SPEED_MULT<SPEED_BASE) {
//		 speedup++;
//	 }
//	 timeline.pause();
//	 timeline.setDelay(Duration.millis(SPEED_BASE-(SPEED_MULT*speedup)));
//	 timeline.play();
// }

}
