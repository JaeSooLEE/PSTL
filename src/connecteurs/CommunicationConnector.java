package connecteurs;

import communication.CommunicationCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import pstl.util.Address;

public class CommunicationConnector extends AbstractConnector implements CommunicationCI {

	@Override
	public String communicate(Address address, String code, double val, String body) throws Exception {
		return ((CommunicationCI)this.offering).communicate(address, code, val, body);
	}

}
