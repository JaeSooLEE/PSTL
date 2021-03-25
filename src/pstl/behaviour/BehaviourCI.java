package pstl.behaviour;

import java.util.ArrayList;

import fr.sorbonne_u.components.interfaces.RequiredCI;
import pstl.util.Pollution;

public interface BehaviourCI extends RequiredCI {
	public void loop() throws Exception;
	public ArrayList<Pollution> TwoHopPolution() throws Exception;
	public void updateTemp() throws Exception;
	//communicate 
}
