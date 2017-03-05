package camus.service.geo;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;



/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class Size2f {
	@PlanetField(ordinal=0) public float width;
	@PlanetField(ordinal=1) public float height;

	public Size2f() { }
	
	public Size2f(float width, float height) {
		this.height = height;
		this.width = width;
	}
	
	@Override
	public String toString() {
		return String.format("%.2fx%.2f", this.width, this.height);
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		else if ( obj == null || getClass() != obj.getClass() ) {
			return false;
		}
		
		Size2f other = (Size2f)obj;
		return this.height == other.height && this.width == other.width;
	}
	
	@Override
	public int hashCode() {
    	int hash = 17;

    	hash = (31 * hash) + new Float(this.width).hashCode();
    	hash = (31 * hash) + new Float(this.height).hashCode();

    	return hash;
	}
	
	public static Size2f parseSize2f(String str) {
		Size2f size = new Size2f();
		
		int index = str.toUpperCase().indexOf('X');
		size.width = Float.parseFloat(str.substring(0, index).trim());
		size.height = Float.parseFloat(str.substring(index+1).trim());
		
		return size;
	}
}
