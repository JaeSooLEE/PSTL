package pstl.sensor;

import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface SensorCI extends OfferedCI {
	public SensorValueI sense() throws Exception;
}
