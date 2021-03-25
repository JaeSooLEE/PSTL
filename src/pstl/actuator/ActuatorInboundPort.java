package pstl.actuator;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import pstl.util.Coord;

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
	public void act(Coord c, double var) throws Exception {

		this.getOwner().runTask(
			a-> {
				try {
					((ActuatorCI) a).act(c,var);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
	
	}

}


