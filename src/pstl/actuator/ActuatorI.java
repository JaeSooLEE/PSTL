package pstl.actuator;

import pstl.util.Coord;

public interface ActuatorI {
	
	public void act(Coord c, double var) throws Exception;
}
