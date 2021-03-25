package pstl.sensor;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import pstl.util.Coord;

public interface SensorCI extends OfferedCI, RequiredCI {
	public double sense(Coord c) throws Exception;
}
