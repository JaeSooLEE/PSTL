package pstl.sensor;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import pstl.actuator.ActuatorCI;
import pstl.util.Coord;

public class SensorInboundPort extends AbstractInboundPort implements SensorCI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 807517902564153286L;
	public SensorInboundPort(ComponentI owner) throws Exception {
		super(ActuatorCI.class, owner);
	}
	public SensorInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ActuatorCI.class, owner);
	}
	@Override
	public double sense(Coord c) throws Exception {
		return this.getOwner().handleRequest(
				s -> ((SensorCI) s).sense(c));
	}

}
