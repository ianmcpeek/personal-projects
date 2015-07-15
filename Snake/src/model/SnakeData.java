package model;

import java.util.ArrayList;

import view.Board;
import javafx.geometry.Point2D;

public class SnakeData {

	//snake head bitboard
	//snake body bitboard
	//fruit bitboard
	
	private static final int FOOD_NUTRITION = 10;
	
	private Compass dir;
	private ArrayList<Point2D> snake;
	private Point2D fruit;
	private int delay;
	
	private boolean[][] fBitBoard;
	private boolean[][] sBitBoard;
	
	
	public SnakeData(Point2D startPos) {
		dir = Compass.NORTH;
		snake = new ArrayList<Point2D>();
		delay = 0;
		
		fBitBoard = new boolean[Board.BOARD_SIZE][Board.BOARD_SIZE];
		sBitBoard = new boolean[Board.BOARD_SIZE][Board.BOARD_SIZE];
		
		//Initialize snake
		Point2D prev = previousPosition(dir, startPos);
		Point2D prevprev = previousPosition(dir, prev); 
		
		//head
		snake.add(startPos);
		sBitBoard[(int)startPos.getX()/Board.PIXEL_SIZE]
				[(int)startPos.getY()/Board.PIXEL_SIZE] = true;
		//body
		snake.add(prev);
		sBitBoard[(int)prev.getX()/Board.PIXEL_SIZE]
				[(int)prev.getY()/Board.PIXEL_SIZE] = true;
		//tail
		snake.add(prevprev);
		sBitBoard[(int)prevprev.getX()/Board.PIXEL_SIZE]
				[(int)prevprev.getY()/Board.PIXEL_SIZE] = true;
	}
	
	public void setDirection(Compass newDir) {
		dir = newDir;
	}
	
	public Compass getDirection() {
		return dir;
	}
	
	public void decrementDelay() {
		delay--;
		if(delay<0) {
			delay = 0;
		}
	}
	
	public int getDelay() {
		return delay;
	}
	
	public ArrayList<Point2D> getSnake() {
		return snake;
	}
	
	public void moveSnake(Point2D newPos) {
		Point2D tail = snake.get(snake.size()-1);
		
		sBitBoard[(int)(newPos.getX()/Board.PIXEL_SIZE)]
				 [(int)(newPos.getY()/Board.PIXEL_SIZE)] = true;
		snake.add(0, newPos);
		//System.out.println("snake: x " + (int)newPos.getX()/Board.PIXEL_SIZE + " y " + (int)newPos.getY()/Board.PIXEL_SIZE);
		if(delay==0) {
			sBitBoard[(int)(tail.getX()/Board.PIXEL_SIZE)]
					 [(int)(tail.getY()/Board.PIXEL_SIZE)] = false;
			snake.remove(snake.size()-1);
			//printSnakeBoard();
		} else {
			delay--;
		}
	}
	
	public Point2D dropFruit() {
		//TODO: make sure fruit can't spawn on snake
		int rx, ry;
		rx = (int)(Math.random()*Board.BOARD_SIZE);
		ry = (int)(Math.random()*Board.BOARD_SIZE);
		fBitBoard[rx][ry] = true;
		//System.out.println("fruit: x " + rx + " y " + ry);
		return new Point2D(rx*Board.PIXEL_SIZE, ry*Board.PIXEL_SIZE);
	}
	
	public void snakeEatFruit(Point2D pos) {
		fBitBoard[(int)(pos.getX()/Board.PIXEL_SIZE)]
				 [(int)(pos.getY()/Board.PIXEL_SIZE)] = false;
		delay = FOOD_NUTRITION;
	}
	
	
	public boolean detectSnakeEatFruit(Point2D pos) {
		return sBitBoard[(int)(pos.getX()/Board.PIXEL_SIZE)][(int)(pos.getY()/Board.PIXEL_SIZE)] 
			&& fBitBoard[(int)(pos.getX()/Board.PIXEL_SIZE)][(int)(pos.getY()/Board.PIXEL_SIZE)];
	}
	
	public boolean detectSnakeEatSnake(Point2D pos) {
		return sBitBoard[(int)(pos.getX()/Board.PIXEL_SIZE)]
						[(int)(pos.getY()/Board.PIXEL_SIZE)]; 
	}
	
	public void printSnakeBoard() {
		for(int i=0; i<Board.BOARD_SIZE; i++) {
			for(int j=0; j<Board.BOARD_SIZE; j++ ) {
				System.out.print((fBitBoard[i][j]?"O":(sBitBoard[i][j]?"X":"-")) + " ");
			}
			System.out.println();
		}
	}
	
	/******************
	 * STATIC METHODS *
	 ******************/
	
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
