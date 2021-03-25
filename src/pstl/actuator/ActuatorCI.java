package pstl.actuator;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import pstl.util.Coord;


public interface ActuatorCI extends OfferedCI, RequiredCI {
	public void act(Coord c, double var) throws Exception;
}
