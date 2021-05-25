package pstl.sensor;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.connecteurs.SensorConnector;
import pstl.simulator.Simulator;
import pstl.util.Coord;

@OfferedInterfaces(offered = { SensorCI.class })
@RequiredInterfaces(required = {SensorCI.class})
/**
 * the class Sensor is a reusable sub-component in charge of 
 * the Sensing function, at creation it is given the inbound 
 * port that is used later to contact it  
 *
 */
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
	/**
	 * Initialise is in charge of creating and publishing the ports
	 * the inbound port is created with the provided URI
	 * @throws Exception
	 */
	protected void initialise() throws Exception {
		this.sop = new SensorOutboundPort(this.SOP_URI, this);
		this.sip = new SensorInboundPort(this.SIP_URI, this);
		this.sop.publishPort();
		this.sip.publishPort();
		this.doPortConnection(SOP_URI, Simulator.SIP_URI, SensorConnector.class.getCanonicalName());
	}
	
	
	/**
	 * sense is the main function of Sensor 
	 * sense gets the measured value from the c coordinate 
	 * @param c the coordinate of the measurement 
	 * @return the value measured
	 */
	@Override
	public double sense(Coord c) throws Exception {
		
		return sop.sense(c);
	}

}
