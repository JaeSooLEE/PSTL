package pstl.state;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class StateInboundPort extends AbstractInboundPort implements StateCI {

	public StateInboundPort(ComponentI owner) throws Exception {
		super(StateInboundCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	public StateInboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri, StateInboundCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	/*
	public StateInboundPort(Class<? extends OfferedCI> implementedInterface, ComponentI owner, String pluginURI,
			String executorServiceURI) throws Exception {
		super(implementedInterface, owner, pluginURI, executorServiceURI);
		// TODO Auto-generated constructor stub
	}

	public StateInboundPort(String uri, Class<? extends OfferedCI> implementedInterface, ComponentI owner,
			String pluginURI, String executorServiceURI) throws Exception {
		super(uri, implementedInterface, owner, pluginURI, executorServiceURI);
		// TODO Auto-generated constructor stub
	}
	
	*/
	
	
	@Override
	public void newState() throws Exception {
		this.getOwner().handleRequest(
				b -> ((StateCI) b).newState());
	}

	@Override
	public void neighState(String address, double value) throws Exception{
		this.getOwner().handleRequest(
				b -> ((StateCI) b).neighState(address, value));
	}

}
