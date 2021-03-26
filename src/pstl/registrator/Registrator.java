package pstl.registrator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.util.Coord;

@OfferedInterfaces(offered = { RegistrationCI.class })
@RequiredInterfaces(required = { RegistrationCI.class })
public class Registrator extends AbstractComponent {

	public static final String RegIP_URI = "rip-uri";
	private Map<String, Coord> heaters = new HashMap<>();
	protected RegistrationInboundPort rip;

	protected Registrator() throws Exception {
		super(1, 0);
		this.rip = new RegistrationInboundPort(RegIP_URI, this);
		this.rip.publishPort();
	}

	public void registerHeater(Coord c, String ipURI) throws Exception {
		heaters.put(ipURI, c);
	}

	public Set<String> getHeaters(Coord thermo) throws Exception {
		if(thermo.x > 7) return getRoomHeaters(3);
		if(thermo.y < 5) return getRoomHeaters(1);
		if(thermo.y > 5) return getRoomHeaters(2);
		return null;
	}

	public Set<String> getRoomHeaters(int n) {
		Set<String> res = new HashSet<>();
		switch (n) {
		case 1:
			for (String s : heaters.keySet()) {
				if (heaters.get(s).x < 8) {
					if (heaters.get(s).y < 5) {
						res.add(s);
					}
				}
			}
			break;
		case 2:
			for (String s : heaters.keySet()) {
				if (heaters.get(s).x < 8) {
					if (heaters.get(s).y > 5) {
						res.add(s);
					}
				}
			}

		case 3:
			for (String s : heaters.keySet()) {
				if (heaters.get(s).x > 7) {
					res.add(s);
				}

			}
		}
		return res;
	}

}
