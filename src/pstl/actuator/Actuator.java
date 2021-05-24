package pstl.actuator;

import connecteurs.ActuatorConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.util.Coord;
import simulator.Simulator;



@OfferedInterfaces(offered = { ActuatorCI.class })
@RequiredInterfaces(required = {ActuatorCI.class})
public class Actuator extends AbstractComponent implements ActuatorI{
	public final String AIP_URI;
	public final String AOP_URI = ActuatorOutboundPort.generatePortURI();


	protected ActuatorOutboundPort aop;
	protected ActuatorInboundPort aip;
	
	protected Actuator(String AIP_URI) {
		super(1, 0);
		
		this.AIP_URI=AIP_URI;
		
		
		try {
			this.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	protected void initialise() throws Exception {
		this.aop = new ActuatorOutboundPort(this.AOP_URI, this);
		this.aip = new ActuatorInboundPort(this.AIP_URI, this);
		this.aop.publishPort();
		this.aip.publishPort();
		this.doPortConnection(AOP_URI, Simulator.AIP_URI, ActuatorConnector.class.getCanonicalName());
	}
	
	@Override
	public void act(Coord c, double var) throws Exception {
		
		aop.act(c, var);
	}
}
