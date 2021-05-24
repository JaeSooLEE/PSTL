package pstl.communication;

import pstl.util.Address;

public interface CommunicationI {
	public String communicate(Address address, String code, double val, String body) throws Exception;
}
