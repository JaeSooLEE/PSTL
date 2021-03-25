package pstl.registrator;

import java.util.ArrayList;
import java.util.Set;

import fr.sorbonne_u.components.ports.AbstractInboundPort;
import pstl.util.Coord;

public class RegistrationInboundPort extends AbstractInboundPort implements RegistratorCI {

	@Override
	public void registerHeater(String ipURI) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> getHeaters(Coord thermo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
