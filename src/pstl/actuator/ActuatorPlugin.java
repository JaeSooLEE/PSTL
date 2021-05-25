package pstl.actuator;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import pstl.connecteurs.ActuatorConnector;
import pstl.simulator.Simulator;
import pstl.util.Coord;

public class ActuatorPlugin extends	AbstractPlugin implements ActuatorI{

	/**
	 * 
	 */
	private static final long serialVersionUID = -508402510074392186L;
	
	public final String AIP_URI;
	public final String AOP_URI = ActuatorOutboundPort.generatePortURI();


	protected ActuatorOutboundPort aop;
	protected ActuatorInboundPort aip;
	
	public ActuatorPlugin(String AIP_URI) {
		super();
		this.AIP_URI=AIP_URI;
	}
	
	
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner);
		this.addRequiredInterface(ActuatorCI.class);
		this.addOfferedInterface(ActuatorCI.class);
		this.aop = new ActuatorOutboundPort(this.AOP_URI, this.getOwner());
		this.aip = new ActuatorInboundPort(this.AIP_URI, this.getOwner());
		this.aop.publishPort();
		this.aip.publishPort();
		
	}
	@Override
	public void initialise() throws Exception {
		Thread.sleep(10L);
		this.getOwner().doPortConnection(AOP_URI, Simulator.AIP_URI, ActuatorConnector.class.getCanonicalName());
		super.initialise();
	}
	@Override
	public void			finalise() throws Exception{
		this.getOwner().doPortDisconnection(this.aop.getPortURI());
	}
	
	@Override
	public void			uninstall() throws Exception
	{
		this.aop.unpublishPort();
		this.aop.destroyPort();
		this.aip.unpublishPort();
		this.aip.destroyPort();
		this.removeRequiredInterface(ActuatorCI.class);
		this.removeOfferedInterface(ActuatorCI.class);
	}

	@Override
	public void act(Coord c, double var) throws Exception {
		aop.act(c, var);
	}

}
