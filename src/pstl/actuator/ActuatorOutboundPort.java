package pstl.actuator;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class ActuatorOutboundPort extends AbstractOutboundPort implements ActuatorCI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ActuatorOutboundPort(ComponentI owner) throws Exception {
		super(ActuatorCI.class, owner);
	}
	public ActuatorOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ActuatorCI.class, owner);
	}
	
	@Override
	public void act(double var) throws Exception {
		try {
			this.getOwner().runTask(
					a-> ((Actuator) a).act(var));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
