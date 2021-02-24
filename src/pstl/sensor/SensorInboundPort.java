package pstl.sensor;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class SensorInboundPort extends AbstractInboundPort implements SensorCI {
	private static final long serialVersionUID = 1L;

	public SensorInboundPort(ComponentI owner) throws Exception {
		super(SensorCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	public SensorInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, SensorCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	/*
	 * public SensorInboundPort(Class<? extends OfferedCI> implementedInterface,
	 * ComponentI owner, String pluginURI, String executorServiceURI) throws
	 * Exception { super(implementedInterface, owner, pluginURI,
	 * executorServiceURI); // TODO Auto-generated constructor stub }
	 * 
	 * public SensorInboundPort(String uri, Class<? extends OfferedCI>
	 * implementedInterface, ComponentI owner, String pluginURI, String
	 * executorServiceURI) throws Exception { super(uri, implementedInterface,
	 * owner, pluginURI, executorServiceURI); // TODO Auto-generated constructor
	 * stub }
	 */
	@Override
	public SensorValueI sense() throws Exception {
		return this.getOwner().handleRequest(a -> ((Sensor) a).sense());
	}

}
