package style;

import javax.swing.*;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.*;

public class ProjStyleButton extends JButton {

    int paddingWidth = 15, paddingHeight = 3;
    private Color unClickBackground;
    private Color clickBackground;
    private Color foreground;

    public ProjStyleButton(Color unClickBackground, Color clickBackground, Color foreground, String txt) {

    	this.unClickBackground = unClickBackground;
    	this.clickBackground = clickBackground;
    	this.foreground = foreground;
    	
        setFont(new Font("맑은 고딕", 1, 18));
        setText(txt);

        Dimension dimension = getPreferredSize();
        int w = (int) dimension.getWidth() + paddingWidth * 3;
        int h = (int) dimension.getHeight() + paddingHeight * 3;

        setPreferredSize(new Dimension(w, h));
        setOpaque(false);
        setBorder(null);
        setBackground(unClickBackground);
        setForeground(foreground);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { // 누르고 있을 때
            	pressedSetBack();
            }

            @Override
            public void mouseReleased(MouseEvent e) { // 손 뗐을 때
            	releasedSetBack();
            }
        });
    }
    
    public void setUnClickBackground(Color unClickBackground) {
    	this.unClickBackground = unClickBackground;
    }
    
	public void setClickBackground(Color clickBackground) {
		this.clickBackground = clickBackground;
    }
    
    public void pressedSetBack() {
    	setBackground(this.clickBackground);
    }
    
    public void releasedSetBack() {
    	setBackground(this.unClickBackground);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension dimension = getPreferredSize();
        int w = (int) dimension.getWidth();
        int h = (int) dimension.getHeight();

        g2.setColor(getBackground());
        g2.fillRect(0, 0, w, h);

        g2.setColor(getForeground());

        FontMetrics fontMetrics = g2.getFontMetrics();
        Rectangle rectangle = fontMetrics.getStringBounds(getText(), g2).getBounds();

        g2.drawString(getText(), (w - rectangle.width) / 2, (h - rectangle.height) / 2 + fontMetrics.getAscent());
    }
}