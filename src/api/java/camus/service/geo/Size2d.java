package camus.service.geo;

import java.awt.Dimension;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;


/**
 * <code>Size2D</code>은 2차원 크기를 정의하는 클래스이다.
 * <p>
 * 2차원 크기는  폭과 높이로 정의된다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class Size2d {
	public static final Size2d S320x240 = new Size2d(320, 240);
	public static final Size2d S640x480 = new Size2d(640, 480);
	
	@PlanetField(ordinal=0) public int width;
	@PlanetField(ordinal=1) public int height;

	public Size2d() { }
	
	public Size2d(int width, int height) {
		this.height = height;
		this.width = width;
	}
	
	public Size2d(Dimension dim) {
		this.width = dim.width;
		this.height = dim.height;
	}
	
	public Size2d(java.awt.Rectangle rect) {
		this.width = rect.width;
		this.height = rect.height;
	}
	
	public int getArea() {
		return this.width * this.height;
	}
	
	public final Dimension toDimension() {
		return new Dimension(this.width, this.height);
	}
	
	public Size2d duplicate() {
		return new Size2d(width, height);
	}
	
	public Point[] toCorners() {
		return new Point[]{ new Point(0,0), new Point(width-1, 0),
							new Point(width-1, height-1), new Point(0, height-1)};
	}
	
	@Override
	public String toString() {
		return "" + width + "x" + height;
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		else if ( obj == null || getClass() != obj.getClass() ) {
			return false;
		}
		
		Size2d other = (Size2d)obj;
		return this.height == other.height && this.width == other.width;
	}
	
	@Override
	public int hashCode() {
    	int hash = 17;

    	hash = (31 * hash) + this.width;
    	hash = (31 * hash) + this.height;

    	return hash;
	}
	
	public static Size2d parseSize2d(String str) {
		Size2d dim = new Size2d();
		
		int index = str.toUpperCase().indexOf('X');
		dim.width = Integer.parseInt(str.substring(0, index).trim());
		dim.height = Integer.parseInt(str.substring(index+1).trim());
		
		return dim;
	}
	
	public static Size2d toSize2D(java.awt.Rectangle rect) {
		return new Size2d(rect.width, rect.height);
	}
	
	public static Size2d minus(Size2d size1, Size2d size2) {
		return new Size2d(size1.width-size2.width, size1.height-size2.height);
	}
	
	public static Size2d multiply(Size2d size1, float ratio) {
		return new Size2d(Math.round(size1.width * ratio), Math.round(size1.height * ratio));
	}
}
