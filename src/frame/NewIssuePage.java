package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleScrollBar;

public class NewIssuePage extends JDialog {
	
	
	public NewIssuePage() {
		setSize(560, 480);
		setLocationRelativeTo(null); // 화면 중앙 위치
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModal(true);
		setUndecorated(true); // 타이틀바 제거
		setOpacity(0.0f); // 처음엔 안보이게
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(ProjColor.customGray);
		panel1.setLayout(null);
		panel1.setBorder(BorderFactory.createLineBorder(ProjColor.clickedCustomDarkGray, 5));
		
		JLabel lbl1 = new JLabel("New Issue");
		lbl1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(lbl1);
		lbl1.setBounds(170, 1, 400, 80);
		
		JLabel lbl2 = new JLabel("Issue title");
		lbl2.setFont(new Font(null, Font.PLAIN, 15));
		panel1.add(lbl2);
		lbl2.setBounds(35, 55, 400, 80);
		
		JLabel lbl3 = new JLabel("description");
		lbl3.setFont(new Font(null, Font.PLAIN, 15));
		panel1.add(lbl3);
		lbl3.setBounds(35, 158, 400, 80);
		
		JTextField tf1 = new JTextField();
		tf1.setBackground(ProjColor.customDarkGray);
		tf1.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(37, 116, 465, 56);
		
		JTextArea ta1 = new JTextArea();
		ta1.setBackground(ProjColor.customDarkGray);
		ta1.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		panel1.add(ta1);
		ta1.setBounds(37, 216, 465, 150);
		
		ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "Add");
		panel1.add(btn1);
		btn1.setBounds(108, 380, 111, 56);
		btn1.setPreferredSize(new Dimension(111, 56));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customWhiteRed, ProjColor.clickedCustomWhiteRed, Color.BLACK, "Cancel");
		panel1.add(btn2);
		btn2.setBounds(325, 380, 111, 56);
		btn2.setPreferredSize(new Dimension(111, 56));

		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		Timer timer = new Timer(1, new ActionListener() {
            float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity < 1.0f) {
                    opacity += 0.1f;
                    setOpacity(Math.min(opacity, 1.0f));
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();

		add(panel1);
		
		setVisible(true);
		
		
	}
	
}

	

	/*
	public NewIssuePage() {
		setSize(700,400);
		setLocationRelativeTo(null);
		setModal(true);
		setUndecorated(true);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(ProjColor.customWhiteGray);
		panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customWhiteGray, ProjColor.customWhiteGray, Color.BLACK, "X");
		btn1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(btn1);
		btn1.setBounds(650, 20, 30, 35);
		btn1.setPreferredSize(new Dimension(30, 35));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "ADD");
		btn2.setFont(new Font(null, Font.PLAIN, 20));
		panel1.add(btn2);
		btn2.setBounds(550, 70, 80, 35);
		btn2.setPreferredSize(new Dimension(80, 35));
		
		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		JLabel lbl1 = new JLabel("Add New Issue");
		lbl1.setFont(new Font(null, Font.PLAIN, 40));
		panel1.add(lbl1);
		lbl1.setBounds(10, 10, 300, 40);
		lbl1.setPreferredSize(new Dimension(300, 40));
		
		JLabel lbl2 = new JLabel("Issue title :");
		lbl2.setFont(new Font(null, Font.PLAIN, 20));
		panel1.add(lbl2);
		lbl2.setBounds(10, 70, 190, 40);
		lbl2.setPreferredSize(new Dimension(190, 40));
		
		JLabel lbl3 = new JLabel("Add description");
		lbl3.setFont(new Font(null, Font.PLAIN, 20));
		panel1.add(lbl3);
		lbl3.setBounds(10, 120, 190, 40);
		lbl3.setPreferredSize(new Dimension(190, 40));
		
		JTextField tf = new JTextField();
		tf.setBorder(null);
		tf.setBackground(ProjColor.customLightGray);
		panel1.add(tf);
		tf.setBounds(115, 80, 190, 30);
		tf.setPreferredSize(new Dimension(190, 30));
		
		JTextArea ta = new JTextArea();
		ta.setFont(new Font("맑은 고딕", 1, 15));
		ta.setBackground(ProjColor.customLightGray);
		ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
		ta.setBorder(null);
		
		JScrollPane scrollPane = new JScrollPane(ta);
		scrollPane.setVerticalScrollBar(new ProjStyleScrollBar());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel1.add(scrollPane);
        scrollPane.setBounds(30, 165, 600, 200);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        scrollPane.setBorder(null);//BorderFactory.createLineBorder(Color.GRAY, 1));
		
		add(panel1);
		
		setVisible(true);
	}
}
*/
