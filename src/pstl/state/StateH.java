package pstl.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import communication.CommunicationCI;
import communication.CommunicationI;
import communication.CommunicationInboundPort;
import communication.CommunicationOutboundPort;
import connecteurs.ActuatorConnector;
import connecteurs.BehaviourConnector;
import connecteurs.CommunicationConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.actuator.ActuatorCI;
import pstl.actuator.ActuatorOutboundPort;
import pstl.behaviour.BehaviourCI;
import pstl.behaviour.BehaviourOutboundPort;
import pstl.util.Address;
import pstl.util.Coord;


@OfferedInterfaces(offered = { StateCI.class, CommunicationCI.class })
@RequiredInterfaces(required = {StateCI.class, BehaviourCI.class, ActuatorCI.class})
public class StateH extends AbstractComponent implements StateI, CommunicationI{
	public final String STIP_URI;
	public final String BIP_URI;
	public final String AIP_URI;
	public final String CIP_URI;
	public final String COM_CIP_URI;
	public final String BOP_URI = BehaviourOutboundPort.generatePortURI();
	public final String AOP_URI = ActuatorOutboundPort.generatePortURI();
	public final String COP_URI = CommunicationOutboundPort.generatePortURI();
	
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

	@Override
	public void newState() throws Exception {
		if(ready) {
			int val = (int)this.getAverageTemp();
			state = bop.update(state, val);
			logMessage(address +": mode " + state + " at " +location );
			aop.act(location,state);
		}
	}
	public void initBehaviour() throws Exception {
		String s = cop.communicate(address, "behaviour", this.room, BIP_URI);
		this.doPortConnection(BOP_URI, BIP_URI, BehaviourConnector.class.getCanonicalName());
		this.ready = true;
	}
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
		if(address.isThermomerer() && code.equals("temp")) {
			temps.put(address, val);
			return "OK";
		}
		return "KO";
		
	}

}
