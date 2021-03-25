package pstl.actuator;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class ActuatorInboundPort extends AbstractInboundPort implements ActuatorCI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ActuatorInboundPort(ComponentI owner) throws Exception {
		super(ActuatorCI.class, owner);
	}
	public ActuatorInboundPort(String uri, ComponentI owner) throws Exception {
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


