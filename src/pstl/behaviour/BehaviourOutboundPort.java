package pstl.behaviour;

import java.util.ArrayList;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import pstl.util.Pollution;

public class BehaviourOutboundPort extends AbstractOutboundPort implements BehaviourCI{
	
	private static final long serialVersionUID = 1L;
	public BehaviourOutboundPort(ComponentI owner) throws Exception {
		super(BehaviourCI.class, owner);
	}
	public BehaviourOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BehaviourCI.class, owner);
	}
	@Override
	public void loop() throws Exception {
		try {
			this.getOwner().runTask(
					b-> ((Behaviour) b).loop());
		} catch (Exception e) {
			e.printStackTrace();
		} 		
	}
	@Override
	public ArrayList<Pollution> TwoHopPolution() throws Exception {
		return this.getOwner().handleRequest(
				b -> ((Behaviour) b).TwoHopPolution());
	}
	@Override
	public void updateTemp() throws Exception {
		this.getOwner().runTask(
				b-> {
					try {
						((Behaviour) b).updateTemp();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}
}
