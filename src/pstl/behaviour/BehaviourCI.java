package pstl.behaviour;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import pstl.util.Address;

public interface BehaviourCI extends  OfferedCI, RequiredCI {
	public int update(Address address, int state, double val) throws Exception;
}
