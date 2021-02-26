package pstl.actuator;



import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;

@RequiredInterfaces(required = { ActuatorCI.class })
public class Actuator extends AbstractComponent {
	public static final String ActOP_URI = "aop-uri";
	protected ActuatorOutboundPort aop;
	protected double temperature;
	
	public Actuator() throws Exception{
		super(1,0);
		this.temperature = 20.0;
		this.aop = new ActuatorOutboundPort(ActOP_URI, this);
		aop.publishPort();
		
	}
	public void act(double var) {
		this.temperature+=var;
	}

}
