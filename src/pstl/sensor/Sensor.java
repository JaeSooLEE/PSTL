package pstl.sensor;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

@OfferedInterfaces(offered = { SensorCI.class })
public class Sensor extends AbstractComponent {
	public static final String SensorIP_URI = "sip-uri";
	protected SensorInboundPort sip;
	protected SensorStateI currentState;
	protected SensorTransitionFI transitionRelation;

	protected Sensor() throws Exception{
		super(1,0);
		this.sip = new SensorInboundPort(SensorIP_URI, this);
		this.sip.publishPort();
	}

	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.sip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}
	protected Sensor(String reflectionInboundPortURI, int nbThreads, int nbSchedulableThreads) {
		super(reflectionInboundPortURI, nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}
	
	public SensorValueI sense() {
		SensorStateValueI stateValue =
				this.transitionRelation.transition(this.currentState);
				this.currentState = stateValue.getState();
				return stateValue.getSensorValue();
	}

}
