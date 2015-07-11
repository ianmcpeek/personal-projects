package view;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import model.Compass;

public class Python {
	
	public static void drawHead(GraphicsContext gc, Compass dir, Point2D pos) {
		switch(dir) {
			case NORTH:
				 gc.fillPolygon(new double[]{pos.getX(), pos.getX()+(Board.PIXEL_SIZE/2), pos.getX()+(Board.PIXEL_SIZE)}, 
					 		new double[]{pos.getY()+Board.PIXEL_SIZE, pos.getY(), pos.getY()+Board.PIXEL_SIZE}, 3);
				break;
			case SOUTH:
				gc.fillPolygon(new double[]{pos.getX(), pos.getX()+(Board.PIXEL_SIZE/2), pos.getX()+(Board.PIXEL_SIZE)}, 
				 		new double[]{pos.getY(), pos.getY()+Board.PIXEL_SIZE, pos.getY()}, 3);
				break;
			case WEST:
				gc.fillPolygon(new double[]{pos.getX()+Board.PIXEL_SIZE, pos.getX(), pos.getX()+Board.PIXEL_SIZE}, 
				 		new double[]{pos.getY(), pos.getY()+(Board.PIXEL_SIZE/2), pos.getY()+(Board.PIXEL_SIZE)}, 3);
				break;
			case EAST:
				gc.fillPolygon(new double[]{pos.getX(), pos.getX()+Board.PIXEL_SIZE, pos.getX()}, 
				 		new double[]{pos.getY(), pos.getY()+(Board.PIXEL_SIZE/2), pos.getY()+(Board.PIXEL_SIZE)}, 3);
				break;
		}
	}
	
	public static void drawBody(GraphicsContext gc, Point2D pos) {
		 gc.fillOval(pos.getX(), pos.getY(), 
				 Board.PIXEL_SIZE, Board.PIXEL_SIZE);
	}

	public static void drawTail(GraphicsContext gc, Compass dir, Point2D pos) {
		//switch(dir) {}
		gc.fillArc(pos.getX(), pos.getY(), Board.PIXEL_SIZE, Board.PIXEL_SIZE, 315, 270, ArcType.ROUND);
	}
	
	public static void drawFruit(GraphicsContext gc, Point2D pos) {
		gc.setStroke(javafx.scene.paint.Paint.valueOf("AQUA"));
		gc.fillOval(pos.getX(), pos.getY(), Board.PIXEL_SIZE, Board.PIXEL_SIZE);
	}

}
