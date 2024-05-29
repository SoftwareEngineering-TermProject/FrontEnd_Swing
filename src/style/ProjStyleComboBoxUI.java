package style;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

import java.awt.*;

public class ProjStyleComboBoxUI extends BasicComboBoxUI {
	
	private Color disabledBack;
	private Color disabledFore;
	
	public ProjStyleComboBoxUI(Color disabledBack, Color disabledFore) {
		this.disabledBack = disabledBack;
		this.disabledFore = disabledFore;
	}
	
	@Override
    protected JButton createArrowButton() {
        return new JButton() {
            @Override
            public int getWidth() {
                return 0;
            }
        };
    }
	
	@Override
    protected void installDefaults() {
        super.installDefaults();
        UIManager.put("ComboBox.disabledBackground", disabledBack); // disabledBackground 변경
        UIManager.put("ComboBox.disabledForeground", disabledFore); // disabledForeground 변경
    }
    
}
