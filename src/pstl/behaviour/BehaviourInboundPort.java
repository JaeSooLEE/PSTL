package pstl.behaviour;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import pstl.util.Address;

public class BehaviourInboundPort extends AbstractInboundPort implements BehaviourCI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2370585714675706549L;
	public BehaviourInboundPort(ComponentI owner) throws Exception {
		super(BehaviourCI.class, owner);
	}
	public BehaviourInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BehaviourCI.class, owner);
	}
	
	
	
	@Override
	public int update(Address address, int state, double val) throws Exception {
		return this.getOwner().handleRequest(
				b -> ((BehaviourI) b).update(address,state, val));
	}

}
