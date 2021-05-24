package pstl.cvm;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import pstl.cloud.Cloud;
import pstl.devices.Heater;
import pstl.devices.Thermometer;
import pstl.registrator.Registrator;
import pstl.simulator.Simulator;
import pstl.util.Address;
import pstl.util.Coord;

public class DistributedCVM extends AbstractDistributedCVM{
	

	public DistributedCVM(String[] args) throws Exception {
		
		super(args);
		
	}

	@Override
	public void instantiateAndPublish() throws Exception {
		
		 String str = AbstractCVM.getThisJVMURI();
	        switch(str)
	        {
	            case "reg":
	            	AbstractComponent.createComponent(Registrator.class.getCanonicalName(), new Object[] {});
	                break;
	            case "sim":
	            	AbstractComponent.createComponent(Simulator.class.getCanonicalName(), new Object[] {});
	                break;
	            case "cloud":
	            	AbstractComponent.createComponent(Cloud.class.getCanonicalName(), new Object[] {});
	                break;
	            case "h":
	        		AbstractComponent.createComponent(Heater.class.getCanonicalName(), new Object[] {new Address("H1", false, true), new Coord(3, 3), 1});
	                break;
	            case "t":
	        		AbstractComponent.createComponent(Thermometer.class.getCanonicalName(), new Object[] {new Address("T1", true, false), new Coord(1, 1), 1});
	                break;
	            default:
	                System.out.println("no match");
	        }
	    
	
		
		super.instantiateAndPublish();
		
	}

	

	public static void main(String[] args) {
		try {
			
			DistributedCVM dc = new DistributedCVM(args);
			
			dc.startStandardLifeCycle(20000L);
			Thread.sleep(3000L);
			
			
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
