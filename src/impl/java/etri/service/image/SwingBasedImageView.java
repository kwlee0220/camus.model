package etri.service.image;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.annotation.concurrent.GuardedBy;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import camus.service.geo.Point;
import camus.service.geo.Polygon;
import camus.service.geo.Rectangle;
import camus.service.geo.Size2d;
import camus.service.image.Color;
import camus.service.image.ImageConvas;
import camus.service.image.ImageView;
import camus.service.vision.Image;

import utils.Initializable;
import utils.swing.SwingUtils;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class SwingBasedImageView implements ImageView, Initializable {
	private static final String DEFAULT_TITLE = "Image View";
	
	// properties (BEGIN)
	private volatile String m_title;
	private volatile Size2d m_size;						// 각 view의 크기 (해상도)
	private volatile boolean m_alwaysOnTop = false;
	private volatile boolean m_frameDecoration = true;	// 윈도우의 타이틀바와  테두리를 보여줄지 여부를 결정
														// ( true: 보임, false: 안보임)
	private volatile boolean m_resizable = false;
	private volatile int m_monitorIndex =-1;			// optional (default: -1)
	// properties (END)
	
	private volatile JFrame m_frame;
	private volatile ViewPanel m_panel;
	
	@GuardedBy("this") private boolean m_visible = false;
	@GuardedBy("this") private BufferedImageConvas m_convas;
	
	class OnWindowExitHandler extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent ev) {
			setVisible(false);
		}
	};
	
	public SwingBasedImageView() { }
	
	public void setTitle(String title) {
		m_title = title;
	}
	
	public void setViewSize(String resolStr) {
		setViewSize(Size2d.parseSize2d(resolStr)); 
	}
	
	/**
	 * 이미지 뷰 보드에서  display되는 뷰의 해상도를 설정한다.
	 * 
	 * @param viewSize	이미지 뷰의 해상도.
	 * @throws	InvalidArgumentException	<code>viewSize</code>가 null이거나 유효하지 않은 경우.
	 */
	public void setViewSize(Size2d size) {
		if ( size == null ) {
			throw new IllegalArgumentException("Resolution is null");
		}
		
		synchronized ( this ) {
			m_size = size;
			
			if ( m_frame != null ) {	// 이미 컴포넌트가 초기화가 수행된 경우
				Container top = m_frame.getContentPane();
				
				m_panel.setPreferredSize(new Dimension(m_size.width, m_size.height));
				relocateFrame();
				
				top.removeAll();
				top.add(m_panel, BorderLayout.CENTER);
				
				m_frame.pack();
				
				m_convas = new BufferedImageConvas(m_size);
			}
		}
	}
	
	public void setFrameDecoration(boolean flag) {
		m_frameDecoration = flag;
	}
	
	public void setAlwaysOnTop(boolean flag) {
		m_alwaysOnTop = flag;
	}
	
	public synchronized void setResizable(boolean resizable) {
		m_resizable = resizable;
	}
	
	public void setMonitorIndex(int monitorIndex) {
		m_monitorIndex = monitorIndex;
	}

	@Override
	public void initialize() throws Exception {
		if ( m_size == null ) {
			// ViewSize가 설정되지 않은 경우는 전체 화면으로 설정한다.
			m_size = Size2d.toSize2D(SwingUtils.getScreenRectangle(m_monitorIndex));
		}
		if ( m_title == null ) {
			m_title = DEFAULT_TITLE;
		}
		
		if ( m_monitorIndex < 0 ) {
			m_frame = new JFrame(m_title);
		}
		else {
			GraphicsDevice gd = SwingUtils.getScreen(m_monitorIndex);
			
			m_frame = new JFrame(m_title, gd.getDefaultConfiguration());
		}
		m_frame.setVisible(false);
		m_frame.getContentPane().setSize(m_size.width, m_size.height);
		m_frame.setBackground(java.awt.Color.WHITE);
		m_frame.setAlwaysOnTop(m_alwaysOnTop);
		m_frame.setUndecorated(!m_frameDecoration);
		m_frame.addWindowListener(new OnWindowExitHandler());
		
		m_panel = new ViewPanel(m_size, new GridLayout(1, 1));
		m_panel.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 0));
		m_frame.getContentPane().add(m_panel, BorderLayout.CENTER);
		
		relocateFrame();
		m_frame.pack();
		
		m_convas = new BufferedImageConvas(m_size);
	}
	
	@Override
	public void destroy() throws Exception {
		m_frame.setVisible(false);
		m_frame.getContentPane().removeAll();
		
		m_frame.dispose();
	}
	
	/**
	 * 이미지 뷰 보드에서 display 되는 뷰의 해상도를 반환한다.
	 * 
	 * @return	이미지 뷰 해상도.
	 */
	@Override
	public synchronized Size2d getConvasSize() {
		return m_size;
	}

	@Override
	public synchronized Size2d getViewSize() {
		return m_size;
	}
	
	public int getMonitorIndex() {
		return m_monitorIndex;
	}

	@Override
	public synchronized boolean getVisible() {
		return m_visible;
	}

	@Override
	public void setVisible(boolean flag) {
		m_visible = flag;
		m_frame.setVisible(flag);
	}

	@Override
	public void drawImage(Image image) {
		resizeIfNecessary(image.format.getSize());
		m_convas.drawImage(image);
	}

	@Override
	public void draw(final BufferedImage bi) {
		resizeIfNecessary(new Size2d(bi.getWidth(), bi.getHeight()));
		m_convas.draw(bi);
	}

	@Override
	public void drawLine(Point fromPt, Point toPt, Color color, int thickness) {
		m_convas.drawLine(fromPt, toPt, color, thickness);
	}

	@Override
	public void drawRect(Rectangle rect, Color color, int thickness) {
		m_convas.drawRect(rect, color, thickness);
	}

	@Override
	public void drawPolygon(Polygon polygon, Color color, int thickness) {
		m_convas.drawPolygon(polygon, color, thickness);
	}

	@Override
	public void drawString(String str, Point loc, int fontSize, Color color) {
		m_convas.drawString(str, loc, fontSize, color);
	}

	@Override
	public synchronized void clear() {
		m_convas.clear();
	}
	
	public void addMouseListener(MouseListener listener) {
		m_panel.addMouseListener(listener);
	}
	
	public void addMouseMotionListener(MouseMotionListener listener) {
		m_panel.addMouseMotionListener(listener);
	}
	
	public void removeMouseListener(MouseListener listener) {
		m_panel.removeMouseListener(listener);
	}
	
	public void removeMouseMotionListener(MouseMotionListener listener) {
		m_panel.removeMouseMotionListener(listener);
	}
	
	public ImageConvas getConvas() {
		return m_convas;
	}

	@Override
	public void updateView() {
		m_panel.repaint();
	}
	
	public JFrame getJFrame() {
		return m_frame;
	}
	
	public GraphicsDevice getGraphicsDevice() {
		return SwingUtils.getScreen(m_monitorIndex);
	}
	
	private void relocateFrame() {
		GraphicsConfiguration config = m_frame.getGraphicsConfiguration(); 
		java.awt.Rectangle rect = config.getBounds();
		
		m_frame.setLocation(rect.x + (rect.width-m_size.width)/2,
							rect.y + (rect.height-m_size.height)/2);
	}
	
	class ViewPanel extends JPanel {
		private static final long serialVersionUID = -462483431223342055L;

		ViewPanel(Size2d size, LayoutManager lm) {
			super(lm);
			
			setPreferredSize(size.toDimension());
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D)g;
			synchronized ( this ) {
				synchronized ( m_convas ) {
					g2d.drawImage(m_convas.getBufferedImage(), 0, 0, m_size.width, m_size.height, this);
				}
			}
		}
		
	}
	
	private void resizeIfNecessary(Size2d newSize) {
		if ( m_resizable && !m_size.equals(newSize) ) {
			setViewSize(newSize);
		}
	}
}
