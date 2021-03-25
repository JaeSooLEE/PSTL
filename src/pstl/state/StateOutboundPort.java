package pstl.state;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import pstl.behaviour.BehaviourCI;

public class StateOutboundPort extends AbstractOutboundPort implements StateCI {

	public StateOutboundPort(ComponentI owner) throws Exception {
		super(StateCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	public StateOutboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri, StateCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void neighState(String address, double value) throws Exception{
		return ((StateCI)this.getConnector()).neighState(address, value);
		
	}
	
	
	@Override
	public void newState() throws Exception {
		
		return ((StateCI)this.getConnector()).newState();
		
	}
	
	
	



}
