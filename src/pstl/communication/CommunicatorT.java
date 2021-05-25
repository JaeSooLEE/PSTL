package pstl.communication;

import java.util.HashSet;
import java.util.Set;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.cloud.Cloud;
import pstl.connecteurs.CommunicationConnector;
import pstl.connecteurs.RegistrationConnector;
import pstl.registrator.RegistrationCI;
import pstl.registrator.RegistrationOutboundPort;
import pstl.registrator.Registrator;
import pstl.util.Address;
import pstl.util.Coord;

@OfferedInterfaces(offered = { CommunicationCI.class })
@RequiredInterfaces(required = {CommunicationCI.class, RegistrationCI.class,})
/**
 * the class Communicator is a reusable sub-component in charge of 
 * the Communicating function, at creation it is given the inbound 
 * port that is used later to contact it as well as the port uri of the 
 * state so that it passes back the messages directed to it
 * 
 * the Communicator is also in charge of the Registration of the global component 
 * 
 * this class is specific to the Thermometer (different approach from using a lambda function)
 *
 */
public class CommunicatorT extends AbstractComponent implements CommunicationI{
	public final String ROP_URI = RegistrationOutboundPort.generatePortURI();
	public final String CIP_URI;
	public final String PARENT_CIP_URI;
	public final String STATE_CIP_URI;
	public final String COP_URI = CommunicationOutboundPort.generatePortURI();
	public final String CLOUD_COP_URI = CommunicationOutboundPort.generatePortURI();

	
	public Address address;
	public Coord location;
	public int room;
	
	protected RegistrationOutboundPort regop;
	protected CommunicationOutboundPort cop;
	protected CommunicationInboundPort cip;
	
	protected CommunicationOutboundPort cloudCop;
	
	// the list of the heaters in the same room
	private Set<CommunicationOutboundPort> heaters = new HashSet<CommunicationOutboundPort>();
	private Set<String> ht =  new HashSet<String>();  
	
	protected CommunicatorT(Address address, Coord location, int room, String CIP_URI, String PARENT_CIP_URI, String STATE_CIP_URI) {
		super(1, 0);
		
		this.address=address;
		this.location=location;
		this.room=room;
		this.CIP_URI=CIP_URI;
		this.PARENT_CIP_URI=PARENT_CIP_URI;
		this.STATE_CIP_URI=STATE_CIP_URI;
		
		
		try {
			this.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Initialise is in charge of creating and publishing the ports
	 * the inbound and outbound ports are created with the provided URIs
	 * it also has a port directed towards the Cloud
	 * @throws Exception
	 */
	protected void initialise() throws Exception {
		this.regop = new RegistrationOutboundPort(ROP_URI, this);
		this.cop = new CommunicationOutboundPort(this.COP_URI, this);
		this.cloudCop = new CommunicationOutboundPort(this.CLOUD_COP_URI, this);
		this.cip = new CommunicationInboundPort(this.CIP_URI, this);
		this.regop.publishPort();
		this.cop.publishPort();
		this.cip.publishPort();
		this.cloudCop.publishPort();
		
		this.doPortConnection(ROP_URI, Registrator.RegIP_URI, RegistrationConnector.class.getCanonicalName());
		
	}
	protected void doLateConnections() throws Exception {
		this.doPortConnection(COP_URI, this.STATE_CIP_URI, CommunicationConnector.class.getCanonicalName());
		this.doPortConnection(CLOUD_COP_URI, Cloud.CIP_URI, CommunicationConnector.class.getCanonicalName());

	}
	@Override
	public synchronized void execute() throws Exception {
		Thread.sleep(500L);
		this.connectHeaters();
		this.doLateConnections();
		super.execute();
	}


	
	/**
	 * communicate is the main function of Communicator (should be reprogrammed at each use)
	 * it is modeled after the TCP protocol, in the sense that it expects an "ACK" 
	 * @param address the address of the sender 
	 * @param code the code of the operation 
	 * @param val a useful numerical value used in diffrent cases
	 * @param body the body of the message  
	 */
	@Override
	public String communicate(Address address, String code, double val, String body) throws Exception{
		
		if(code.equals("temp")) {
			for(CommunicationOutboundPort p : heaters) {
				String ACK = p.communicate(address, code, val, body);
				if(!ACK.equals("OK")) {
					throw new Exception("connection problem");
				}
			}
			return "OK";
		}else {
		if(code.equals("log")) {
			return cloudCop.communicate(address, code, val, body);
		}else {
			
		if(code.equals("behaviour")) {
			return cloudCop.communicate(address, code, val, body);
			
		}else {
			return "KO";
	}}}}
	
	/**
	 * the registration function
	 */
	public void connectHeaters() throws Exception {
		Set<String> hts = regop.getHeaters(address, room, location);
		for(String s : hts) {
			if(!this.ht.contains(s)) {
				ht.add(s);
				String uriTempR = CommunicationOutboundPort.generatePortURI();
				CommunicationOutboundPort rp = new CommunicationOutboundPort(uriTempR, this);
				rp.publishPort();
				this.doPortConnection(uriTempR, s, CommunicationConnector.class.getCanonicalName());
				heaters.add(rp);
			}
		}
	}
	
	
}
