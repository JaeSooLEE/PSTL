package pstl.behaviour;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

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
	public int update(int state) throws Exception {
		return this.getOwner().handleRequest(
				b -> ((BehaviourCI) b).update(state));
	}

}
