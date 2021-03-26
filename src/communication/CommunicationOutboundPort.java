package communication;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import pstl.behaviour.BehaviourCI;

public class CommunicationOutboundPort extends AbstractOutboundPort implements CommunicationCI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3841381202158308345L;


	public CommunicationOutboundPort(ComponentI owner) throws Exception {
		super(BehaviourCI.class, owner);
	}
	public CommunicationOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BehaviourCI.class, owner);
	}
	
	
	@Override
	public void communicate(int address, double message) throws Exception {
		((CommunicationCI)this.getConnector()).communicate(address, message);
		
	}

}
