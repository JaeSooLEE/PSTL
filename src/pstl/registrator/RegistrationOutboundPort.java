package pstl.registrator;

import java.util.ArrayList;
import java.util.Set;

import cps.registration.RegistrationCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import pstl.util.Coord;

public class RegistrationOutboundPort extends AbstractOutboundPort implements RegistratorCI {

	public RegistrationOutboundPort(ComponentI owner) throws Exception {
		super(RegistratorCI.class, owner);

	}

	public RegistrationOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, RegistratorCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void registerHeater(Coord c, String ipURI) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> getHeaters(Coord thermo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
