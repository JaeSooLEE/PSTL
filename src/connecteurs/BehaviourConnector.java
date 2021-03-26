package connecteurs;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import pstl.behaviour.BehaviourCI;

public class BehaviourConnector extends AbstractConnector implements BehaviourCI{

	@Override
	public int update(int state) throws Exception {
		return ((BehaviourCI)this.offering).update(state);
	}

}
