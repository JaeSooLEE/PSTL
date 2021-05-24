package pstl.connecteurs;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import pstl.behaviour.BehaviourCI;
import pstl.util.Address;

public class BehaviourConnector extends AbstractConnector implements BehaviourCI{

	@Override
	public int update(Address address, int state, double val) throws Exception {
		return ((BehaviourCI)this.offering).update(address, state, val);
	}

}
