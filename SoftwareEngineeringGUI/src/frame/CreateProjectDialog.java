package frame;

import style.ProjColor;
import style.ProjStyleButton;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;

public class CreateProjectDialog extends JDialog { // Modal 창 만들기 위해
	
	private MainFrame parentFrame;
	
	public CreateProjectDialog(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		setTitle("New Project");
		setSize(560, 480);
		setLocationRelativeTo(null); // 화면 중앙 위치
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModal(true);
		setUndecorated(true); // 타이틀바 제거
		setOpacity(0.0f); // 처음엔 안보이게
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(ProjColor.customGray);
		panel1.setLayout(null);
		
		JLabel lbl1 = new JLabel("New Project");
		lbl1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(lbl1);
		lbl1.setBounds(140, 1, 400, 80);
		
		JLabel lbl2 = new JLabel("project name");
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
		
		ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "Create");
		panel1.add(btn1);
		btn1.setBounds(108, 380, 111, 56);
		btn1.setPreferredSize(new Dimension(111, 56));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				tempfunction();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customWhiteRed, ProjColor.clickedCustomWhiteRed, Color.BLACK, "Cancel");
		panel1.add(btn2);
		btn2.setBounds(325, 380, 111, 56);
		btn2.setPreferredSize(new Dimension(111, 56));
		
		System.out.println("33");
		
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
	
	public void tempfunction() {
		parentFrame.setVisible(false);
		parentFrame.dispose();
	}
}
