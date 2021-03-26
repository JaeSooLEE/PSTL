package cvm;

import connecteurs.RegistrationConnector;
import connecteurs.SensorConnector;
import devices.Thermometer;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import pstl.registrator.Registrator;
import simulator.Simulator;

public class CVM extends AbstractCVM {

	public CVM() throws Exception {}

	@Override
	public void deploy() throws Exception {
		String[] therm = new String[1000];
		String[] ter = new String[1000];
		String[] ap = new String[1000];
		
		
		AbstractComponent.createComponent(Registrator.class.getCanonicalName(), new Object[] {});
		AbstractComponent.createComponent(Simulator.class.getCanonicalName(), new Object[] {});
		
		for(int i=0; i<10;i++) {

			therm[i] = AbstractComponent.createComponent(Thermometer.class.getCanonicalName(), new Object[] {});
			this.doPortConnection(therm[i],Thermometer.RegOP_URI , Registrator.RegIP_URI, RegistrationConnector.class.getCanonicalName());
			this.doPortConnection(therm[i],Thermometer.RegOP_URI , Registrator.RegIP_URI, SensorConnector.class.getCanonicalName());
		} 
		
		
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
