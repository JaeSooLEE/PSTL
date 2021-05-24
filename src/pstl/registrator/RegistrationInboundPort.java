package pstl.registrator;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import pstl.util.Address;
import pstl.util.Coord;

public class RegistrationInboundPort extends AbstractInboundPort implements RegistrationCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3934975392496197191L;

	public RegistrationInboundPort(ComponentI owner) throws Exception {
		super(RegistrationCI.class, owner);

	}

	public RegistrationInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, RegistrationCI.class, owner);
	}
	
	
	@Override
	public void registerHeater(Address address, int room, String ipURI) throws Exception {
		this.getOwner().runTask(
				s-> {
					try {
						((Registrator) s).registerHeater(address, room, ipURI);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				});

	}
	@Override
	public Set<String> getHeaters(Address address, int room, Coord thermo) throws Exception {
		return this.getOwner().handleRequest(
				s -> ((Registrator) s).getHeaters(address,room, thermo));
	}
	

}
