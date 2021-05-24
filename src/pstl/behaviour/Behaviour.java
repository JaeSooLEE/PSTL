package pstl.behaviour;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;

@OfferedInterfaces(offered = { BehaviourCI.class })
@RequiredInterfaces(required = {BehaviourCI.class})
public class Behaviour extends AbstractComponent implements BehaviourI{
	public final String BIP_URI;
	protected BehaviourI f;
	
	protected BehaviourInboundPort bip;
	
	protected Behaviour(String BIP_URI, BehaviourI f) {
		super(1, 0);

		this.BIP_URI=BIP_URI;
		this.f = f;
		
		try {
			this.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	protected void initialise() throws Exception {
		this.bip = new BehaviourInboundPort(this.BIP_URI, this);
		this.bip.publishPort();
	}


	public int update(int state, double val) throws Exception {
		return f.update(state, val);
	}

}
