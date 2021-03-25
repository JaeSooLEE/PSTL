package pstl.registrator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cps.info.ConnectionInfo;
import cps.registration.RegistrationCI;
import cps.registration.RegistrationInboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;


@OfferedInterfaces(offered = { RegistratorCI.class })
@RequiredInterfaces(required = { RegistratorCI.class })
public class Registration extends AbstractComponent {
	
	public static final String RegIP_URI = "rip-uri";
	private Set<String> heaters = new HashSet<>();
	protected RegistrationInboundPort rip;

	protected Registration() throws Exception {
		super(1, 0);
		this.rip = new RegistrationInboundPort(RegIP_URI, this);
		this.rip.publishPort();
	}

	
	public void registerHeater(String ipURI) throws Exception{
		heaters.add(ipURI);
	}
	
	
	
	
	public ArrayList<String> getHeaters(Coord thermo) throws Exception{
		return heaters;
	}
	
	
}
