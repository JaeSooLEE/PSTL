package pstl.util;

public class Coord {
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
	

}
