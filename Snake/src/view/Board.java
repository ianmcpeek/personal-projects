package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Compass;
import model.SnakeData;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Board extends Application{
	
	public static final int PIXEL_SIZE = 15;
	public static final int BOARD_SIZE = 30;
	public static final int CANVAS_SIZE = PIXEL_SIZE * BOARD_SIZE;
	private static boolean inpDisabled;
	private SnakeData snake;
	private Compass usrDir = Compass.NORTH;
	
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
    	
    	ArrayList<String> colors = new ArrayList<String>();
    	Scanner scan = new Scanner(new File("colors.txt"));
    	while(scan.hasNext()) {
    		colors.add(scan.next());
    	}
    	
    	primaryStage.setTitle("Hello World!");
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
        Button btn = new Button();
        btn.setText("Draw like a PIIUMP!!");
        border.setBottom(btn);
        placeSnake(gcSnake, new Point2D((CANVAS_SIZE)/2, 
        		CANVAS_SIZE-(PIXEL_SIZE*4)));
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	int rx = (int)(Math.random()*CANVAS_SIZE);
            	int ry = (int)(Math.random()*CANVAS_SIZE);
            	paintSplatter(gcFruit, colors, new Point2D(rx, ry));
            }
        });
        
        //TIMER
        
        Timeline timeline = new Timeline(new KeyFrame(
    	        Duration.millis(100),
    	        ae -> advanceSnake(gcSnake, snake.getSnake().get(0))));//paintSplatter(gcFruit, colors, new Point2D(30, 30))));
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
		                	usrDir = Compass.NORTH;
		                	break;
		                case DOWN:
		                	usrDir = Compass.SOUTH;
		                	break;
		                case LEFT:
		                	usrDir = Compass.WEST;
		                	break;
		                case RIGHT:
		                	usrDir = Compass.EAST;
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
	 gc.setFill(javafx.scene.paint.Paint.valueOf("DARKGREEN"));
	 //Initialize snake
	 snake = new SnakeData(spwPos);
	 
	 //draw snake head, body, tail
	 Python.drawHead(gc, snake.getDirection(), snake.getSnake().get(0));
	 Python.drawBody(gc, snake.getSnake().get(1));
	 Python.drawTail(gc, snake.getDirection(), snake.getSnake().get(2));
 }
 
 public void advanceSnake(GraphicsContext gc, Point2D currPos) {
	 inpDisabled = true;
	 //check if user input is valid
	 if(usrDir != Compass.opposite(snake.getDirection())) {
		 snake.setDirection(usrDir);
	 }
	 
	 Point2D nextPos = SnakeData.nextPosition(snake.getDirection(), currPos);
	 //check for collision
	 	//if collision draw animation
	 //advance snake in current direction
	 	//decrement delay if > 0
	 
	 	//-draw new head
	 	//-draw body over old head
	 	//-erase old tail
	 	//-draw new tail over old body
	 	Python.drawHead(gc, snake.getDirection(), nextPos);
	 	gc.clearRect(snake.getSnake().get(0).getX(), snake.getSnake().get(0).getY(), 
	 			PIXEL_SIZE, PIXEL_SIZE);
	 	Python.drawBody(gc, snake.getSnake().get(0));
	 	snake.getSnake().add(0, nextPos);
	 
	 	gc.clearRect(snake.getSnake().get(snake.getSnake().size()-1).getX(), 
	 			snake.getSnake().get(snake.getSnake().size()-1).getY(), 
	 			PIXEL_SIZE, PIXEL_SIZE);
	 	snake.getSnake().remove(snake.getSnake().size()-1);
	 	gc.clearRect(snake.getSnake().get(snake.getSnake().size()-1).getX(), 
	 			snake.getSnake().get(snake.getSnake().size()-1).getY(), 
	 			PIXEL_SIZE, PIXEL_SIZE);
	 	Python.drawTail(gc, snake.getDirection(), snake.getSnake().get(snake.getSnake().size()-1));
	 	//increment snake bitboard
	 
	 inpDisabled = false;
 }
 
 public void placeFruit(Point2D spwPos) {
	 
 }
 
 public void eatFruit() {
	 
 }
 
 public void eatSelf() {
	 //Oroboros
 }
 
 public void paintSplatter(GraphicsContext gc, ArrayList<String> colors, Point2D origin) {
	 int rx, ry, rh, rw;
	 int ri = (int)(Math.random()*colors.size());
	 System.out.println("Color: " + colors.get(ri));
	 gc.setFill(javafx.scene.paint.Paint.valueOf(colors.get(ri)));
	 
     //Paint origin
     for(int i=0; i<10; i++) {
    	 rx = (int)((Math.random()*10) + origin.getX());
    	 ry = (int)((Math.random()*10) + origin.getY());
    	 rw = (int)(Math.random()*25)+5;
    	 rh = rw;
    	 gc.fillOval(rx, ry, rw, rh);
     }
     //Paint edges
     for(int i=0; i<30; i++) {
    	 rx = (int)((Math.random()*50) + origin.getX());
    	 ry = (int)((Math.random()*50) + origin.getY());
    	 rw = (int)(Math.random()*15)+2;
    	 rh = rw;
    	 gc.fillOval(rx, ry, rw, rh);
     }
 }

}
