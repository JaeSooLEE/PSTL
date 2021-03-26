package cvm;

import connecteurs.ActuatorConnector;
import connecteurs.RegistrationConnector;
import connecteurs.SensorConnector;
import devices.Heater;
import devices.Thermometer;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import pstl.registrator.Registrator;
import pstl.util.Coord;
import simulator.Simulator;


public class CVM extends AbstractCVM {

	public CVM() throws Exception {}

	@Override
	public void deploy() throws Exception {
		String[] therm = new String[1000];
		String[] heat = new String[1000];
		
		
		
		AbstractComponent.createComponent(Registrator.class.getCanonicalName(), new Object[] {});
		AbstractComponent.createComponent(Simulator.class.getCanonicalName(), new Object[] {});
		
		for(int i=0; i<8;i++) {

			therm[i] = AbstractComponent.createComponent(Thermometer.class.getCanonicalName(), new Object[] {new Coord((int)Math.random()*10,(int)Math.random()*10)});
			this.doPortConnection(therm[i],Thermometer.RegOP_URI , Registrator.RegIP_URI, RegistrationConnector.class.getCanonicalName());
			this.doPortConnection(therm[i],Thermometer.SOP_URI , Simulator.SIP_URI, SensorConnector.class.getCanonicalName());
		} 
		
		for(int i=0; i<2;i++) {

			heat[i] = AbstractComponent.createComponent(Heater.class.getCanonicalName(), new Object[] {new Coord((int)Math.random()*10,(int)Math.random()*10)});
			this.doPortConnection(heat[i],Heater.RegOP_URI , Registrator.RegIP_URI, RegistrationConnector.class.getCanonicalName());
			this.doPortConnection(heat[i],Heater.AOP_URI , Simulator.AIP_URI, ActuatorConnector.class.getCanonicalName());
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
