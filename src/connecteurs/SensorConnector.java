package connecteurs;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import pstl.sensor.SensorCI;
import pstl.util.Coord;

public class SensorConnector extends AbstractConnector implements SensorCI  {

	@Override
	public double sense(Coord c) throws Exception {
		return ((SensorCI)this.offering).sense(c);
	}

}
