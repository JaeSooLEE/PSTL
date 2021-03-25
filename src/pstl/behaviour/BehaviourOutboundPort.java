package pstl.behaviour;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class BehaviourOutboundPort extends AbstractOutboundPort implements BehaviourCI{
	
	private static final long serialVersionUID = 1L;
	public BehaviourOutboundPort(ComponentI owner) throws Exception {
		super(BehaviourCI.class, owner);
	}
	public BehaviourOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BehaviourCI.class, owner);
	}
	@Override
	public int update(int state) throws Exception {
		
		return ((BehaviourCI)this.getConnector()).update(state);
		
	}
	
	
	

}
