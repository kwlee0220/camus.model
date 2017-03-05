package camus.service.image;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;


/**
 * <code>Color</code>는 RGB를 이용한 색을 정의한 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class Color {
	@PlanetField(ordinal=0) public int r;
	@PlanetField(ordinal=1) public int g;
	@PlanetField(ordinal=2) public int b;
	
	public static final Color RED = new Color(java.awt.Color.RED);
	public static final Color BLUE = new Color(java.awt.Color.BLUE);
	public static final Color GREEN = new Color(java.awt.Color.GREEN);
	public static final Color YELLOW = new Color(java.awt.Color.YELLOW);
	public static final Color CYAN = new Color(java.awt.Color.CYAN);
	public static final Color ORANGE = new Color(java.awt.Color.ORANGE);
	public static final Color PINK = new Color(java.awt.Color.PINK);
	public static final Color MAGENTA = new Color(java.awt.Color.MAGENTA);
	public static final Color WHITE = new Color(java.awt.Color.WHITE);
	public static final Color BLACK = new Color(java.awt.Color.BLACK);
	
	public Color() { }	// just for planet
	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public Color(java.awt.Color color) {
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
	}
	
	public Color duplicate() {
		return new Color(this.r, this.g, this.b);
	}
	
	public java.awt.Color toAwtColor() {
		return new java.awt.Color(this.r, this.g, this.b);
	}
	
	public static Color parse(String str) {
		String[] parts = str.split(",");
		
		if ( parts.length != 3 ) {
			throw new IllegalArgumentException("invalid Color string: value=\"" + str + "\"");
		}
		int r = Integer.parseInt(parts[0].trim());
		int g = Integer.parseInt(parts[1].trim());
		int b = Integer.parseInt(parts[2].trim());
		
		return new Color(r,g,b);
	}
	
	@Override
	public String toString(){
		return String.format("%s[%d,%d,%d]", getClass().getSimpleName(), r, g, b);
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		else if ( obj == null || getClass() != obj.getClass() ) {
			return false;
		}
		
		Color other = (Color)obj;
		return this.r == other.r && this.g == other.g && this.b == other.b;
	}

	@Override
	public int hashCode() {
    	int hash = 17;

    	hash = (31 * hash) + r;
    	hash = (31 * hash) + g;
    	hash = (31 * hash) + b;

    	return hash;
	}
}
