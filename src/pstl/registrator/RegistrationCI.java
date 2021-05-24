package pstl.registrator;

import java.util.Set;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import pstl.util.Address;
import pstl.util.Coord;

public interface RegistrationCI extends RequiredCI, OfferedCI {
	public void registerHeater(Address address, int room, String ipURI) throws Exception;
	public Set<String> getHeaters(Address address, int room, Coord thermo) throws Exception;
	
	
	
}
