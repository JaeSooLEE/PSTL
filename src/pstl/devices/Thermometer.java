package pstl.devices;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import pstl.behaviour.BehaviourInboundPort;
import pstl.communication.CommunicationCI;
import pstl.communication.CommunicationI;
import pstl.communication.CommunicationInboundPort;
import pstl.communication.CommunicationOutboundPort;
import pstl.communication.CommunicatorT;
import pstl.registrator.RegistrationCI;
import pstl.sensor.Sensor;
import pstl.sensor.SensorInboundPort;
import pstl.state.StateCI;
import pstl.state.StateInboundPort;
import pstl.state.StateT;
import pstl.util.Address;
import pstl.util.Coord;

@OfferedInterfaces(offered = { StateCI.class,  CommunicationCI.class })
@RequiredInterfaces(required = {RegistrationCI.class, StateCI.class,  CommunicationCI.class })

public class Thermometer extends AbstractComponent implements CommunicationI{

	private Address address;
	private Coord location;
	private int room;
	
	
	public final String COP_URI = CommunicationOutboundPort.generatePortURI();
	public final String CIP_URI = CommunicationInboundPort.generatePortURI();
	public final String STATE_CIP_URI = CommunicationInboundPort.generatePortURI();
	
	//subcomponent
	public String SUB_SENSOR_URI;
	public final String SUB_SIP_URI = SensorInboundPort.generatePortURI();

	

	public final String SUB_BIP_URI = BehaviourInboundPort.generatePortURI();

	
	public String SUB_STATE_URI;
	public final String SUB_STIP_URI = StateInboundPort.generatePortURI();
	protected StateInboundPort sub_stip;

	public String SUB_COM_URI;
	public final String SUB_CIP_URI = CommunicationInboundPort.generatePortURI();
	protected CommunicationInboundPort sub_cip;
	
	
	
	private CommunicationInboundPort cip;
	
	protected boolean stop = false;

	// pooling 
	protected static final String	POOL_URI = "computations pool" ;
	protected static final int		NTHREADS = 3 ;
	
	
	protected Thermometer(Address address, Coord location, int room) throws Exception  {
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
		
		this.SUB_SENSOR_URI = this.createSubcomponent(Sensor.class.getCanonicalName(), new Object[]{this.SUB_SIP_URI}) ;
		this.SUB_COM_URI = this.createSubcomponent(CommunicatorT.class.getCanonicalName(), new Object[]{this.address, this.location, this.room, this.SUB_CIP_URI, this.CIP_URI, this.STATE_CIP_URI});
		this.SUB_STATE_URI = this.createSubcomponent(StateT.class.getCanonicalName(), new Object[]{this.address, this.location, this.room, this.SUB_STIP_URI, this.SUB_BIP_URI, this.STATE_CIP_URI, this.SUB_CIP_URI}) ;

		this.createNewExecutorService(POOL_URI, NTHREADS, false) ;
		
		
		
		
		
	}
	
	
	
	
	
	
	
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		
		try {
			
			this.findSubcomponentInboundPortFromURI(this.SUB_SENSOR_URI,this.SUB_SIP_URI) ;
			this.sub_stip =(StateInboundPort)this.findSubcomponentInboundPortFromURI(this.SUB_STATE_URI,this.SUB_STIP_URI) ;
			this.sub_cip =(CommunicationInboundPort)this.findSubcomponentInboundPortFromURI(this.SUB_COM_URI,this.SUB_CIP_URI) ;
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

	
	
	
	public String  communicate(Address address, String code, double val, String body) throws Exception{
		return "KO";
	}
	
	
	
	
	public void nS() throws Exception{
		this.sub_stip.newState();
	
	}
	
	
	
	
	
}
