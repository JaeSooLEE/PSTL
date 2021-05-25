package pstl.sensor;


import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import pstl.connecteurs.SensorConnector;
import pstl.simulator.Simulator;
import pstl.util.Coord;



public class SensorPlugin extends	AbstractPlugin implements SensorI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2721363139041477305L;

	
	public final String SIP_URI;
	public final String SOP_URI = SensorOutboundPort.generatePortURI();
	
	
	protected SensorOutboundPort sop;
	protected SensorInboundPort sip;
	
	public SensorPlugin(String SIP_URI) {
		super();
		this.SIP_URI=SIP_URI;
	}
	

	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner);
		
		this.addRequiredInterface(SensorCI.class);
		this.addOfferedInterface(SensorCI.class);
		this.sop = new SensorOutboundPort(this.SOP_URI, this.getOwner());
		this.sip = new SensorInboundPort(this.SIP_URI, this.getOwner());
		this.sop.publishPort();
		this.sip.publishPort();
		
	}
	@Override
	public void initialise() throws Exception {
		Thread.sleep(10L);
		this.getOwner().doPortConnection(SOP_URI, Simulator.SIP_URI, SensorConnector.class.getCanonicalName());
		super.initialise();
	}
	@Override
	public void			finalise() throws Exception{
		this.getOwner().doPortDisconnection(this.sop.getPortURI());
	}
	
	@Override
	public void			uninstall() throws Exception
	{
		this.sop.unpublishPort();
		this.sop.destroyPort();
		this.sip.unpublishPort();
		this.sip.destroyPort();
		this.removeRequiredInterface(SensorCI.class);
		this.removeOfferedInterface(SensorCI.class);
	}
	
	
	
	@Override
	public double sense(Coord c) throws Exception {
		return sop.sense(c);
	}

}
