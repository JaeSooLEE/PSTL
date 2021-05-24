package pstl.communication;

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
public class CommunicatorH extends AbstractComponent implements CommunicationI{
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

	protected CommunicatorH(Address address, Coord location, int room, String CIP_URI, String PARENT_CIP_URI, String STATE_CIP_URI) {
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
	
	
	protected void initialise() throws Exception {
		this.regop = new RegistrationOutboundPort(ROP_URI, this);
		this.cop = new CommunicationOutboundPort(this.COP_URI, this);
		this.cip = new CommunicationInboundPort(this.CIP_URI, this);
		this.cloudCop = new CommunicationOutboundPort(this.CLOUD_COP_URI, this);
		this.regop.publishPort();
		this.cop.publishPort();
		this.cip.publishPort();
		this.cloudCop.publishPort();
		
		this.doPortConnection(ROP_URI, Registrator.RegIP_URI, RegistrationConnector.class.getCanonicalName());
		this.doPortConnection(CLOUD_COP_URI, Cloud.CIP_URI, CommunicationConnector.class.getCanonicalName());

	}
	protected void doLateConnections() throws Exception {
		this.doPortConnection(COP_URI, this.STATE_CIP_URI, CommunicationConnector.class.getCanonicalName());

	}
	@Override
	public synchronized void execute() throws Exception {
		this.registerHeater(location, PARENT_CIP_URI);
		this.doLateConnections();
		super.execute();
	}


	@Override
	public String communicate(Address address, String code, double val, String body) throws Exception {
		
		if(address.equals(this.address) && code.equals("behaviour")) {
			return cloudCop.communicate(address, code, val, body);
		}
		return cop.communicate(address, code, val, body);
	}
	
	public void registerHeater(Coord c, String ipURI) throws Exception{
		regop.registerHeater(address, room, ipURI);
	}

	
	
}
