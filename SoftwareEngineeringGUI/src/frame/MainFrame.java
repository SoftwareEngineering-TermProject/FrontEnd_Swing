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
	private ArrayList<ProjStyleButton> btnArray = new ArrayList<>();
	private JPanel btnPanel;
	private JScrollPane scr;
	private int numBtn = 0;
	
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
		
		//라벨
		JLabel lbl1 = new JLabel("All Projects");
		lbl1.setFont(new Font(null, Font.PLAIN, 50)); // 폰트, 굵게, 크기
		panel1.add(lbl1);
		lbl1.setBounds(20,1,400,80);
		
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
		Object[] array = {title, description};
		projectList.add(array);
		
	}
	
	public void repaintButtonPanel() {
		ProjStyleButton tempbtn = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, (String)projectList.get(numBtn)[0]);
		btnArray.add(tempbtn);
		btnArray.get(numBtn).setBounds(31, 35 + 110 * numBtn, 997, 75);
		btnPanel.add(btnArray.get(numBtn));
		btnArray.get(numBtn).setPreferredSize(new Dimension(997, 75));
		btnPanel.setPreferredSize(new Dimension(1000, 120 + 110 * numBtn));
		btnPanel.revalidate();
		numBtn++;
		
		scr.revalidate();
        scr.repaint();
	}
}


