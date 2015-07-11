package model;

public enum Compass {
	NORTH,
	SOUTH,
	EAST,
	WEST;
	
	public static Compass opposite(Compass dir) {
		Compass op = null;
		switch(dir) {
			case NORTH:
				op = SOUTH;
				break;
			case SOUTH:
				op = NORTH;
				break;
			case WEST:
				op = EAST;
				break;
			case EAST:
				op = WEST;
				break;
		}
		return op;
	}
}
