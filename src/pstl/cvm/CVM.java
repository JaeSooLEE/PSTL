package pstl.cvm;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import pstl.cloud.Cloud;
import pstl.devices.Heater;
import pstl.devices.Thermometer;
import pstl.registrator.Registrator;
import pstl.simulator.Simulator;
import pstl.util.Address;
import pstl.util.Coord;


public class CVM extends AbstractCVM {

	public CVM() throws Exception {}

	@Override
	public void deploy() throws Exception {
		
		
		AbstractComponent.createComponent(Registrator.class.getCanonicalName(), new Object[] {});
		AbstractComponent.createComponent(Simulator.class.getCanonicalName(), new Object[] {});
		AbstractComponent.createComponent(Cloud.class.getCanonicalName(), new Object[] {});
		
		
		AbstractComponent.createComponent(Heater.class.getCanonicalName(), new Object[] {new Address("H1", false, true), new Coord(3, 3), 1});
		AbstractComponent.createComponent(Heater.class.getCanonicalName(), new Object[] {new Address("H2", false, true), new Coord(3, 8), 2});
		AbstractComponent.createComponent(Heater.class.getCanonicalName(), new Object[] {new Address("H3", false, true), new Coord(8, 5), 3});
		
		AbstractComponent.createComponent(Thermometer.class.getCanonicalName(), new Object[] {new Address("T1", true, false), new Coord(1, 1), 1});
		AbstractComponent.createComponent(Thermometer.class.getCanonicalName(), new Object[] {new Address("T2", true, false), new Coord(1, 7), 2});
		AbstractComponent.createComponent(Thermometer.class.getCanonicalName(), new Object[] {new Address("T3", true, false), new Coord(8, 2), 3});
		AbstractComponent.createComponent(Thermometer.class.getCanonicalName(), new Object[] {new Address("T4", true, false), new Coord(8, 8), 3});

			
		
			
		
		super.deploy();
	}

	public static void main(String[] args) {
		try {
			CVM c = new CVM();
			c.startStandardLifeCycle(20000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
