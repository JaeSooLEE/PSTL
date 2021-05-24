package pstl.cloud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import communication.CommunicationCI;
import communication.CommunicationI;
import communication.CommunicationInboundPort;
import communication.CommunicationOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.behaviour.Behaviour;
import pstl.behaviour.BehaviourI;
import pstl.behaviour.BehaviourInboundPort;
import pstl.util.Address;

@OfferedInterfaces(offered = {CommunicationCI.class })
@RequiredInterfaces(required = {CommunicationCI.class })

public class Cloud extends AbstractComponent implements CommunicationI{
	
	protected final String COP_URI = CommunicationOutboundPort.generatePortURI();
	public static final String CIP_URI = "server_ip_uri";
	protected CommunicationOutboundPort cop;
	protected CommunicationInboundPort cip;
	
	Map<Address, Set<Double>> tempLog = new HashMap<Address, Set<Double>>(); 
	Map<Address, Double> averages = new HashMap<Address, Double>(); 
	Map<Address, BehaviourInboundPort> behaviours = new HashMap<Address, BehaviourInboundPort>(); 
	
	
	BehaviourI subBehaviourThermometer = (s, val)->s++%2+1;
	
	protected Cloud() throws Exception  {
		super(1, 0);
		this.cop = new CommunicationOutboundPort(this.COP_URI, this);
		this.cip = new CommunicationInboundPort(Cloud.CIP_URI, this);
		
		this.cop.publishPort();
		this.cip.publishPort();
		
		
		
	}


	@Override
	public synchronized void finalise() throws Exception {
		for(Entry<Address, Double> e: this.averages.entrySet() ) {
			System.out.println(e.getKey()+": average temp = "+e.getValue());
		}
		super.finalise();
	}
	public void toggleSafety() {
		
	}

	@Override
	public String communicate(Address address, String code, double val, String body) throws Exception {
		if(address.isThermomerer() && code.equals("log")) {
			if(tempLog.containsKey(address)) {
				double tmp = averages.get(address);
				if(tmp-val<4) {
					toggleSafety();
				}
				int s = tempLog.get(address).size();
				averages.put(address, (tmp*s)/(s+1)+val/s+1);
				tempLog.get(address).add(val);
				
			}else {
				Set<Double> tmp = new HashSet<Double>();
				tmp.add(val);
				tempLog.put(address, tmp);
				averages.put(address, val);
			}
		}else {
		if(code.equals("behaviour")) {
			String SUB_BEHAVIOUR_URI;
			String SUB_BIP_URI = BehaviourInboundPort.generatePortURI();
			BehaviourInboundPort sub_bip;
			SUB_BEHAVIOUR_URI = this.createSubcomponent(Behaviour.class.getCanonicalName(), new Object[]{body,subBehaviourThermometer}) ;
			sub_bip =(BehaviourInboundPort)this.findSubcomponentInboundPortFromURI(SUB_BEHAVIOUR_URI, SUB_BIP_URI) ;
			behaviours.put(address, sub_bip);

		}	
		}
		return "OK";
		
	}
}
