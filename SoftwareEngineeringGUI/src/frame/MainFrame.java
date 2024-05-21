package frame;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleScrollBar;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class MainFrame extends JFrame {
	private ArrayList<Object[]> projectList = new ArrayList<>();
	private JPanel btnPanel;
	private JScrollPane scr;
	private int i = 0;
	
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
		panel1.setBackground(ProjColor.customGray);
		
		// panel1 레이아웃
		panel1.setLayout(null);
		
		btnPanel = new JPanel(); // 내부 배경 패널
		btnPanel.setBackground(ProjColor.customDarkGray);
		btnPanel.setLayout(null);
		//btnPanel.setBounds(48, 95, 1062, 680);
		btnPanel.setPreferredSize(new Dimension(1000, 120));;
		
		scr = new JScrollPane(btnPanel);
		scr.setBackground(ProjColor.customDarkGray);
		scr.setBorder(null);
		panel1.add(scr);
		scr.setBounds(48, 95, 1062, 680);
		scr.setVerticalScrollBar(new ProjStyleScrollBar());
		scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		ProjStyleButton btnk = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, "Project1 - Trouble ticket system");
		panel1.add(btnk);
		btnk.setBounds(30, 30, 30, 30);
		btnk.setPreferredSize(new Dimension(30, 30));
		
		btnk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				addTempButton();
			}
		});
		
		//라벨
		JLabel lbl1 = new JLabel("All Projects");
		lbl1.setFont(new Font(null, Font.PLAIN, 50)); // 폰트, 굵게, 크기
		panel1.add(lbl1);
		
		lbl1.setBounds(20,1,400,80);
		/*
		ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, "Project1 - Trouble ticket system");
		btnPanel.add(btn1);
		btn1.setBounds(31, 35, 997, 75);
		btn1.setPreferredSize(new Dimension(997, 75));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				new ProjectFrame("Project1 - Trouble ticket system");
				setVisible(false);
				dispose();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, "Open source SW project-Chatda");
		btnPanel.add(btn2);
		btn2.setBounds(31, 145, 997, 75);
		btn2.setPreferredSize(new Dimension(997, 75));
		
		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				new ProjectFrame("Open source SW project-Chatda");
			}
		});
		
		ProjStyleButton btn3 = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, "League of Legend");
		btnPanel.add(btn3);
		btn3.setBounds(31, 255, 997, 75);
		btn3.setPreferredSize(new Dimension(997, 75));
		
		btn3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				new ProjectFrame("League of Legend");
			}
		});
		
		*/
		
		/*
		for(int i = 0; i < 10; i++) {
			ProjStyleButton tempbtn = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, "TEMP");
			btnPanel.add(tempbtn);
			tempbtn.setBounds(31, 35 + 110 * i, 997, 75);
			tempbtn.setPreferredSize(new Dimension(997, 75));
			btnPanel.setPreferredSize(new Dimension(1000, 120 + 110 * i));
		}
		*/
		ProjStyleButton btn4 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "+ new project");
		panel1.add(btn4);
		btn4.setBounds(890, 24, 220, 57);
		btn4.setPreferredSize(new Dimension(220, 57));
		
		btn4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { // 원래 기능 + new
            	parent();
            }
        });
		
		add(panel1);
		
		// visible
		setVisible(true);
	}
	
	public void parent() {
		new CreateProjectDialog(this);
	}
	
	public void addProjectList(String title, String description) {
		Object[] array1 = {title, description};
		projectList.add(array1);
		
	}
	
	public void addTempButton() {
		ProjStyleButton tempbtn = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, "TEMP");
		tempbtn.setBounds(31, 35 + 110 * i, 997, 75);
		btnPanel.add(tempbtn);
		tempbtn.setPreferredSize(new Dimension(997, 75));
		btnPanel.setPreferredSize(new Dimension(1000, 120 + 110 * i));
		btnPanel.revalidate();
		i++;
		
		scr.revalidate();
        scr.repaint();
	}
}


