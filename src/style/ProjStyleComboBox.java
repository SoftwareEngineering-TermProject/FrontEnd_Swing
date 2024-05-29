package style;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;

public class ProjStyleComboBox extends JComboBox {
	
	private Color back;
	private Color fore;
	private Color disabledBack;
	private Color disabledFore;
	
	public ProjStyleComboBox(String[] items) {
		this.back = ProjColor.customStaleSkyBlue;
		this.fore = Color.BLACK;
		this.disabledBack = Color.WHITE;
		this.disabledFore = Color.BLACK;
		
		setFont(new Font("맑은 고딕", 1, 18));
		setUI(new ProjStyleComboBoxUI(disabledBack, disabledFore));
		StringSetModel(items);
	}
	
	public ProjStyleComboBox(String[] items, Color back, Color fore, Color disabledBack, Color disabledFore) {
		this.back = back;
		this.fore = fore;
		this.disabledBack = disabledBack;
		this.disabledFore = disabledFore;
		
		setFont(new Font("맑은 고딕", 1, 18));
		setUI(new ProjStyleComboBoxUI(disabledBack, disabledFore));
		StringSetModel(items);
	}
	
	public void StringSetModel(String[] items) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		int numItems = items.length;
		for (int i = 0; i < numItems; i++) {
			model.addElement(items[i]);
		}
        setModel(model);
	}
	
	@Override
	public void setEnabled(boolean b) {
        super.setEnabled(b);
        firePropertyChange( "enabled", !isEnabled(), isEnabled() );
        updateColors();
    }
	
	private void updateColors() {
        if (isEnabled()) {
            // 활성화 상태일 때의 색상 설정
            setBackground(back);
            setForeground(fore);
        }
        else {
        	setBackground(disabledBack);
            setForeground(disabledFore);
        }
    }
}
