package pstl.state;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class StateInboundPort extends AbstractInboundPort implements StateCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5477044762728649467L;

	public StateInboundPort(ComponentI owner) throws Exception {
		super(StateCI.class, owner);
	}

	public StateInboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri, StateCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void newState() throws Exception {
		this.getOwner().runTask(
				s-> {
					try {
						((StateCI) s).newState();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				});
	}

	@Override
	public void neighState(String address, double value) throws Exception{
		this.getOwner().runTask(
				s-> {
					try {
						((StateCI) s).neighState(address, value);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				});
	}

}
