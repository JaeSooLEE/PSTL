package pstl.actuator;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import pstl.util.Coord;

public class ActuatorOutboundPort extends AbstractOutboundPort implements ActuatorCI {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7131550215345026078L;
	public ActuatorOutboundPort(ComponentI owner) throws Exception {
		super(ActuatorCI.class, owner);
	}
	public ActuatorOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ActuatorCI.class, owner);
	}
	@Override
	public void act(Coord c, double var) throws Exception {
		((ActuatorCI)this.getConnector()).act(c, var);
		
	}
}
