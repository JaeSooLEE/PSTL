package pstl.sensor;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import pstl.actuator.ActuatorCI;
import pstl.util.Coord;

public class SensorOutboundPort extends AbstractOutboundPort implements SensorCI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1919049987755081820L;
	public SensorOutboundPort(ComponentI owner) throws Exception {
		super(ActuatorCI.class, owner);
	}
	public SensorOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ActuatorCI.class, owner);
	}
	@Override
	public double sense(Coord c) throws Exception {
		return ((SensorCI)this.getConnector()).sense(c);
		
	}

}
