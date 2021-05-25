package pstl.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.actuator.ActuatorCI;
import pstl.actuator.ActuatorOutboundPort;
import pstl.behaviour.BehaviourCI;
import pstl.behaviour.BehaviourOutboundPort;
import pstl.cloud.Cloud;
import pstl.communication.CommunicationCI;
import pstl.communication.CommunicationI;
import pstl.communication.CommunicationInboundPort;
import pstl.communication.CommunicationOutboundPort;
import pstl.connecteurs.ActuatorConnector;
import pstl.connecteurs.BehaviourConnector;
import pstl.connecteurs.CommunicationConnector;
import pstl.util.Address;
import pstl.util.Coord;


@OfferedInterfaces(offered = { StateCI.class, CommunicationCI.class })
@RequiredInterfaces(required = {StateCI.class, BehaviourCI.class, ActuatorCI.class})

/**
 * the class State is a reusable sub-component in charge of 
 * the updating the state, getting and processing the inputs and deciding on the next action to take, 
 * at creation it is given the inbound port that is used later to contact it  
 *
 *
 *this class is specific to the Heater
 */
public class StateH extends AbstractComponent implements StateI, CommunicationI{
	public final String STIP_URI;
	public final String BIP_URI;
	public final String AIP_URI;
	public final String CIP_URI;
	public final String COM_CIP_URI;
	public final String BOP_URI = BehaviourOutboundPort.generatePortURI();
	public final String AOP_URI = ActuatorOutboundPort.generatePortURI();
	public final String COP_URI = CommunicationOutboundPort.generatePortURI();
	
	
	//ports of the other subcomponents 
	protected StateInboundPort stip;
	protected BehaviourOutboundPort bop;
	protected ActuatorOutboundPort aop;
	protected CommunicationInboundPort cip;
	protected CommunicationOutboundPort cop;
	
	
	public int state;
	public Address address;
	public Coord location;
	public int room;
	private boolean ready = false;
	
	// a map of temperatures from the different thermometers in the room
	Map<Address, Double> temps = new HashMap<Address, Double>(); 
	
	protected StateH(Address address, Coord location, int room, String STIP_URI, String BIP_URI, String AIP_URI, String CIP_URI, String COM_CIP_URI) {
		super(1, 0);
		this.address=address;
		this.location=location;
		this.room=room;
		this.STIP_URI=STIP_URI;
		this.BIP_URI =BIP_URI;
		this.AIP_URI =AIP_URI;
		this.CIP_URI=CIP_URI;
		this.COM_CIP_URI=COM_CIP_URI;
		this.location=location;
		try {
			this.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.toggleLogging();
		this.toggleTracing();
		
	}
	
	/**
	 * Initialise is in charge of creating and publishing the ports
	 * the inbound port is created with the provided URI
	 * @throws Exception
	 */
	protected void initialise() throws Exception {
		this.stip = new StateInboundPort(this.STIP_URI, this);
		this.bop = new BehaviourOutboundPort(this.BOP_URI, this);
		this.aop = new ActuatorOutboundPort(this.AOP_URI, this);
		this.cip = new CommunicationInboundPort(this.CIP_URI, this);
		this.cop = new CommunicationOutboundPort(this.COP_URI, this);
		this.stip.publishPort();
		this.bop.publishPort();
		this.aop.publishPort();
	    this.cip.publishPort();
		this.cop.publishPort();


		this.doPortConnection(AOP_URI, AIP_URI, ActuatorConnector.class.getCanonicalName());
		this.doPortConnection(COP_URI, COM_CIP_URI, CommunicationConnector.class.getCanonicalName());
	}
	
	@Override
	public synchronized void execute() throws Exception {
		initBehaviour();
		super.execute();
	}

	/**
	 * newState is the main function of State (should be reprogrammed at each use) 
	 * it examines the inputs (Communicator and Sensor), gets the next state from Behaviour, and takes action with Actuator  
	 */
	@Override
	public void newState() throws Exception {
		if(ready) {
			//getting the average temperature in the room
			int val = (int)this.getAverageTemp();
			//using Behaviour to get the next state depending on the inputs and the current state 
			//in this case the state represents the intensity of the heater from 0 (off) to 5 (max)
			state = bop.update(address, state, val);
			logMessage(address +": mode " + state + " at " +location );
		    // using the Actuator to take action (heat the room)
			aop.act(location,state);
		}
	}
	
	/**
	 * in our implementation, the Behaviour sub-component is located on the Cloud
	 * this function uses Communicator to contact the Cloud, and requests the creation of a 
	 * Behaviour sub-component 
	 * @throws Exception
	 */
	public void initBehaviour() throws Exception {
		//sending a create behaviour request (code = "behaviour") 
		String s = cop.communicate(address, "behaviour", this.room, BIP_URI);
		if(!s.equals("OK")) {throw new Exception("communication failed");}
		this.doPortConnection(BOP_URI, Cloud.BIP_URI, BehaviourConnector.class.getCanonicalName());
		this.ready = true;
	}
	
	/**
	 * calculates the average temperature of the room from the last measurement of all 
	 * the thermometers in the same room 
	 * @return
	 */
	private double getAverageTemp() {
		if(temps.isEmpty()) {return 17;}
		else {
			double average = 0;
			int i = 0;
			
			for(Entry<Address, Double> e : temps.entrySet()) {
				average  +=  e.getValue();
				i++;
			}
			return (average/i);
		}
	}
	
	@Override
	public String communicate(Address address, String code, double val, String body) throws Exception {
		//recieving the temperature  from thermometers (code = "temp")
		if(address.isThermomerer() && code.equals("temp")) {
			temps.put(address, val);
			return "OK";
		}
		return "KO";
		
	}

}
