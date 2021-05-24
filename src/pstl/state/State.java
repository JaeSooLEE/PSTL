package pstl.state;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;


@OfferedInterfaces(offered = { StateCI.class })
@RequiredInterfaces(required = {StateCI.class})
public class State extends AbstractComponent implements StateI{
	public final String STIP_URI;
	protected StateI f;
	
	protected StateInboundPort stip;
	
	protected State(String STIP_URI, StateI f) {
		super(1, 0);

		this.STIP_URI=STIP_URI;
		this.f = f;
		
		try {
			this.initialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	protected void initialise() throws Exception {
		this.stip = new StateInboundPort(this.STIP_URI, this);
		this.stip.publishPort();
	}
	
	@Override
	public void newState() throws Exception {
		f.newState();
	}

}
