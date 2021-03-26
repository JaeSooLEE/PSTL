package pstl.registrator;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import pstl.util.Coord;

public class RegistrationOutboundPort extends AbstractOutboundPort implements RegistrationCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8204727238838107449L;

	public RegistrationOutboundPort(ComponentI owner) throws Exception {
		super(RegistrationCI.class, owner);

	}

	public RegistrationOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, RegistrationCI.class, owner);
	}
	
	
	@Override
	public void registerHeater(Coord c, String ipURI) throws Exception {
		((RegistrationCI)this.getConnector()).registerHeater(c, ipURI);


	}

	@Override
	public Set<String> getHeaters(Coord thermo) throws Exception {
		return ((RegistrationCI)this.getConnector()).getHeaters(thermo);
	}

}
