package GUI;

import javax.swing.*;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.*;

public class OutlineButton extends JButton {
    private final Color background;
    private final Color foreground;

    int paddingWidth = 15, paddingHeight = 5;

    public OutlineButton(Color background, Color foreground, String txt) {
        this.background = background;
        this.foreground = foreground;
        setText(txt);

        Dimension dimension = getPreferredSize();
        int w = (int) dimension.getWidth() + paddingWidth * 2;
        int h = (int) dimension.getHeight() + paddingHeight * 2;

        setPreferredSize(new Dimension(w, h));
        setOpaque(false);
        setBorder(null);
        setBackground(null);
        setForeground(background);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(background);
                setForeground(foreground);
                revalidate();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(null);
                setForeground(background);
                revalidate();
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension dimension = getPreferredSize();
        int w = (int) dimension.getWidth() - 1;
        int h = (int) dimension.getHeight()- 1;

        if(getBackground() != null) {
            g2.setColor(getBackground());
            g2.fillRoundRect(1, 1, (int) (w - 1 / 3.5), (int) (h - 1 / 2.8), 35, 35);
        }

        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, w, h, 35, 35);

        g2.setColor(getForeground());
        g2.setFont(new Font("맑은 고딕", 1, 18));

        FontMetrics fontMetrics = g2.getFontMetrics();
        Rectangle rectangle = fontMetrics.getStringBounds(getText(), g2).getBounds();

        g2.drawString(getText(), (w - rectangle.width) / 2, (h - rectangle.height) / 2 + fontMetrics.getAscent());
    }
}
class CircleButton extends JButton {
    private final Color unClickBackground;
    private final Color clickBackground;
    private final Color foreground;

    int size = 45;
    int paddingWidth = 15, paddingHeight = 3;

    public CircleButton(Color unClickBackground, Color clickBackground, Color foreground) {
        this.unClickBackground = unClickBackground;
        this.clickBackground = clickBackground;
        this.foreground = foreground;

        setPreferredSize(new Dimension(size, size));
        setOpaque(false);
        setBorder(null);
        setBackground(unClickBackground);
        setForeground(foreground);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(clickBackground);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(unClickBackground);
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension dimension = getPreferredSize();
        int w = (int) dimension.getWidth();
        int h = (int) dimension.getHeight();

        g2.setColor(getBackground());
        g2.fillOval(0, 0, size, size);

        g2.drawImage(new ImageIcon(new ImageIcon("D:\\icon.png").getImage()
                .getScaledInstance(25, 25, Image.SCALE_SMOOTH)).getImage(), 10, 10, null);
    }
}