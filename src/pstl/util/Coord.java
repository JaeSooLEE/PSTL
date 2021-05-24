package pstl.util;

import java.io.Serializable;

public class Coord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 850784721322298600L;
	public int x; 
	public int y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override 
	public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Coord)) {
            return false;
        }
    
        Coord c = (Coord) o;
          
        return Double.compare(x, c.x) == 0
                && Double.compare(y, c.y) == 0;
    }
	@Override
	public int hashCode() {
		String s = ""+x+""+y;
		return s.hashCode();
	}
	
	@Override
	public String toString() {
		return "Coord[" + this.x + ", " + this.y+"]";
	}

}
