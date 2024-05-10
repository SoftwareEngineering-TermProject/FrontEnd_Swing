package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;

import javax.swing.*;

public class ProjectFrame extends JFrame{

	Color userGray = new Color(196, 196, 196);
	
	Color userDarkGray = new Color(157, 157, 157);
	Color clickedUserDarkGray = new Color(120, 120, 120);
	
	Color userDarkSkyBlue = new Color(78, 157, 157);
	Color clickedUserDarySkyBlue = new Color(70, 140, 140);
	
	Color userWhiteRed = new Color(217, 102, 102);
	Color clickedUserWhiteRed = new Color(200, 75, 75);
	
	Color userWhiteGray = new Color(215, 215, 215);
	
	public ProjectFrame(String name) {
		setTitle(name);
		setSize(1150, 820);
		setLocationRelativeTo(null); // 화면 중앙 위치
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(userGray);
		panel1.setLayout(null);
		
		JLabel lbl1 = new JLabel(name);
		lbl1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(lbl1);
		lbl1.setBounds(20, 1, 800, 80);
		
		ProjStyleButton btn1 = new ProjStyleButton(userDarkGray, clickedUserDarkGray, Color.BLACK, "+ new issue");
		panel1.add(btn1);
		btn1.setBounds(50, 100, 220, 50);
		btn1.setPreferredSize(new Dimension(220, 50));
		
		ProjStyleButton btn2 = new ProjStyleButton(userDarkGray, clickedUserDarkGray, Color.BLACK, "+ add member");
		panel1.add(btn2);
		btn2.setBounds(285, 100, 220, 50);
		btn2.setPreferredSize(new Dimension(220, 50));
		
		JTextField tf1 = new JTextField();
		tf1.setBackground(userWhiteGray);
		tf1.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(745, 100, 220, 50);
		
		ProjStyleButton btn3 = new ProjStyleButton(userDarkGray, clickedUserDarkGray, Color.BLACK, "search");
		panel1.add(btn3);
		btn3.setBounds(980, 100, 100, 50);
		btn3.setPreferredSize(new Dimension(100, 50));
		
		
		String header[] = {"ID", "Issue title", "Reporter", "Fixer", "Assignee", "Priority", "Status", "Date"};
		String contents[][] = {
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "}
		};
		
		//ProjStyleTable table1 = new ProjStyleTable(contents, header);
		//table1.setBackground(Color.BLACK);
		/*
		panel1.add(table1);
		JScrollPane scl = new JScrollPane(table1);
		panel1.add(scl);
		scl.setLocation(75,200);
        scl.setSize(1000,500);
		*/
		add(panel1);
		
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose(); // 현재 프레임만 없애기
			}
		});
	}
}
