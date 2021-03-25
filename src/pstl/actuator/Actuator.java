package pstl.actuator;



import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;

@OfferedInterfaces(offered = { ActuatorCI.class })
public class Actuator extends AbstractComponent {
	public static final String ActIP_URI = "aip-uri";
	protected ActuatorInboundPort aip;
	protected double temperature;
	
	public Actuator() throws Exception{
		super(1,0);
		this.temperature = 20.0;
		this.aip = new ActuatorInboundPort(ActIP_URI, this);
		aip.publishPort();
		
	}
	public void act(double var) {
		this.temperature+=var;
	}

}
