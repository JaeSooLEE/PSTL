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

	public void registerHeater(Coord c, String ipURI) throws Exception {
		heaters.put(ipURI, c);
	}

	public Set<String> getHeaters(Coord thermo) throws Exception {
		if(thermo.y > 7) return getRoomHeaters(3);
		if(thermo.x < 5) return getRoomHeaters(1);
		if(thermo.x > 5) return getRoomHeaters(2);
		return null;
	}

	public Set<String> getRoomHeaters(int n) {
		Set<String> res = new HashSet<>();
		switch (n) {
		case 1:
			for (String s : heaters.keySet()) {
				if (heaters.get(s).y < 8) {
					if (heaters.get(s).x < 5) {
						res.add(s);
					}
				}
			}
			break;
		case 2:
			for (String s : heaters.keySet()) {
				if (heaters.get(s).y < 8) {
					if (heaters.get(s).x > 5) {
						res.add(s);
					}
				}
			}

		case 3:
			for (String s : heaters.keySet()) {
				if (heaters.get(s).y > 7) {
					res.add(s);
				}

			}
		}
	}

}
