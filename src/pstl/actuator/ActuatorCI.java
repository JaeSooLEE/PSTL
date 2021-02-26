package pstl.actuator;

import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ActuatorCI extends RequiredCI {
	public void act(double var) throws Exception;
}
