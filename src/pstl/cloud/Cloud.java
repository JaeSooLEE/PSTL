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
	Map<Integer, Map<Address,BehaviourInboundPort>> behaviours = new HashMap<Integer, Map<Address,BehaviourInboundPort>>(); 
	
	protected boolean safety = false;
	protected int safetyRoom = 0;
	protected int safetyThreshHold = 4;
	
	BehaviourI subBehaviourThermometer = (s, val)->s++%2+1;
	BehaviourI subBehaviourHeater = new BehaviourI(){
		@Override
		public int update(int s, double val){
			double temp = val;
			if(temp >= 18) {return 0;}
			else {
			if(temp >= 16) {return 1;}
			else {
			if(temp >= 14) {return 2;}
			else {
			if(temp >= 11) {return 3;}		
			else {
			if(temp >= 9) {return 4;}		
			else {return 5;
		}}}}}}};
		
		
	// pooling 
	protected static final String	POOL_URI = "safety_pool" ;
	protected static final int		NTHREADS = 2 ;
	
	
	protected Cloud() throws Exception  {
		super(1, 0);
		this.cop = new CommunicationOutboundPort(this.COP_URI, this);
		this.cip = new CommunicationInboundPort(Cloud.CIP_URI, this);
		
		this.cop.publishPort();
		this.cip.publishPort();
		
		this.createNewExecutorService(POOL_URI, NTHREADS, false) ;
		
	}

	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		
		this.runTaskOnComponent(
				POOL_URI,
				new AbstractComponent.AbstractTask() {
					
					@Override
					public void run() {
						try {
							int i=0;
							while(i<10) {

							Thread.sleep(1000L) ;
							if(safety) {
								toggleSafety();
								Thread.sleep(3000L) ;
								unToggleSafety();
							}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						}});
		
		
		
	}

	@Override
	public synchronized void finalise() throws Exception {
		for(Entry<Address, Double> e: this.averages.entrySet() ) {
			System.out.println(e.getKey()+": average temp = "+e.getValue());
		}
		super.finalise();
	}
	public void toggleSafety() throws Exception {
		
		for(Entry<Address, BehaviourInboundPort> e :behaviours.get(safetyRoom).entrySet()) {
			
			if(e.getKey().isHeater()) {
				System.out.println("toggeling");
				e.getValue().update(-1, 0);
				this.safety = true;
			}
		}
		
	}
	
	public void unToggleSafety() throws Exception {
		for(Entry<Address, BehaviourInboundPort> e :behaviours.get(safetyRoom).entrySet()) {
			if(e.getKey().isHeater()) {
				System.out.println("untoggeling");
				e.getValue().update(-1, 0);
				this.safety = false;
			}
		}
		
	}

	@Override
	public String communicate(Address address, String code, double val, String body) throws Exception {
		if(address.isThermomerer() && code.equals("log")) {
			if(tempLog.containsKey(address)) {
				double tmp = averages.get(address);
				if(tmp-val>safetyThreshHold) {
					
					safetyRoom = Integer.parseInt(body);
					safety=true;
				}
				int s = tempLog.get(address).size();
				
				averages.put(address, ((tmp*s)/(s+1))+(val/(s+1)));
				tempLog.get(address).add(val);
				
			}else {
				Set<Double> tmp = new HashSet<Double>();
				tmp.add(val);
				tempLog.put(address, tmp);
				averages.put(address, val);
			}
		}else {
		if(code.equals("behaviour")) {
			
			if(address.isThermomerer()) {
				String SUB_BEHAVIOUR_URI;
				BehaviourInboundPort sub_bip;
				SUB_BEHAVIOUR_URI = this.createSubcomponent(Behaviour.class.getCanonicalName(), new Object[]{body,subBehaviourThermometer}) ;
				sub_bip =(BehaviourInboundPort)this.findSubcomponentInboundPortFromURI(SUB_BEHAVIOUR_URI, body) ;
				if(behaviours.containsKey((int)val)) {
					behaviours.get( (int)val ).put(address, sub_bip);
				}
				else {
					Map<Address,BehaviourInboundPort> tmp = new HashMap<Address,BehaviourInboundPort>();
					tmp.put(address, sub_bip);
					behaviours.put((int) val, tmp);
				}
			
			}else {
				if(address.isHeater()) {
					String SUB_BEHAVIOUR_URI;
					BehaviourInboundPort sub_bip;
					SUB_BEHAVIOUR_URI = this.createSubcomponent(Behaviour.class.getCanonicalName(), new Object[]{body,subBehaviourHeater}) ;
					sub_bip =(BehaviourInboundPort)this.findSubcomponentInboundPortFromURI(SUB_BEHAVIOUR_URI, body) ;
					
					
					if(behaviours.containsKey((int)val)) {
						behaviours.get((int)val).put(address, sub_bip);
					}
					else {
						Map<Address,BehaviourInboundPort> tmp = new HashMap<Address,BehaviourInboundPort>();
						tmp.put(address, sub_bip);
						behaviours.put((int) val, tmp);
					}
				
					
				}
			}

		}	
		}
		return "OK";
		
	}
}
