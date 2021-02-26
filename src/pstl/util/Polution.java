package pstl.util;

public class Polution {
	private double value;
	private String node;
	
	public Polution(double value, String node) {
		this.value = value;
		this.node = node;
	}
	public Polution(String node) {
		this.value = 100;
		this.node = node;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	
	
}
