package cvm;

import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;

public class DistributedCVM extends AbstractDistributedCVM{
	

	public DistributedCVM(String[] args) throws Exception {
		
		super(args);
		
	}

	@Override
	public void instantiateAndPublish() throws Exception {
		
		
	
		
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
