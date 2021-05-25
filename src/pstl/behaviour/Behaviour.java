package pstl.behaviour;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.util.Address;
@OfferedInterfaces(offered = { BehaviourCI.class })
@RequiredInterfaces(required = {BehaviourCI.class})
/**
 * the class Behaviour is a reusable sub-component in charge of 
 * determining the behavior, at creation it is given the inbound 
 * port that is used later to contact it, as well as a lambda that describes the "behavior" 
 *
 */
public class Behaviour extends AbstractComponent implements BehaviourI{
	public final String BIP_URI;
	protected BehaviourI f;
	
	protected BehaviourInboundPort bip;
	
	
	protected boolean lock = false;
	protected Behaviour(String BIP_URI, BehaviourI f) {
		super(3, 2);

		this.BIP_URI=BIP_URI;
		this.f = f;
		
		try {
			this.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * Initialise is in charge of creating and publishing the ports
	 * the inbound port is created with the provided URI
	 * @throws Exception
	 */
	protected void initialise() throws Exception {
		this.bip = new BehaviourInboundPort(this.BIP_URI, this);
		this.bip.publishPort();
	}

	/**
	 * update is the main function of Behaviour (should be reprogrammed at each use) 
	 * @param address the address of the calling component
	 * @param state the current state of the caller 
	 * @param val the value gathered from inputs by the state 
	 * @return the next state  
	 * 
	 */
	public int update(Address address, int state, double val) throws Exception {
		
		if(state == -1) {this.lock=!lock;}
		//return max if the safety is on
		if(lock) {return 5;}
		// else return the value of the provided lambda 
		return f.update(address, state, val);
	}

}
