package communication;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import pstl.behaviour.BehaviourCI;

public class CommunicationInboundPort  extends AbstractInboundPort implements CommunicationCI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8187884600035310175L;
	public CommunicationInboundPort(ComponentI owner) throws Exception {
		super(BehaviourCI.class, owner);
	}
	public CommunicationInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BehaviourCI.class, owner);
	}
	@Override
	public void communicate(String address, String message) throws Exception {

		this.getOwner().runTask(
				c-> {
					try {
						((CommunicationCI) c).communicate(address, message);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		
	}

}
