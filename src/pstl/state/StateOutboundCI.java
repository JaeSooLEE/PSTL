package pstl.state;

import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface StateOutboundCI extends RequiredCI {
	public int getSensorValue();
	public void setActuator(double temp);
	

}
