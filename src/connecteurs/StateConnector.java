package connecteurs;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import pstl.state.StateCI;

public class StateConnector extends AbstractConnector implements StateCI{

	@Override
	public void neighState(String address, double value) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newState() throws Exception {
		((StateCI)this.offering).newState();
		
	}

}
