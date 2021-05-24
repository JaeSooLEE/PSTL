package pstl.util;

import java.io.Serializable;

public class Address implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1154025854543765986L;
	private String address;
	private boolean isThermomerer;
	private boolean isHeater;
	
	public Address(String address, boolean isThermometer, boolean isHeater) {
		this.address = address;
		this.isThermomerer = isThermometer;
		this.isHeater = isHeater;
	}
	@Override
	public String toString() {
		return this.address;
	}
	public String getAddress() {
		return this.address;
	}
	
	public boolean isThermomerer() {
		return isThermomerer;
	}
	public boolean isHeater() {
		return isHeater;
	}
	@Override
	public int hashCode() {
		return this.address.hashCode();
	}
	
	@Override 
	public boolean equals(Object o) {
		
	    if (this == o) return true;
	    if (o == null) return false;
	    if (getClass() != o.getClass()) return false;
	    Address e = (Address) o;
	    
	    return this.getAddress().equals(e.getAddress());
		
	}
	
	
}
