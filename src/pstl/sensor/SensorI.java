package pstl.sensor;

import pstl.util.Coord;

public interface SensorI {
	public double sense(Coord c) throws Exception;
}
