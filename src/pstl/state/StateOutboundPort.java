package pstl.state;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class StateOutboundPort extends AbstractOutboundPort implements StateCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4155493516822640173L;


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
		((StateCI)this.getConnector()).neighState(address, value);
		
	}
	
	
	@Override
	public void newState() throws Exception {
		
		((StateCI)this.getConnector()).newState();
		
	}
	
	
	



}
