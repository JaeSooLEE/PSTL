package pstl.state;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface StateCI extends RequiredCI, OfferedCI {
	public void neighState(String address, double value) throws Exception;
	public int newState(int state) throws Exception;
	
}
