package pstl.sensor;

import connecteurs.SensorConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.util.Coord;
import simulator.Simulator;

@OfferedInterfaces(offered = { SensorCI.class })
@RequiredInterfaces(required = {SensorCI.class})
public class Sensor extends AbstractComponent implements SensorI{
	public final String SIP_URI;
	public final String SOP_URI = SensorOutboundPort.generatePortURI();
	
	
	protected SensorOutboundPort sop;
	protected SensorInboundPort sip;
	
	protected Sensor(String SIP_URI) {
		super(1, 0);
		
		this.SIP_URI=SIP_URI;
		
		
		try {
			this.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	protected void initialise() throws Exception {
		this.sop = new SensorOutboundPort(this.SOP_URI, this);
		this.sip = new SensorInboundPort(this.SIP_URI, this);
		this.sop.publishPort();
		this.sip.publishPort();
		this.doPortConnection(SOP_URI, Simulator.SIP_URI, SensorConnector.class.getCanonicalName());
	}
	
	@Override
	public double sense(Coord c) throws Exception {
		
		return sop.sense(c);
	}

}
