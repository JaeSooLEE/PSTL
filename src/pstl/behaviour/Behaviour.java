package pstl.behaviour;

import java.util.ArrayList;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import pstl.util.LoopTime;
import pstl.util.Pollution;

@RequiredInterfaces(required = { BehaviourCI.class })
public class Behaviour extends AbstractComponent{
	public static final String BehOP_URI = "bop-uri";
	protected BehaviourOutboundPort bop;

	
	public Behaviour() throws Exception{
		super(1,0);
		this.bop = new BehaviourOutboundPort(BehOP_URI, this);
		bop.publishPort();
		
	}
		
	public void loop() {
		 long now;
		    long updateTime;
		    long wait;

		    final int TARGET_FPS = 60; // to be modified pbbly too high
		    final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		    LoopTime t = new LoopTime();
		    t.start(100);
		    while (t.checkTime()) {
		        now = System.nanoTime();

		        // treatment and orders to state
		        // yeah and prbbly more functions to come  

		        updateTime = System.nanoTime() - now;
		        wait = (OPTIMAL_TIME - updateTime) / 1000000;

		        try {
		            Thread.sleep(wait);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }


		    }
	}

	public ArrayList<Pollution> TwoHopPolution() throws Exception {
		return null;
	}

	public void updateTemp() throws Exception {
		
	}
}
