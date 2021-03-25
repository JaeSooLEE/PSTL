package simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import pstl.actuator.ActuatorCI;
import pstl.sensor.SensorCI;
import pstl.util.Coord;

@OfferedInterfaces(offered = { SensorCI.class, ActuatorCI.class })
public class Simulator extends AbstractComponent {
	
	int size = 10;
	double tauxPropagation = 0.001;
	double tauxDecipation = 0.00001;
	double tauxChaufage = 0.001;
	
	double[][] map = new double[size][size]; 
	
	Map<Coord, Double> heaterEffect = new HashMap<Coord, Double>();
	
	
	protected Simulator() throws Exception {
		super(1, 0);
		initMap();
		
		
		
		/*
		this.rip = new RegistrationInboundPort(RegIP_URI, this);
		this.rip.publishPort();
		*/
	}
	
	
	
	public double sense(Coord c) throws Exception{
		return map[c.x][c.y];
	}
	
	
	public void act(Coord c, double var) throws Exception{
		heaterEffect.putIfAbsent(c, var);
	}
	
	private void heat() {
		for(Entry<Coord, Double> e : heaterEffect.entrySet()) {
			map[e.getKey().x][e.getKey().y] += tauxChaufage * e.getValue(); 
			
		}
	}
	
	
	private void propagate() {
		for(int i =0; i < size; i++) {
			for(int j =0; j < size; j++) {
				map[i][j] = 17;
		
	       if(map[i][j] == -1) {
               continue;
           } else {
               double diff = 0;
               if(i != 0 && map[i-1][j] != -1) {
                   diff = map[i][j] - map[i-1][j];
                   map[i][j] -= tauxPropagation*diff+ tauxDecipation*map[i][j];
                   map[i-1][j] += tauxPropagation*diff;
               }
               if(i != size-1 && map[i+1][j] != -1) {
                   diff = map[i][j] - map[i+1][j];
                   map[i][j] -= tauxPropagation*diff+ tauxDecipation*map[i][j];
                   map[i+1][j] += tauxPropagation*diff;
               }
               if(j != 0 && map[i][j-1] != -1) {
                   diff = map[i][j] - map[i][j-1];
                   map[i][j] -= tauxPropagation*diff+ tauxDecipation*map[i][j];
                   map[i][j-1] += tauxPropagation*diff;
               }
               if(j != size-1 && map[i][j+1] != -1) {
                   diff = map[i][j] - map[i][j+1];
                   map[i][j] -= tauxPropagation*diff+ tauxDecipation*map[i][j];
                   map[i][j+1] += tauxPropagation*diff;
               }
                   
			}
	      }
		}
	}
	
	
	private void initMap() {
		for(int i =0; i < size; i++) {
			for(int j =0; j < size; j++) {
				map[i][j] = 17;
			}
		}
		for(int i =0; i < 8; i++) {
			map[i][5] = -1;
		}
	}
	
	
	

}
