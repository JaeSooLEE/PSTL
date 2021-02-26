package pstl.state;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class StateOutboundPort extends AbstractOutboundPort implements StateOutboundCI {

	public StateOutboundPort(ComponentI owner) throws Exception {
		super(StateOutboundCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	public StateOutboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri, StateOutboundCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getSensorValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setActuator(double temp) {
		// TODO Auto-generated method stub

	}

}
