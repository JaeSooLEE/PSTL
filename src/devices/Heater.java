package devices;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import communication.CommunicationCI;
import communication.CommunicationInboundPort;
import communication.CommunicationOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import pstl.actuator.ActuatorCI;
import pstl.actuator.ActuatorInboundPort;
import pstl.actuator.ActuatorOutboundPort;
import pstl.behaviour.BehaviourCI;
import pstl.behaviour.BehaviourInboundPort;
import pstl.behaviour.BehaviourOutboundPort;
import pstl.registrator.RegistrationOutboundPort;
import pstl.sensor.SensorInboundPort;
import pstl.state.StateCI;
import pstl.state.StateInboundPort;
import pstl.state.StateOutboundPort;
import pstl.util.Coord;

@OfferedInterfaces(offered = { ActuatorCI.class, StateCI.class, BehaviourCI.class, CommunicationCI.class })
@RequiredInterfaces(required = {ActuatorCI.class, BehaviourCI.class, CommunicationCI.class })

public class Heater extends AbstractComponent {
	
	public static final String RegOP_URI = RegistrationOutboundPort.generatePortURI();
	public final String AOP_URI = ActuatorOutboundPort.generatePortURI();
	public final String AIP_URI = ActuatorInboundPort.generatePortURI();
	public final String STOP_URI = StateOutboundPort.generatePortURI();
	public final String STIP_URI = StateInboundPort.generatePortURI();
	public final String BOP_URI = BehaviourOutboundPort.generatePortURI();
	public final String BIP_URI = BehaviourInboundPort.generatePortURI();
	public final String COP_URI = CommunicationOutboundPort.generatePortURI();
	public final String CIP_URI = CommunicationInboundPort.generatePortURI();
	
	public final String SIMIP_URI = SensorInboundPort.generatePortURI();
	
	private RegistrationOutboundPort regop;
	private ActuatorOutboundPort aop;
	private ActuatorInboundPort aip;
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
	
	private int state;
	
	Map<Integer, Double> temps = new HashMap<Integer, Double>(); 
	
	protected Heater(Coord c) throws Exception {
		super(1, 0);
		this.location = c;
		try {
			this.regop = new RegistrationOutboundPort(RegOP_URI, this);
			this.aop = new ActuatorOutboundPort(AOP_URI, this);
			this.aip = new ActuatorInboundPort(AIP_URI, this);
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
			this.aop.publishPort();
			this.aip.publishPort();
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

	private double getAverageTemp() {
		if(temps.isEmpty()) {return 17;}
		else {
			double average = 0;
			int i = 0;
			
			for(Entry<Integer, Double> e : temps.entrySet()) {
				average  +=  e.getValue();
				i++;
			}
			return (average/i);
		}
	}
	
	
	public void communicate(int address, double message) throws Exception{
		temps.put(address, message);
	}
	
	public void act(Coord c, double var) throws Exception{
		aop.act(c, var);
	}
	
	public int update(int state) throws Exception{
		double temp = getAverageTemp();
		if(temp >= 17) {return 0;}
		else {
		if(temp >= 14) {return 1;}
		else {
		if(temp >= 10) {return 2;}
		else {
		if(temp >= 5) {return 3;}		
		else {
		if(temp >= 2) {return 4;}		
		else {return 5;
		}}}}}
	}
	
	//public void neighState(String address, double value) throws Exception{
	//}
	
	
	public void newState() throws Exception{
		this.state = update(state);
		
		act(location,state);
	
	}
	
}
