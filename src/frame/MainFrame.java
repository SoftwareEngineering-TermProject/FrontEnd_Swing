package frame;

import style.ProjStyleButton;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MainFrame extends JFrame {
	
	// 색깔들은 나중에 클래스 하나로 뭉치자
	Color userGray = new Color(196, 196, 196);
			
	Color userDarkGray = new Color(157, 157, 157);
	Color clickedUserDarkGray = new Color(120, 120, 120);
			
	Color userDarkSkyBlue = new Color(78, 157, 157);
	Color clickedUserDarySkyBlue = new Color(70, 140, 140);
	
	//생성자
	public MainFrame() {
		setTitle("Main Frame");
		setSize(1150,820);
		setLocationRelativeTo(null); // 화면 중앙 위치
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// layout manager 전달
		//setLayout(null);
		
		// panel 생성
		JPanel panel1 = new JPanel(); // 배경 패널
		panel1.setBackground(userGray);
		
		// panel1 레이아웃
		panel1.setLayout(null);
		
		JPanel panel2 = new JPanel(); // 내부 배경 패널
		panel2.setBackground(userDarkGray);
		panel2.setLayout(null);
		
		//panel2.setSize(getPreferredSize());
		panel1.add(panel2);
		
		panel2.setBounds(48, 95, 1062, 680);
		
		//라벨
		JLabel lbl1 = new JLabel("All Projects");
		lbl1.setFont(new Font(null, Font.PLAIN, 50)); // 폰트, 굵게, 크기
		panel1.add(lbl1);
		
		lbl1.setBounds(20,1,400,80);
		
		ProjStyleButton btn1 = new ProjStyleButton(userDarkSkyBlue, clickedUserDarySkyBlue, Color.BLACK, "Project1 - Trouble ticket system                                                                                         admin   5");
		panel2.add(btn1);
		btn1.setBounds(31, 35, 997, 75);
		btn1.setPreferredSize(new Dimension(997, 75));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				new ProjectFrame("Project1 - Trouble ticket system");
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(userDarkSkyBlue, clickedUserDarySkyBlue, Color.BLACK, "Open source SW project-Chatda                                                                                              dev   4");
		panel2.add(btn2);
		btn2.setBounds(31, 145, 997, 75);
		btn2.setPreferredSize(new Dimension(997, 75));
		
		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				new ProjectFrame("Open source SW project-Chatda");
			}
		});
		
		ProjStyleButton btn3 = new ProjStyleButton(userDarkSkyBlue, clickedUserDarySkyBlue, Color.BLACK, "League of Legend                                                                                                        tester   5");
		panel2.add(btn3);
		btn3.setBounds(31, 255, 997, 75);
		btn3.setPreferredSize(new Dimension(997, 75));
		
		btn3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				new ProjectFrame("League of Legend");
			}
		});
		
		ProjStyleButton btn4 = new ProjStyleButton(userDarkGray, clickedUserDarkGray, Color.BLACK, "+ new project");
		panel1.add(btn4);
		btn4.setBounds(890, 24, 220, 57);
		btn4.setPreferredSize(new Dimension(220, 57));
		
		btn4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { // 원래 기능 + new
                new CreateProjectFrame();
            }
        });
		
		add(panel1);
		
		// visible
		setVisible(true);
	}
}


