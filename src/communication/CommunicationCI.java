package communication;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import pstl.util.Address;

public interface CommunicationCI extends OfferedCI, RequiredCI {
	
	public String communicate(Address address, String code, double val, String body) throws Exception;
	
}
