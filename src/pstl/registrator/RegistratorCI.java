package pstl.registrator;

import java.util.ArrayList;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface RegistratorCI extends RequiredCI, OfferedCI {
	public void registerHeater(String address, String ipURI) throws Exception;
	public ArrayList<String> getHeaters() throws Exception;
	
	
	
}
