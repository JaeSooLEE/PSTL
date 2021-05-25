package pstl.actuator;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.connecteurs.ActuatorConnector;
import pstl.simulator.Simulator;
import pstl.util.Coord;


@OfferedInterfaces(offered = { ActuatorCI.class })
@RequiredInterfaces(required = {ActuatorCI.class})
/**
 * the class Actuator is a reusable sub-component in charge of 
 * the Actuating function, at creation it is given the inbound 
 * port that is used later to contact it  
 *
 */
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
	/**
	 * Initialise is in charge of creating and publishing the ports
	 * the inbound port is created with the provided URI
	 * @throws Exception
	 */
	protected void initialise() throws Exception {
		this.aop = new ActuatorOutboundPort(this.AOP_URI, this);
		this.aip = new ActuatorInboundPort(this.AIP_URI, this);
		this.aop.publishPort();
		this.aip.publishPort();
		this.doPortConnection(AOP_URI, Simulator.AIP_URI, ActuatorConnector.class.getCanonicalName());
	}
	
	/**
	 * act is the main function of the Actuator (should be reprogrammed at each use)
	 * @param c the coordinates of the effected location
	 * @param var the intensity of the effect
	 */
	@Override
	public void act(Coord c, double var) throws Exception {
		
		aop.act(c, var);
	}
}
