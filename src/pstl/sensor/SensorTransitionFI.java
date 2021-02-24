package pstl.sensor;

@FunctionalInterface
public interface SensorTransitionFI {
	public SensorStateValueI transition(
			SensorStateI sigma
	);
}
