package devices;

import communication.CommunicationCI;
import communication.CommunicationI;
import communication.CommunicationInboundPort;
import communication.CommunicatorH;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import pstl.actuator.Actuator;
import pstl.actuator.ActuatorInboundPort;
import pstl.behaviour.BehaviourInboundPort;
import pstl.state.StateH;
import pstl.state.StateInboundPort;
import pstl.util.Address;
import pstl.util.Coord;

@OfferedInterfaces(offered = {CommunicationCI.class })
@RequiredInterfaces(required = {CommunicationCI.class })

public class Heater extends AbstractComponent implements CommunicationI{
	
	
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
	
	protected void initialise() throws Exception {
		
		this.cip = new CommunicationInboundPort(CIP_URI, this);
		this.cip.publishPort();
		
		this.SUB_ACTUATOR_URI = this.createSubcomponent(Actuator.class.getCanonicalName(), new Object[]{this.SUB_AIP_URI}) ;
		this.SUB_COM_URI = this.createSubcomponent(CommunicatorH.class.getCanonicalName(), new Object[]{this.address, this.location, this.room, this.SUB_CIP_URI, this.CIP_URI, this.STATE_CIP_URI});
		this.SUB_STATE_URI = this.createSubcomponent(StateH.class.getCanonicalName(), new Object[]{this.address, this.location, this.room, this.SUB_STIP_URI, this.SUB_BIP_URI, this.SUB_AIP_URI, this.STATE_CIP_URI, this.SUB_CIP_URI}) ;

		
		
		this.createNewExecutorService(POOL_URI, NTHREADS, false) ;
		
		
		
	}
	
	
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		
		try {
			this.findSubcomponentInboundPortFromURI(this.SUB_ACTUATOR_URI,this.SUB_AIP_URI) ;
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


}
