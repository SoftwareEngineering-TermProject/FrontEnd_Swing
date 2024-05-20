package frame;

import style.ProjStyleComboBox;
import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleComboBoxUI;
import style.ProjStyleScrollBar;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;

public class IssuePage extends JDialog {
	
	public IssuePage () {
		setSize(600,700);
		setLocationRelativeTo(null);
		setModal(true);
		setUndecorated(true);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(Color.WHITE);
		panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		ProjStyleButton btn1 = new ProjStyleButton(Color.WHITE, Color.WHITE, Color.BLACK, "X");
		btn1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(btn1);
		btn1.setBounds(550, 20, 30, 35);
		btn1.setPreferredSize(new Dimension(30, 35));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		JLabel lbl1 = new JLabel("Issue title");
		lbl1.setFont(new Font(null, Font.BOLD, 40));
		panel1.add(lbl1);
		lbl1.setBounds(10, 20, 190, 40);
		lbl1.setPreferredSize(new Dimension(190, 40));
		
		JLabel lbl2 = new JLabel("Reporter");
		lbl2.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl2);
		lbl2.setBounds(30, 440, 100, 25);
		lbl2.setPreferredSize(new Dimension(100, 25));
		
		JLabel lbl3 = new JLabel("Fixer");
		lbl3.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl3);
		lbl3.setBounds(30, 480, 100, 25);
		lbl3.setPreferredSize(new Dimension(100, 25));
		
		JLabel lbl4 = new JLabel("Assignee");
		lbl4.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl4);
		lbl4.setBounds(30, 520, 100, 25);
		lbl4.setPreferredSize(new Dimension(100, 25));
		
		JLabel lbl5 = new JLabel("Priority");
		lbl5.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl5);
		lbl5.setBounds(30, 560, 100, 25);
		lbl5.setPreferredSize(new Dimension(100, 25));
		
		JLabel lbl6 = new JLabel("Status");
		lbl6.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl6);
		lbl6.setBounds(30, 600, 100, 25);
		lbl6.setPreferredSize(new Dimension(100, 25));
		
		JTextField tf = new JTextField("00-00-00");
		tf.setBackground(Color.WHITE);
		tf.setFont(new Font(null, Font.BOLD, 18));
		tf.setEditable(false);
		panel1.add(tf);
		tf.setBounds(30, 150, 100, 25);
		tf.setPreferredSize(new Dimension(100, 25));
		tf.setBorder(null);
		
		String[] tester = {"tester1", "tester2", "tester3", "tester4"};
		String[] dev = {"dev1", "dev2", "dev3", "dev4"};
		String[] priority = {"critical", "major", "high", "medium", "low", "trivial"};
		String[] status = {"new", "assigned", "fixed", "investigating", "closed"};
		
		ProjStyleComboBox cb1 = new ProjStyleComboBox(tester);
		cb1.setEnabled(false);
		panel1.add(cb1);
		cb1.setBounds(150, 440, 100, 25);
		cb1.setPreferredSize(new Dimension(100, 25));
		
		ProjStyleComboBox cb2 = new ProjStyleComboBox(dev);
		cb2.setEnabled(false);
		panel1.add(cb2);
		cb2.setBounds(150, 480, 100, 25);
		cb2.setPreferredSize(new Dimension(100, 25));
		
		ProjStyleComboBox cb3 = new ProjStyleComboBox(dev);
		cb3.getEditor().getEditorComponent().setBackground(Color.red);
		cb3.setEnabled(false);
		panel1.add(cb3);
		cb3.setBounds(150, 520, 100, 25);
		cb3.setPreferredSize(new Dimension(100, 25));
		
		ProjStyleComboBox cb4 = new ProjStyleComboBox(priority);
		cb4.setEnabled(false);
		panel1.add(cb4);
		cb4.setBounds(150, 560, 100, 25);
		cb4.setPreferredSize(new Dimension(100, 25));
		
		ProjStyleComboBox cb5 = new ProjStyleComboBox(status);
		cb5.setEnabled(false);
		panel1.add(cb5);
		cb5.setBounds(150, 600, 100, 25);
		cb5.setPreferredSize(new Dimension(100, 25));
		
		
		
		
		JTextArea ta = new JTextArea();
		ta.setFont(new Font("맑은 고딕", 1, 15));
		ta.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
		ta.setEditable(false);
		ta.setBorder(null);
		
		JScrollPane scrollPane = new JScrollPane(ta);
		scrollPane.setVerticalScrollBar(new ProjStyleScrollBar());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 수평 스크롤바 비활성화
        panel1.add(scrollPane);
        scrollPane.setBounds(30, 185, 540, 245);
        scrollPane.setPreferredSize(new Dimension(540, 245));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customGray, ProjColor.clickedCustomGray, Color.BLACK, "Edit");
		btn2.setFont(new Font("맑은 고딕", 1, 25));
		panel1.add(btn2);
		btn2.setBounds(40, 80, 55, 40);
		btn2.setPreferredSize(new Dimension(55, 40));
		btn2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(tf.isEditable()) {
					tf.setEditable(false);
					ta.setEditable(false);
					cb1.setEnabled(false);
					cb2.setEnabled(false);
					cb3.setEnabled(false);
					cb4.setEnabled(false);
					cb5.setEnabled(false);
					tf.setBackground(Color.WHITE);
				}
				else {
					tf.setEditable(true);
					ta.setEditable(true);
					cb1.setEnabled(true);
					cb2.setEnabled(true);
					cb3.setEnabled(true);
					cb4.setEnabled(true);
					cb5.setEnabled(true);
					tf.setBackground(ProjColor.customStaleSkyBlue);
				}
			}
		});
		
		
		
		add(panel1);
		
		setVisible(true);
	}
	
}
