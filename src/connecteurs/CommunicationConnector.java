package connecteurs;

import communication.CommunicationCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class CommunicationConnector extends AbstractConnector implements CommunicationCI {

	@Override
	public void communicate(int address, double message) throws Exception {
		((CommunicationCI)this.offering).communicate(address, message);
	}

}
