package pstl.devices;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import pstl.actuator.ActuatorI;
import pstl.actuator.ActuatorInboundPort;
import pstl.actuator.ActuatorPlugin;
import pstl.behaviour.BehaviourInboundPort;
import pstl.communication.CommunicationCI;
import pstl.communication.CommunicationI;
import pstl.communication.CommunicationInboundPort;
import pstl.communication.CommunicatorH;
import pstl.state.StateH;
import pstl.state.StateInboundPort;
import pstl.util.Address;
import pstl.util.Coord;

@OfferedInterfaces(offered = {CommunicationCI.class })
@RequiredInterfaces(required = {CommunicationCI.class })
/**
 * Heater is the abstraction of a heater, it acts as a wrapper by creating the sub-components 
 * and making sure that they are linked correctly, as well as act as 
 * a clock (2.5Hz), it is therefore modeled after an electronic component
 *
 */
public class Heater extends AbstractComponent implements CommunicationI, ActuatorI{
	
	
	public final String SUB_AIP_URI = ActuatorInboundPort.generatePortURI();
	public final String CIP_URI = CommunicationInboundPort.generatePortURI();
	public final String STATE_CIP_URI = CommunicationInboundPort.generatePortURI();


	private CommunicationInboundPort cip;
	
	private Address address;
	private Coord location;
	private int room;
	
	
	protected boolean stop = false;

	// pooling 
	protected static final String	POOL_URI = "computations pool" ;
	protected static final int		NTHREADS = 3 ;
	
	
	//subcomponent
	public String SUB_ACTUATOR_URI;
	public final String AIMIP_URI = ActuatorInboundPort.generatePortURI();

	
	
	public final String SUB_BIP_URI = BehaviourInboundPort.generatePortURI();
	
	
		
		
	public String SUB_STATE_URI;
	public final String SUB_STIP_URI = StateInboundPort.generatePortURI();
	protected StateInboundPort sub_stip;
	
	public String SUB_COM_URI;
	public final String SUB_CIP_URI = CommunicationInboundPort.generatePortURI();
	protected CommunicationInboundPort sub_cip;
	
			
	
	//plugin
	protected final static String	APLUGIN = "aplugin";
	ActuatorPlugin aplugin = new ActuatorPlugin(SUB_AIP_URI);;
		
		
	protected Heater(Address address, Coord location, int room) throws Exception  {
		super(1, 0);
		this.address=address;
		this.location=location;
		this.room=room;
		try {
			this.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * the function that creates the different subcomponents and passes the right uris from interconnection
	 * @throws Exception
	 */
	protected void initialise() throws Exception {
		
		this.cip = new CommunicationInboundPort(CIP_URI, this);
		this.cip.publishPort();
		
		
		// actuator plugin
		// although we find the sub-component model much more practical (and conformant to the article's view)
		// we created these plugin's to demostrate that they can be used as well
		aplugin.setPluginURI(APLUGIN); 
		this.installPlugin(aplugin);
				
		//actuator sub-component
		//this.SUB_ACTUATOR_URI = this.createSubcomponent(Actuator.class.getCanonicalName(), new Object[]{this.SUB_AIP_URI}) ;
		//communicator sub-component
		this.SUB_COM_URI = this.createSubcomponent(CommunicatorH.class.getCanonicalName(), new Object[]{this.address, this.location, this.room, this.SUB_CIP_URI, this.CIP_URI, this.STATE_CIP_URI});
		//state sub-component
		this.SUB_STATE_URI = this.createSubcomponent(StateH.class.getCanonicalName(), new Object[]{this.address, this.location, this.room, this.SUB_STIP_URI, this.SUB_BIP_URI, this.SUB_AIP_URI, this.STATE_CIP_URI, this.SUB_CIP_URI}) ;
		
		//the Behaviour sub-component is created and stored on the server on request of State
		
		
		this.createNewExecutorService(POOL_URI, NTHREADS, false) ;
		
		
		
	}
	
	
	
	/**
	 * start is in charge of getting the inboud port handles from the sub-components 
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		
		try {
			//this.findSubcomponentInboundPortFromURI(this.SUB_ACTUATOR_URI,this.SUB_AIP_URI) ;
			this.sub_cip =(CommunicationInboundPort)this.findSubcomponentInboundPortFromURI(this.SUB_COM_URI,this.SUB_CIP_URI) ;
			this.sub_stip =(StateInboundPort)this.findSubcomponentInboundPortFromURI(this.SUB_STATE_URI,this.SUB_STIP_URI) ;
			
			
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
		}

	}

	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		
		this.runTaskOnComponent(
				POOL_URI,
				new AbstractComponent.AbstractTask() {
					
					@Override
					public void run() {
						try {

							while(!stop) {
							//this sleep acts as the 2.5Hz clock for the component state
							Thread.sleep(400L) ;
							nS();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						}});
		
		
		
	}

	@Override
	public synchronized void finalise() throws Exception {
		stop= true;
		Thread.sleep(100L);
		
		this.shutdownExecutorService(POOL_URI);
		this.cip.unpublishPort();
		super.finalise();
		
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.cip.destroyPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}

	
	
	public String communicate(Address address, String code, double val, String body) throws Exception{
		return sub_cip.communicate(address, code, val, body);
	}
	
	

	
	public void nS() throws Exception{
		this.sub_stip.newState();
		
	
	}
	@Override
	public void act(Coord c, double var) throws Exception {
		aplugin.act(c, var);
		
	}


}
