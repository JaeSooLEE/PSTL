package connecteurs;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import pstl.registrator.RegistrationCI;
import pstl.util.Address;
import pstl.util.Coord;

public class RegistrationConnector extends AbstractConnector implements RegistrationCI {

	@Override
	public void registerHeater(Address address, int room, String ipURI) throws Exception {
		((RegistrationCI)this.offering).registerHeater(address, room, ipURI);
		
	}

	@Override
	public Set<String> getHeaters(Address address, int room, Coord thermo) throws Exception {
		return ((RegistrationCI)this.offering).getHeaters(address, room, thermo);
	}

}
