package communication;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import pstl.util.Address;

public class CommunicationInboundPort  extends AbstractInboundPort implements CommunicationCI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8187884600035310175L;
	public CommunicationInboundPort(ComponentI owner) throws Exception {
		super(CommunicationCI.class, owner);
	}
	public CommunicationInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, CommunicationCI.class, owner);
	}
	@Override
	public String communicate(Address address, String code, double val, String body) throws Exception {
		return this.getOwner().handleRequest(
				c -> ((CommunicationI) c).communicate(address, code, val, body));
		
	}

}
