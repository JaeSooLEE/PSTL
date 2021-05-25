package pstl.registrator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.util.Address;
import pstl.util.Coord;

@OfferedInterfaces(offered = { RegistrationCI.class })
@RequiredInterfaces(required = { RegistrationCI.class })

/**
 * Registrator is in charge of memorizing the heaters and passing the list of heaters
 * in the same room to the thermometers
 *
 */
public class Registrator extends AbstractComponent {

	public static final String RegIP_URI = "rip-uri";
	private Map<Integer, Map<Address, String>> heaters = new HashMap<Integer, Map<Address, String>>();
	protected RegistrationInboundPort rip;

	protected Registrator() throws Exception {
		super(1, 0);
		this.rip = new RegistrationInboundPort(RegIP_URI, this);
		this.rip.publishPort();
	}

	/**
	 * this function registers heaters in the map "heaters" 
	 */
	public void registerHeater(Address address, int room, String ipURI) throws Exception {
		if(heaters.containsKey(room)) {
			heaters.get(room).put(address, ipURI);
		}else {
			Map<Address, String> tmp = new HashMap<Address, String>();
			tmp.put(address, ipURI);
			heaters.put(room, tmp);
		}
	}
	/**
	 * this function is called by thermometer to get the list of heaters in the same room
	 */
	public Set<String> getHeaters(Address address, int room, Coord thermo) throws Exception {
		Set<String> res = new HashSet<>();
		
		for(Entry<Address, String> e: heaters.get(room).entrySet()) {
			res.add(e.getValue());
		}
		return res;
		
	}

	
}
