package pstl.registrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cps.info.ConnectionInfo;
import cps.registration.RegistrationCI;
import cps.registration.RegistrationInboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import pstl.util.Coord;


@OfferedInterfaces(offered = { RegistratorCI.class })
@RequiredInterfaces(required = { RegistratorCI.class })
public class Registration extends AbstractComponent {
	
	public static final String RegIP_URI = "rip-uri";
	private Map<String, Coord> heaters = new HashMap<>();
	protected RegistrationInboundPort rip;

	protected Registration() throws Exception {
		super(1, 0);
		this.rip = new RegistrationInboundPort(RegIP_URI, this);
		this.rip.publishPort();
	}

	
	public void registerHeater(String ipURI) throws Exception{
		heaters.add(ipURI);
	}
	
	
	
	
	public Set<String> getHeaters(Coord thermo) throws Exception{
		return heaters;
	}
	
	public Set<String> getRoomHeaters(int n){
		Set<String> res = new HashSet<>();
		switch(n) {
		case 1:
			for(String s : heaters) {
				if()
			}
		}
			
	}
	
	
}
