package style;

import javax.swing.JScrollBar;

import java.awt.Color;
import java.awt.Dimension;

public class ProjStyleScrollBar extends JScrollBar {

    public ProjStyleScrollBar(Color bar, Color track) {
        setUI(new ProjStyleScrollBarUI());
        setPreferredSize(new Dimension(8, 8));
        setForeground(bar);
        setBackground(track);
    }
}