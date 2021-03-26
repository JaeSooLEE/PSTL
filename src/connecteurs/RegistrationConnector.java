package connecteurs;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import pstl.registrator.RegistrationCI;
import pstl.util.Coord;

public class RegistrationConnector extends AbstractConnector implements RegistrationCI {

	@Override
	public void registerHeater(Coord c, String ipURI) throws Exception {
		((RegistrationCI)this.offering).registerHeater(c, ipURI);
		
	}

	@Override
	public Set<String> getHeaters(Coord thermo) throws Exception {
		return ((RegistrationCI)this.offering).getHeaters(thermo);
	}

}
