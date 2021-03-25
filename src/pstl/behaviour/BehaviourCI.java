package pstl.behaviour;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface BehaviourCI extends  OfferedCI, RequiredCI {
	public int update(int state) throws Exception;
}
