package pstl.cloud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.behaviour.Behaviour;
import pstl.behaviour.BehaviourCI;
import pstl.behaviour.BehaviourI;
import pstl.behaviour.BehaviourInboundPort;
import pstl.communication.CommunicationCI;
import pstl.communication.CommunicationI;
import pstl.communication.CommunicationInboundPort;
import pstl.communication.CommunicationOutboundPort;
import pstl.util.Address;

@OfferedInterfaces(offered = {CommunicationCI.class, BehaviourCI.class })
@RequiredInterfaces(required = {CommunicationCI.class })
/**
 * the Cloud class is an abstraction of a server on the cloud, this is not a component so is doesn't adhere 
 * to the pulverization paradigm
 *
 */
public class Cloud extends AbstractComponent implements CommunicationI, BehaviourI{
	
	protected final String COP_URI = CommunicationOutboundPort.generatePortURI();
	public static final String CIP_URI = "server_cip_uri";
	protected CommunicationOutboundPort cop;
	protected CommunicationInboundPort cip;
	
	public static final String BIP_URI = "server_bip_uri";
	protected BehaviourInboundPort bip;
	
	Map<Address, Set<Double>> tempLog = new HashMap<Address, Set<Double>>(); 
	Map<Address, Double> averages = new HashMap<Address, Double>(); 
	Map<Integer, Map<Address,BehaviourInboundPort>> behaviours = new HashMap<Integer, Map<Address,BehaviourInboundPort>>(); 
	
	
	// params of the system that maxes the heat if a door is open (referred to as safety here)
	protected boolean safety = false;
	protected int safetyRoom = 0;
	protected int safetyThreshHold = 4;
	
	
	//the lambdas for the behavior of both the Thermometer and the Heater
	BehaviourI subBehaviourThermometer = (a,s, val)->s++%2+1;
	BehaviourI subBehaviourHeater = new BehaviourI(){
		@Override
		public int update(Address address, int s, double val){
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
		this.bip = new BehaviourInboundPort(Cloud.BIP_URI, this);
		
		this.cop.publishPort();
		this.cip.publishPort();
		this.bip.publishPort();
		this.createNewExecutorService(POOL_URI, NTHREADS, false) ;
		
	}

	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		// we launch a thread to take care of toggling on the safety and then off after a certain time 
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
		this.cop.unpublishPort();
		this.cip.unpublishPort();
		this.bip.unpublishPort();
		super.finalise();
	}
	
	
	/**
	 * the function that toggles the safety on, uses the behavors stored on the servers directly to change 
	 * their value of return to the thermometer and the heater 
	 * @throws Exception
	 */
	public void toggleSafety() throws Exception {
		
		for(Entry<Address, BehaviourInboundPort> e :behaviours.get(safetyRoom).entrySet()) {
			
			if(e.getKey().isHeater()) {
				System.out.println("toggeling");
				e.getValue().update(null, -1, 0);
				this.safety = true;
			}
		}
		
	}
	/**
	 * the function that untoggles the safety
	 * @throws Exception
	 */
	public void unToggleSafety() throws Exception {
		for(Entry<Address, BehaviourInboundPort> e :behaviours.get(safetyRoom).entrySet()) {
			if(e.getKey().isHeater()) {
				System.out.println("untoggeling");
				e.getValue().update(null, -1, 0);
				this.safety = false;
			}
		}
		
	}
	/**
	 * the communicate function does mainly 2 things:
	 * it logs the temperatures measured by the thermometers 
	 * and it listens to Behaviour creation requests, it creates them and stores them 
	 * under the right address and room
	 */
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
	
	/**
	 * this function handles the requests of all components to their respective behaviors and 
	 * directs the requests twords the correspoding Behaviour stored in the Cloud
	 */
	@Override
	public int update(Address address, int state, double val) throws Exception {
		for(Entry<Integer, Map<Address,BehaviourInboundPort>> e1: this.behaviours.entrySet()) {
			for(Entry<Address,BehaviourInboundPort> e2: e1.getValue().entrySet()) {
				if(e2.getKey().equals(address)) {
					return e2.getValue().update(address, state, val);
				}
			}
		}
		return 0;
	}
}
