package devices;

import java.util.HashSet;
import java.util.Set;

import communication.CommunicationCI;
import communication.CommunicationInboundPort;
import communication.CommunicationOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import pstl.behaviour.BehaviourCI;
import pstl.behaviour.BehaviourInboundPort;
import pstl.behaviour.BehaviourOutboundPort;
import pstl.registrator.RegistrationOutboundPort;
import pstl.sensor.SensorCI;
import pstl.sensor.SensorInboundPort;
import pstl.sensor.SensorOutboundPort;
import pstl.state.StateCI;
import pstl.state.StateInboundPort;
import pstl.state.StateOutboundPort;
import pstl.util.Coord;

@OfferedInterfaces(offered = { SensorCI.class, StateCI.class, BehaviourCI.class, CommunicationCI.class })
@RequiredInterfaces(required = { SensorCI.class, StateCI.class, BehaviourCI.class, CommunicationCI.class })

public class Thermometer extends AbstractComponent {
	
	public static final String RegOP_URI = RegistrationOutboundPort.generatePortURI();
	public static final String SOP_URI = SensorOutboundPort.generatePortURI();
	public final String SIP_URI = SensorInboundPort.generatePortURI();
	public final String STOP_URI = StateOutboundPort.generatePortURI();
	public final String STIP_URI = StateInboundPort.generatePortURI();
	public final String BOP_URI = BehaviourOutboundPort.generatePortURI();
	public final String BIP_URI = BehaviourInboundPort.generatePortURI();
	public final String COP_URI = CommunicationOutboundPort.generatePortURI();
	public final String CIP_URI = CommunicationInboundPort.generatePortURI();
	
	public final String SIMIP_URI = SensorInboundPort.generatePortURI();
	
	private RegistrationOutboundPort regop;
	private SensorOutboundPort sop;
	private SensorInboundPort sip;
	private StateOutboundPort stop;
	private StateInboundPort stip;
	private BehaviourOutboundPort bop;
	private BehaviourInboundPort bip;
	private CommunicationOutboundPort cop;
	private CommunicationInboundPort cip;
	
	public static int count = 0;
	public static int genID() {
		Thermometer.count++;
		return Thermometer.count;
	} 
	
	private int myID = Thermometer.genID();
	private Coord location;
	private double myTemp;
	
	private int state;
	
	private Set<CommunicationOutboundPort> heaters = new HashSet<CommunicationOutboundPort>();
	
	protected Thermometer(Coord c) throws Exception {
		super(1, 0);
		this.location = c;
		this.getTemp();
		try {
			this.regop = new RegistrationOutboundPort(RegOP_URI, this);
			this.sop = new SensorOutboundPort(SOP_URI, this);
			this.sip = new SensorInboundPort(SIP_URI, this);
			this.stop = new StateOutboundPort(STOP_URI, this);
			this.stip = new StateInboundPort(STIP_URI, this);
			this.bop = new BehaviourOutboundPort(BOP_URI, this);
			this.bip = new BehaviourInboundPort(BIP_URI, this);
			this.cop = new CommunicationOutboundPort(COP_URI, this);
			this.cip = new CommunicationInboundPort(CIP_URI, this);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			this.regop.publishPort();
			this.sop.publishPort();
			this.sip.publishPort();
			this.stop.publishPort();
			this.stip.publishPort();
			this.bop.publishPort();
			this.bip.publishPort();
			this.cop.publishPort();
			this.cip.publishPort();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		this.toggleLogging();
		this.toggleTracing();
	}

	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		Set<String> hts = regop.getHeaters(location);
		for(String s : hts) {
			String uriTempR = RegistrationOutboundPort.generatePortURI();
			RegistrationOutboundPort rp = new RegistrationOutboundPort(uriTempR, this);
			
			
		}
		
		while(true) {
			this.newState();
		}
	}

	@Override
	public synchronized void finalise() throws Exception {
		super.finalise();
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		super.shutdown();
	}

	
	
	
	public void communicate(int address, double message) throws Exception{
		for(CommunicationOutboundPort p : heaters) {
			p.communicate(address, message);
		}
	}
	
	public double sense(Coord c) throws Exception{
		return this.myTemp;
	}
	
	public int update(int state) throws Exception{
		if(state == 1) {
			return 2;
		}
		else {
		if(state == 2) {
			return 1;
		}
		else {
			return 0;
		}}
	}
	
	//public void neighState(String address, double value) throws Exception{
	//}
	
	
	public void newState() throws Exception{
		this.state = update(state);
		
		if(state == 1) {
			getTemp();
		}
		if(state == 2) {
			communicate(myID, myTemp);
		}
	
	}
	
	
	public void getTemp() {
		try {
			this.myTemp = sop.sense(location);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
