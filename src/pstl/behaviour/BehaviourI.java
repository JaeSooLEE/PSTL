package pstl.behaviour;

import pstl.util.Address;

public interface BehaviourI {
	public int update(Address address, int state, double val) throws Exception;
}
