package pstl.state;

import communication.CommunicationCI;
import communication.CommunicationI;
import communication.CommunicationInboundPort;
import communication.CommunicationOutboundPort;
import connecteurs.BehaviourConnector;
import connecteurs.CommunicationConnector;
import connecteurs.SensorConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.actuator.ActuatorCI;
import pstl.behaviour.BehaviourCI;
import pstl.behaviour.BehaviourOutboundPort;
import pstl.sensor.SensorOutboundPort;
import pstl.util.Address;
import pstl.util.Coord;
import simulator.Simulator;


@OfferedInterfaces(offered = { StateCI.class, CommunicationCI.class })
@RequiredInterfaces(required = {StateCI.class, BehaviourCI.class, ActuatorCI.class, CommunicationCI.class})
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
	
	@Override
	public void newState() throws Exception{
	
		if(ready) {
		state = bop.update(state, myTemp);
		


		if(state == 1) {
			getTemp();
		}
		if(state == 2) {
			broadcast();
		}
		}
	}
	
	
	public void broadcast() throws Exception {
		cop.communicate(address, "temp", myTemp, "");
		cop.communicate(address, "log", myTemp, "");
	}
	
	public void getTemp() {
		try {
			this.myTemp = this.sop.sense(location);
			
			this.logMessage(address + ": temp " + Simulator.round(myTemp, 0) + " at "+ location);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void initBehaviour() throws Exception {
		String s = cop.communicate(address, "behaviour", 0, BIP_URI);
		this.doPortConnection(BOP_URI, BIP_URI, BehaviourConnector.class.getCanonicalName());
		
		this.ready = true;
	}
	
	@Override
	public String communicate(Address address, String code, double val, String body) throws Exception {
		return "KO";
		
	}

}
