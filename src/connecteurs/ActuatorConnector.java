package connecteurs;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import pstl.actuator.ActuatorCI;
import pstl.util.Coord;

public class ActuatorConnector extends AbstractConnector implements ActuatorCI {

	@Override
	public void act(Coord c, double var) throws Exception {
		((ActuatorCI)this.offering).act(c, var);
		
	}

}
