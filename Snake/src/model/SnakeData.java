package model;

import java.util.ArrayList;

import view.Board;
import javafx.geometry.Point2D;

public class SnakeData {

	//snake head bitboard
	//snake body bitboard
	//fruit bitboard
	
	private static final int FOOD_NUTRITION = 3;
	
	private Compass dir;
	private ArrayList<Point2D> snake;
	private int delay;
	
	public SnakeData(Point2D startPos) {
		dir = Compass.NORTH;
		snake = new ArrayList<Point2D>();
		delay = 0;
		
		//Initialize snake
		Point2D prev = previousPosition(dir, startPos);
		snake.add(startPos);
		snake.add(prev);
		snake.add(previousPosition(dir, prev));
	}
	
	public void setDirection(Compass newDir) {
		dir = newDir;
	}
	
	public Compass getDirection() {
		return dir;
	}
	
	public ArrayList<Point2D> getSnake() {
		return snake;
	}
	
	public static Point2D nextPosition(Compass dir, Point2D pos) {
		Point2D nextPos = null;
		switch(dir) {
			case NORTH:
				if(pos.getY()-Board.PIXEL_SIZE < 0) {
					nextPos = new Point2D(pos.getX(), (Board.CANVAS_SIZE + (pos.getY()-Board.PIXEL_SIZE)));
				} else {
					nextPos = new Point2D(pos.getX(), (pos.getY()-Board.PIXEL_SIZE));
				}
				break;
			case SOUTH:
				nextPos = new Point2D(pos.getX(), (pos.getY()+Board.PIXEL_SIZE)%Board.CANVAS_SIZE);
				break;
			case WEST:
				if(pos.getX()-Board.PIXEL_SIZE < 0) {
					nextPos = new Point2D(Board.CANVAS_SIZE + (pos.getX()-Board.PIXEL_SIZE), pos.getY());
				} else {
					nextPos = new Point2D((pos.getX()-Board.PIXEL_SIZE), pos.getY());
				}
				break;
			case EAST:
				nextPos = new Point2D((pos.getX()+Board.PIXEL_SIZE)%Board.CANVAS_SIZE, pos.getY());
				break;
		}
		return nextPos;
	}
	
	private static Point2D previousPosition(Compass dir, Point2D pos) {
		Point2D prevPos = null;
		switch(dir) {
			case NORTH:
				prevPos = new Point2D(pos.getX(), (pos.getY()+Board.PIXEL_SIZE)%Board.CANVAS_SIZE);
				break;
			case SOUTH:
				prevPos = new Point2D(pos.getX(), (pos.getY()-Board.PIXEL_SIZE)%Board.CANVAS_SIZE);
				break;
			case WEST:
				prevPos = new Point2D((pos.getX()+Board.PIXEL_SIZE)%Board.CANVAS_SIZE, pos.getY());
				break;
			case EAST:
				prevPos = new Point2D((pos.getX()-Board.PIXEL_SIZE)%Board.CANVAS_SIZE, pos.getY());
				break;
		}
		return prevPos;
	}
}
