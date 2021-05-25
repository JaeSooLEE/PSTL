package pstl.state;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.actuator.ActuatorCI;
import pstl.behaviour.BehaviourCI;
import pstl.behaviour.BehaviourOutboundPort;
import pstl.cloud.Cloud;
import pstl.communication.CommunicationCI;
import pstl.communication.CommunicationI;
import pstl.communication.CommunicationInboundPort;
import pstl.communication.CommunicationOutboundPort;
import pstl.connecteurs.BehaviourConnector;
import pstl.connecteurs.CommunicationConnector;
import pstl.connecteurs.SensorConnector;
import pstl.sensor.SensorOutboundPort;
import pstl.simulator.Simulator;
import pstl.util.Address;
import pstl.util.Coord;


@OfferedInterfaces(offered = { StateCI.class, CommunicationCI.class })
@RequiredInterfaces(required = {StateCI.class, BehaviourCI.class, ActuatorCI.class, CommunicationCI.class})
/**
 * the class State is a reusable sub-component in charge of 
 * the updating the state, getting and processing the inputs and deciding on the next action to take, 
 * at creation it is given the inbound port that is used later to contact it  
 *
 *this class is specific to the Thermometer
 */
public class StateT extends AbstractComponent implements StateI, CommunicationI{
	public final String STIP_URI;
	public final String BIP_URI;
	public final String CIP_URI;
	public final String COM_CIP_URI;
	public final String BOP_URI = BehaviourOutboundPort.generatePortURI();
	public final String SOP_URI = SensorOutboundPort.generatePortURI();
	public final String COP_URI = CommunicationOutboundPort.generatePortURI();
	
	protected StateInboundPort stip;
	protected BehaviourOutboundPort bop;
	protected SensorOutboundPort sop;
	protected CommunicationInboundPort cip;
	protected CommunicationOutboundPort cop;
	
	private boolean ready = false;
	public int state;
	public Address address;
	public Coord location;
	public int room;
	private double myTemp;
	
	protected StateT(Address address, Coord location, int room, String STIP_URI, String BIP_URI, String CIP_URI, String COM_CIP_URI) {
		super(1, 0);
		this.address=address;
		this.location=location;
		this.room=room;
		
		this.STIP_URI=STIP_URI;
		this.BIP_URI =BIP_URI;
		this.COM_CIP_URI =COM_CIP_URI;
		this.CIP_URI=CIP_URI;
		this.location=location;
		try {
			this.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.toggleLogging();
		this.toggleTracing();
		
	}
	
	@Override
	public synchronized void execute() throws Exception {
		initBehaviour();
		super.execute();
	}
	
	
	/**
	 * Initialise is in charge of creating and publishing the ports
	 * the inbound port is created with the provided URI
	 * @throws Exception
	 */
	protected void initialise() throws Exception {
		this.stip = new StateInboundPort(this.STIP_URI, this);
		this.bop = new BehaviourOutboundPort(this.BOP_URI, this);
		this.sop = new SensorOutboundPort(this.SOP_URI, this);
		this.cip = new CommunicationInboundPort(this.CIP_URI, this);
		this.cop = new CommunicationOutboundPort(this.COP_URI, this);
		this.stip.publishPort();
		this.bop.publishPort();
		this.sop.publishPort();
		this.cop.publishPort();
		this.cip.publishPort();
		this.doPortConnection(SOP_URI, Simulator.SIP_URI, SensorConnector.class.getCanonicalName());
		this.doPortConnection(COP_URI, COM_CIP_URI, CommunicationConnector.class.getCanonicalName());
		this.state=1;
	}
	
	/**
	 * newState is the main function of State (should be reprogrammed at each use) 
	 * it examines the inputs (Communicator and Sensor), gets the next state from Behaviour, and takes action with Actuator  
	 */
	@Override
	public void newState() throws Exception{
	
		if(ready) {
		//using Behaviour to get the next state (an alternation between 1=sensing and 2=communication)
		state = bop.update(address, state, myTemp);
		


		if(state == 1) {
			//sensing function
			getTemp();
		}
		if(state == 2) {
			//communication function
			broadcast();
		}
		}
	}
	
	/**
	 * broadcast sends the last measurement to all the heaters in the room as well as to the cloud 
	 * @throws Exception
	 */
	public void broadcast() throws Exception {
		//sending the temperature to the heaters (code = "temp")
		cop.communicate(address, "temp", myTemp, "");
		//sending the temperature to the Cloud (code = "log")
		cop.communicate(address, "log", myTemp, ""+this.room);
	}
	
	/**
	 * uses the Sensor to get the current temperature at this location
	 */
	public void getTemp() {
		try {
			this.myTemp = this.sop.sense(location);
			
			this.logMessage(address + ": temp " + Simulator.round(myTemp, 0) + " at "+ location);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * in our implementation, the Behaviour sub-component is located on the Cloud
	 * this function uses Communicator to contact the Cloud, and requests the creation of a 
	 * Behaviour sub-component 
	 * @throws Exception
	 */
	public void initBehaviour() throws Exception {
		String s = cop.communicate(address, "behaviour", this.room, BIP_URI);
		if(!s.equals("OK")) {throw new Exception("communication failed");}
		this.doPortConnection(BOP_URI, Cloud.BIP_URI, BehaviourConnector.class.getCanonicalName());
		
		this.ready = true;
	}
	
	@Override
	public String communicate(Address address, String code, double val, String body) throws Exception {
		return "KO";
		
	}

}
