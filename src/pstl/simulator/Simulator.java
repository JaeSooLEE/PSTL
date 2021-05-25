package pstl.simulator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import pstl.actuator.ActuatorCI;
import pstl.actuator.ActuatorI;
import pstl.actuator.ActuatorInboundPort;
import pstl.sensor.SensorCI;
import pstl.sensor.SensorI;
import pstl.sensor.SensorInboundPort;
import pstl.util.Coord;

@OfferedInterfaces(offered = { SensorCI.class, ActuatorCI.class })

/**
 * the class Simulator represents the house where the simulation takes place 
 * as well as the physics of heat propagation, it also provides the physical end of the Sensing 
 * and Actuating functions
 *
 */
public class Simulator extends AbstractComponent implements SensorI, ActuatorI {
	
	public static final String SIP_URI =     "sim_sip";
	public static final String AIP_URI = 	"sim_aip";
	
	private SensorInboundPort sip;
	private ActuatorInboundPort aip;
	
	
	int size = 10;
	// heat propagation coefficient 
	double tauxPropagation = 0.1;
	// heat loss coefficient
	double tauxDecipation = 0.002;
	// heating coefficient
	double tauxChaufage = 4;
	
	//the map of the house
	double[][] map = new double[size][size]; 
	
	// this value is used for thermo-isolating walls
	double wall = Double.MIN_VALUE;
	Map<Coord, Double> heaterEffect = new HashMap<Coord, Double>();
	
	
	// pooling 
	protected static final String	POOL_URI = "computations pool" ;
	protected static final int		NTHREADS = 3 ;
	
	protected Simulator() throws Exception {
		super(1, 0);
		initMap();
		
		this.sip = new SensorInboundPort(SIP_URI , this);
		this.aip = new ActuatorInboundPort(AIP_URI , this);
		
		try {
			this.sip.publishPort();
			this.aip.publishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.toggleLogging();
		this.toggleTracing();
		
		try {
			this.createNewExecutorService(POOL_URI, NTHREADS, false) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		
		
		
		this.runTaskOnComponent(
				POOL_URI,
				new AbstractComponent.AbstractTask() {
					
					@Override
					public void run() {
						try {
							int i=0;
							while(i<10) {
							Thread.sleep(100L) ;
							propagate();
							heat();
							}
						
						} catch (Exception e) {
							e.printStackTrace();
						}
						}});
		

		this.runTaskOnComponent(
				POOL_URI,
				new AbstractComponent.AbstractTask() {
					
					@Override
					public void run() {
						try {
							Thread.sleep(4000L) ;
							// creating the event of an open door
							map[1][1] = 1;
							map[1][2] = 1;
							
						
						} catch (Exception e) {
							e.printStackTrace();
						}
						}});
		
		
		
		
	}





	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.sip.unpublishPort();
			this.aip.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		super.shutdown();
	}





	@Override
	public synchronized void finalise() throws Exception {
		super.finalise();
		this.printMap();
	}




	/**
	 * the physical end of the sensor, return the temperature at the specified coordinate
	 * @param c given location
	 * @return temperature value
	 */
	public double sense(Coord c) throws Exception{
		return map[c.x][c.y];
	}
	
	/**
	 * the physical end of the heater, adds heat at the specified location and with the specified intensity
	 * @param c given location
	 * @param var given intensity
	 */
	public void act(Coord c, double var) throws Exception{
		heaterEffect.put(c, var);
	}
	
	/**
	 * the function that heats the marked locations with a given intensity multiplied by a coefficient 
	 */
	private void heat() {
		for(Entry<Coord, Double> e : heaterEffect.entrySet()) {
			map[e.getKey().x][e.getKey().y] += tauxChaufage * e.getValue(); 
			
		}
	}
	
	
	/**
	 * propagate acts as the physics of the house 
	 * if one cell of the matrix is hotter (or colder) then this others, there will be a transfer
	 * of heat with the neighbors of that cell, the amount is determined by the transfer coefficient 
	 * 
	 * on each run of the function, the cells will lose a certain amount of heat to the exterior determined 
	 * by the dissipation coefficient 
	 */
	private void propagate() {
		for(int i =0; i < size; i++) {
			for(int j =0; j < size; j++) {
				
		
	       if(map[i][j] == wall) {
               continue;
           } else {
               double diff = 0;
               if(i != 0 && map[i-1][j] != wall) {
                   diff = map[i][j] - map[i-1][j];
                   map[i][j] -= tauxPropagation*diff+ tauxDecipation*map[i][j];
                   map[i-1][j] += tauxPropagation*diff;
               }
               if(i != size-1 && map[i+1][j] != wall) {
                   diff = map[i][j] - map[i+1][j];
                   map[i][j] -= tauxPropagation*diff+ tauxDecipation*map[i][j];
                   map[i+1][j] += tauxPropagation*diff;
               }
               if(j != 0 && map[i][j-1] != wall) {
                   diff = map[i][j] - map[i][j-1];
                   map[i][j] -= tauxPropagation*diff+ tauxDecipation*map[i][j];
                   map[i][j-1] += tauxPropagation*diff;
               }
               if(j != size-1 && map[i][j+1] != wall) {
                   diff = map[i][j] - map[i][j+1];
                   map[i][j] -= tauxPropagation*diff+ tauxDecipation*map[i][j];
                   map[i][j+1] += tauxPropagation*diff;
               }
                   
			}
	      }
		}
	}
	
	public void printMap() {
		System.out.print("\n-------------------------\n");
		for(int i =0; i < size; i++) {
			for(int j =0; j < size; j++) {
				if(map[i][j] == wall) {
					System.out.print("xxx  ");
				}
				else {
					System.out.print(round(map[i][j],2)+"  ");
				}
			}
			System.out.print("\n");
		}
		System.out.print("\n-------------------------\n\n");
		
		System.out.println("num heaters in simulation: "+ heaterEffect.size());
		for(Entry<Coord, Double> e : heaterEffect.entrySet()) {
			 System.out.println(e.getKey()+"   "+e.getValue());
			
		}
	}
	/**
	 * giving the map of the house and the start temperature 
	 */
	private void initMap() {
		for(int i =0; i < size; i++) {
			for(int j =0; j < size; j++) {
				map[i][j] = 17;
			}
		}
		for(int i =0; i < 8; i++) {
			map[i][5] = wall;
		}
		map[7][4]=wall;
		map[7][3]=wall;
		map[7][6]=wall;
		map[7][7]=wall;
		this.printMap();
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	

}
