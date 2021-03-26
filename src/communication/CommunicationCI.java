package communication;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface CommunicationCI extends OfferedCI, RequiredCI {
	
	public void communicate(int address, double message) throws Exception;
	
}
