package camus.service.geo;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;


/**
 * <code>Rectangle</code>은 사각형 영역을 표현하는 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class Rectangle {
	/** 사각형의 가장 왼쪽 X좌표 값. */
	@PlanetField(ordinal=0) public int left;
	/** 사각형의 가장 오른쪽 X 좌표 값. */
	@PlanetField(ordinal=1) public int right;
	/** 사각형의 가장 위쪽 Y 좌표 값. */
	@PlanetField(ordinal=2) public int top;
	/** 사각형의 가장 아래쪽 Y 좌표 값. */
	@PlanetField(ordinal=3) public int bottom;
	
	public Rectangle() { }
	
	public Rectangle(int left, int right, int top, int bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
	
	public Rectangle(float left, float right, float top, float bottom) {
		this.left = Math.round(left);
		this.right = Math.round(right);
		this.top = Math.round(top);
		this.bottom = Math.round(bottom);
	}
	
	public Rectangle(Rectangle rect) {
		this.left = rect.left;
		this.right = rect.right;
		this.top = rect.top;
		this.bottom = rect.bottom;
	}
	
	public Rectangle(Point pt, Size2d size) {
		this.left = pt.xpos;
		this.top = pt.ypos;
		this.right = pt.xpos + size.width -1;
		this.bottom = pt.ypos + size.height -1;
	}
	
	public Rectangle(Size2d size) {
		this.left = 0;
		this.top = 0;
		this.right = size.width -1;
		this.bottom = size.height -1;
	}
	
	public Rectangle(Point[] corner) {
		this.left = corner[0].xpos;
		this.top = corner[0].ypos;
		this.right = corner[2].xpos;
		this.bottom = corner[2].ypos;
	}
	
	public Rectangle(Point2f[] corner) {
		this.left = Math.round(corner[0].xpos);
		this.top = Math.round(corner[0].ypos);
		this.right = Math.round(corner[2].xpos);
		this.bottom = Math.round(corner[2].ypos);
	}
	
	public Rectangle(java.awt.Rectangle awtRect) {
		this.left = awtRect.x;
		this.top = awtRect.y;
		this.right = awtRect.x + awtRect.width -1;
		this.bottom = awtRect.y + awtRect.height -1;
	}
	
	public int getLeft() { return left; }
	public int getRight() { return right; }
	public int getTop() { return top; }
	public int getBottom() { return bottom; }

	public Point getOffset() { return new Point(left, top); }
	public Size2d getSize() { return new Size2d(getWidth(), getHeight()); }
	public int getX() { return left; }
	public int getY() { return top; }
	public int getWidth() { return right - left + 1; }
	public int getHeight() { return bottom - top + 1; }
	
	public Point getCenter() {
		return new Point(left + getWidth()/2, top + getHeight()/2);
	}
	
	public int getArea() {
		return getWidth() * getHeight();
	}
	
	public Point[] toPoints() {
		return new Point[] {
			new Point(left, top), new Point(right, top),
			new Point(right, bottom), new Point(left, bottom),
		};
	}
	
	public Point2f[] toPoint2fs() {
		return new Point2f[] {
			new Point2f(left, top), new Point2f(right, top),
			new Point2f(right, bottom), new Point2f(left, bottom),
		};
	}
	
	public Rectangle expand(Size2d margin, Size2d imageSize) {
		Rectangle expanded = new Rectangle();
		
		if ( (expanded.left = left - margin.width) < 0 ) {
			expanded.left = 0;
		}
		if ( (expanded.right = right + margin.width) >= imageSize.width ) {
			expanded.right = imageSize.width-1;
		}
		if ( (expanded.top = top - margin.height) < 0 ) {
			expanded.top = 0;
		}
		if ( (expanded.bottom = bottom + margin.height) >= imageSize.height ) {
			expanded.bottom = imageSize.height-1;
		}
		
		return expanded;
	}
	
	public Rectangle shift(Point offset) {
		return new Rectangle(left + offset.xpos, right + offset.xpos,
							top + offset.ypos, bottom + offset.ypos);
	}
	
	public final java.awt.Rectangle toAwtRectangle() {
		return new java.awt.Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public String toString() {
		return "[(" + getOffset() + "):" + getSize() + "]";
//		return "[" + left + "," + right + "," + top + "," + bottom + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		else if ( obj == null || getClass() != obj.getClass() ) {
			return false;
		}
		
		Rectangle other = (Rectangle)obj;
		return this.left == other.left && this.right == other.right
				&& this.top == other.top && this.bottom == other.bottom;
	}
	
	@Override
	public int hashCode() {
    	int hash = 17;

    	hash = (31 * hash) + this.left;
    	hash = (31 * hash) + this.right;
    	hash = (31 * hash) + this.top;
    	hash = (31 * hash) + this.bottom;

    	return hash;
	}
	
	public static Rectangle parseRectangle(String str) {
		int idx1 = str.indexOf(',');
		
		if ( idx1 < 0 ) {
			throw new IllegalArgumentException("invalid Rectangle string: value=\"" + str + "\"");
		}
		int left = Integer.parseInt(str.substring(0, idx1).trim());
		
		int idx2 = str.indexOf(',', idx1+1);
		int right = Integer.parseInt(str.substring(idx1+1, idx2).trim());
		
		int idx3 = str.indexOf(',', idx2+1);
		int top = Integer.parseInt(str.substring(idx2+1, idx3).trim());
		int bottom = Integer.parseInt(str.substring(idx3+1).trim());
		
		return new Rectangle(left, right, top, bottom);
	}
}
