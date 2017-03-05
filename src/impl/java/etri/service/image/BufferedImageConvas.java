package etri.service.image;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import camus.service.geo.Point;
import camus.service.geo.Polygon;
import camus.service.geo.Rectangle;
import camus.service.geo.Size2d;
import camus.service.image.Color;
import camus.service.image.ImageConvas;
import camus.service.vision.Image;

import net.jcip.annotations.GuardedBy;
import utils.swing.ImageUtils;



/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class BufferedImageConvas implements ImageConvas {
	private final Size2d m_size;
	@GuardedBy("this") private BufferedImage m_bi;
	
	public BufferedImageConvas(BufferedImage bi) {
		m_size = new Size2d(bi.getWidth(), bi.getHeight());
		m_bi = bi;
	}
	
	public BufferedImageConvas(Size2d size) {
		m_size = size;
		m_bi = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public Size2d getConvasSize() {
		return m_size;
	}
	
	public synchronized void resize(Size2d newSize) {
		BufferedImage prevBi = m_bi;
		m_bi = new BufferedImage(newSize.width, newSize.height, BufferedImage.TYPE_INT_RGB);
		
		draw(prevBi);
	}

	@Override
	public void drawImage(Image image) {
		draw(ImageUtils.toBufferedImage(image.bytes));
	}
	
	public synchronized void setBufferedImage(final BufferedImage bi) {
		if ( m_bi.getWidth() != bi.getWidth() || m_bi.getHeight() != bi.getHeight() ) {
			throw new IllegalArgumentException("incompatible getHeight: different size");
		}
		
		m_bi = bi;
	}
	
	public synchronized void draw(final BufferedImage bi) {
		Graphics2D g = m_bi.createGraphics();
		g.drawImage(bi, 0, 0, m_size.width, m_size.height, null);
		g.dispose();
	}
	
	public synchronized void drawBufferedImage(final BufferedImage bi, final Point pt,
												final Size2d size) {
		Graphics2D g = m_bi.createGraphics();
		g.drawImage(bi, pt.xpos, pt.ypos, size.width, size.height, null);
		g.dispose();
	}

	@Override
	public synchronized void drawLine(Point fromPt, Point toPt, Color color, int thickness) {
		Graphics2D g = m_bi.createGraphics();
		g.setColor(color.toAwtColor());
		g.setStroke(new BasicStroke(thickness));
		g.drawLine(fromPt.xpos, fromPt.ypos, toPt.xpos, toPt.ypos);
		g.dispose();
	}

	@Override
	public synchronized void drawRect(Rectangle rect, Color color, int thickness) {
		Graphics2D g = m_bi.createGraphics();
		g.setColor(color.toAwtColor());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if ( thickness < 0 ) {
			g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		}
		else if ( thickness > 0 ) {
			g.setStroke(new BasicStroke(thickness));
			g.drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		}
		g.dispose();
	}

	@Override
	public void drawPolygon(Polygon polygon, Color color, int thickness) {
		// Polygon에서 java.awt.Polygon으로 변환하는 과정이 시간이 소요될 수 있기 때문에
		// synchronized 블럭으로 진입하기 전에 수행한다.
		if ( polygon.points.length == 0 ) {
			return;
		}
		
		java.awt.Polygon jpoly = new java.awt.Polygon();
		for ( int i =0; i < polygon.points.length; ++i ) {
			jpoly.addPoint((int)polygon.points[i].xpos, (int)polygon.points[i].ypos);
		}
		
		synchronized ( this ) {
			Graphics2D g = m_bi.createGraphics();
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(color.toAwtColor());
			if ( thickness >= 0 ) {
				g.setStroke(new BasicStroke(thickness));
				g.drawPolygon(jpoly);
			}
			else {
				g.fillPolygon(jpoly);
			}
			g.dispose();
		}
	}

	@Override
	public synchronized void drawString(String str, Point loc, int fontSize, Color color) {
		Graphics2D g = m_bi.createGraphics();
		g.setColor(color.toAwtColor());
		g.setFont(new Font("Ariel", Font.BOLD, fontSize));
		g.drawString(str, loc.xpos, loc.ypos);
		g.dispose();
	}

	@Override
	public void clear() {
		drawRect(new Rectangle(new Point(0,0), m_size), Color.BLACK, -1);
	}

	public BufferedImage getBufferedImage() {
		return m_bi;
	}
}
