package pstl.state;

import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface StateInboundCI extends OfferedCI {
	public int getPollution();
	public void changeTemperature(double temp);

}
