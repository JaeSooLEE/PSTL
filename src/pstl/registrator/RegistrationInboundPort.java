package pstl.registrator;

import java.util.ArrayList;

import fr.sorbonne_u.components.ports.AbstractInboundPort;
import pstl.util.Coord;

public class RegistrationInboundPort extends AbstractInboundPort implements RegistratorCI {

	@Override
	public void registerHeater(String ipURI) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> getHeaters(Coord thermo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
