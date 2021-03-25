package pstl.actuator;

import fr.sorbonne_u.components.interfaces.OfferedCI;


public interface ActuatorCI extends OfferedCI {
	public void act(double var) throws Exception;
}
