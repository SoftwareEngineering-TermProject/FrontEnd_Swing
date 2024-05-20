package style;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ProjStyleScrollBar extends JScrollBar {

    public ProjStyleScrollBar() {
        setUI(new ProjStyleScrollBarUI());
        setPreferredSize(new Dimension(8, 8));
        setForeground(ProjColor.tableHeaderGray);
        setBackground(ProjColor.customGray);
    }
}