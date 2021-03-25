package pstl.registrator;

import java.util.ArrayList;
import java.util.Set;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import pstl.util.Coord;

public interface RegistratorCI extends RequiredCI, OfferedCI {
	public void registerHeater(String ipURI) throws Exception;
	public Set<String> getHeaters(Coord thermo) throws Exception;
	
	
	
}
